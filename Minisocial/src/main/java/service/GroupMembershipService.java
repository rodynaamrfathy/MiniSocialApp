package service;

import Utils.GroupMembershipUtil;
import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupMembership;
import models.GroupMembershipDTO;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class GroupMembershipService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private GroupMembershipUtil util;

    public Object requestToJoinGroup(Long userId, Long groupId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        boolean alreadyMember = !em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId", GroupMembership.class)
                .setParameter("userId", userId)
                .setParameter("groupId", groupId)
                .getResultList()
                .isEmpty();

        List<String> errors = util.validateJoinRequest(user, group, alreadyMember);
        if (!errors.isEmpty()) return errors;

        GroupMembership membership = new GroupMembership();
        membership.setUser(user);
        membership.setGroup(group);
        membership.setJoinedDate(new Date());
        membership.setRole("member");

        boolean isCreator = group.getGroupAdmins().stream()
                .anyMatch(admin -> admin.getUser().getUserId().equals(userId));

        membership.setStatus(group.getIsOpen() || isCreator
                ? GroupMemberShipStatusEnum.approved
                : GroupMemberShipStatusEnum.pending);

        em.persist(membership);
        return GroupMembershipDTO.fromEntity(membership);
    }

    public Object getPendingRequestsForGroup(Long groupId) {
        Group group = em.find(Group.class, groupId);
        if (group == null) {
            return List.of("Group with ID " + groupId + " does not exist.");
        }

        TypedQuery<GroupMembership> query = em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.status = :status",
                GroupMembership.class);
        query.setParameter("groupId", groupId);
        query.setParameter("status", GroupMemberShipStatusEnum.pending);

        List<GroupMembership> results = query.getResultList();
        if (results.isEmpty()) {
            return List.of("There are no pending requests for this group.");
        }

        return results.stream().map(GroupMembershipDTO::fromEntity).collect(Collectors.toList());
    }

    public Object respondToRequest(Long groupId, Long userId, boolean approve) {
        TypedQuery<GroupMembership> query = em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.user.userId = :userId AND gm.status = :status",
                GroupMembership.class);
        query.setParameter("groupId", groupId);
        query.setParameter("userId", userId);
        query.setParameter("status", GroupMemberShipStatusEnum.pending);

        List<GroupMembership> results = query.getResultList();
        if (results.isEmpty()) {
            return List.of("No pending request found for this user in the group.");
        }

        GroupMembership membership = results.get(0);
        membership.setStatus(approve ? GroupMemberShipStatusEnum.approved : GroupMemberShipStatusEnum.rejected);

        return GroupMembershipDTO.fromEntity(membership);
    }

    public List<String> leaveGroup(Long userId, Long groupId) {
        TypedQuery<GroupMembership> query = em.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId",
                GroupMembership.class);
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);

        List<GroupMembership> memberships = query.getResultList();
        GroupMembership membership = memberships.isEmpty() ? null : memberships.get(0);

        List<String> errors = util.validateLeaveRequest(membership);
        if (!errors.isEmpty()) return errors;

        em.remove(membership);
        return new ArrayList<>();
    }
}
