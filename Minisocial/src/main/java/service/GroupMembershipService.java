package service;

import Utils.GroupMembershipUtil;
import dtos.GroupMembershipDTO;
import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupMembership;
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

/**
 * Service class for handling group membership-related operations such as requesting to join a group, responding to requests, 
 * adding initial memberships (admins), and handling membership leave requests.
 */
@Stateless
public class GroupMembershipService {

    // 🔹 Persistence Context for managing database interactions
    @PersistenceContext
    private EntityManager em;

    // 🔹 Injected utility for validating group membership requests
    @Inject
    private GroupMembershipUtil util;

    /**
     * Handles a user's request to join a group.
     * 
     * @param userId The ID of the user requesting to join the group.
     * @param groupId The ID of the group to join.
     * @return An object containing either a list of errors or a GroupMembershipDTO representing the membership.
     */
    public Object requestToJoinGroup(Long userId, Long groupId) {
       
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

     
        boolean alreadyMember = !em.createQuery(GroupMembershipUtil.FIND_MEMBERSHIP, GroupMembership.class)
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

    /**
     * Adds the initial membership for a group (sets a user as an admin).
     * 
     * @param userId The ID of the user to be made an admin.
     * @param group The group in which the user is being added as an admin.
     */
    public void addInitialMembership(Long userId, Group group) {
        // Fetch the user from the database
        User user = em.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid User ID");
        }

       
        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setRole("admin");
        membership.setStatus(GroupMemberShipStatusEnum.approved);
        membership.setJoinedDate(new Date());

        em.persist(membership);
    }

    /**
     * Retrieves all pending requests for a specific group.
     * 
     * @param groupId The ID of the group for which pending membership requests are to be retrieved.
     * @return A list of pending membership requests or an error message if no requests exist.
     */
    public Object getPendingRequestsForGroup(Long groupId) {
        // Fetch the group from the database
        Group group = em.find(Group.class, groupId);
        if (group == null) {
            return List.of("Group with ID " + groupId + " does not exist.");
        }

      
        TypedQuery<GroupMembership> query = em.createQuery(
                GroupMembershipUtil.FIND_PENDING_MEMBERSHIPS_FOR_GROUP, GroupMembership.class);
        query.setParameter("groupId", groupId);
        query.setParameter("status", GroupMemberShipStatusEnum.pending);

        List<GroupMembership> results = query.getResultList();
        if (results.isEmpty()) {
            return List.of("There are no pending requests for this group.");
        }

        return results.stream().map(GroupMembershipDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Responds to a pending membership request for a user in a group, either approving or rejecting it.
     * 
     * @param groupId The ID of the group for which the request is to be responded to.
     * @param userId The ID of the user whose request is being responded to.
     * @param approve A flag indicating whether the request is being approved or rejected.
     * @return A GroupMembershipDTO representing the updated membership after the response.
     */
    public Object respondToRequest(Long groupId, Long userId, boolean approve) {
    
        TypedQuery<GroupMembership> query = em.createQuery(
                GroupMembershipUtil.FIND_PENDING_REQUEST_FOR_USER, GroupMembership.class);
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

    /**
     * Allows a user to leave a group by removing their membership.
     * 
     * @param userId The ID of the user who wishes to leave the group.
     * @param groupId The ID of the group the user is leaving.
     * @return A list of error messages if the leave request is invalid, or an empty list if the operation is successful.
     */
    public List<String> leaveGroup(Long userId, Long groupId) {
     
        TypedQuery<GroupMembership> query = em.createQuery(
                GroupMembershipUtil.FIND_MEMBERSHIP, GroupMembership.class);
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
