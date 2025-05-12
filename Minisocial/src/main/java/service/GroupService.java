package service;

import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupDTO;
import models.GroupMembership;
import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    // Class to return either result or errors
    public static class GroupCreationResult {
        public GroupDTO groupDTO;
        public List<String> errors;

        public GroupCreationResult(GroupDTO groupDTO) {
            this.groupDTO = groupDTO;
            this.errors = null;
        }

        public GroupCreationResult(List<String> errors) {
            this.errors = errors;
            this.groupDTO = null;
        }
    }

    @Transactional
    public GroupCreationResult createGroup(GroupDTO groupDTO, Long adminId) {
        List<String> validationErrors = new ArrayList<>();

        // Check if admin user exists
        User adminUser = entityManager.find(User.class, adminId);
        if (adminUser == null) {
            validationErrors.add("Admin user not found.");
        }

        // Check if group with the same name exists
        boolean groupExists = entityManager
                .createQuery("SELECT COUNT(g) FROM Group g WHERE g.groupName = :name", Long.class)
                .setParameter("name", groupDTO.getGroupName())
                .getSingleResult() > 0;

        if (groupExists) {
            validationErrors.add("Group with the same name already exists.");
        }

        if (!validationErrors.isEmpty()) {
            return new GroupCreationResult(validationErrors);
        }

        // Create and persist group
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        group.setDescription(groupDTO.getDescription());
        group.setIsOpen(groupDTO.getIsOpen());
        entityManager.persist(group);

        // Add admin to the group
        GroupMembership membership = new GroupMembership();
        membership.setUser(adminUser);
        membership.setGroup(group);
        membership.setRole("admin");
        membership.setStatus(GroupMemberShipStatusEnum.approved);
        membership.setJoinedDate(new Date());
        entityManager.persist(membership);

        return new GroupCreationResult(GroupDTO.fromGroup(group));
    }

    public List<GroupMembership> getApprovedMembershipsByGroupId(Long groupId) {
        return entityManager.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.status = :status",
                GroupMembership.class)
                .setParameter("groupId", groupId)
                .setParameter("status", GroupMemberShipStatusEnum.approved)
                .getResultList();
    }
}
