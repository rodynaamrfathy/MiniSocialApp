package service;

import Utils.GroupUtil;
import dtos.GroupDTO;
import enums.GroupMemberShipStatusEnum;
import models.Group;
import models.GroupMembership;
import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📦 GroupService
 *
 * This stateless EJB handles business logic related to Group management,
 * including group creation, membership assignment, and retrieval of approved group members.
 * 
 * 💡 Responsibilities:
 * - Validating admin user existence and group name uniqueness
 * - Persisting new groups and admin memberships
 * - Fetching approved group memberships
 *
 * ➡ Works closely with:
 * - GroupUtil for validation logic
 * - EntityManager for database operations
 */
@Stateless
public class GroupService {

    /**
     * 🗄️ EntityManager instance for interacting with the persistence context.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 📦 GroupCreationResult
     *
     * Helper class to encapsulate the result of group creation operation.
     * Can return either a successfully created GroupDTO or a list of validation errors.
     */
    public static class GroupCreationResult {
        /** The created GroupDTO if successful. */
        public GroupDTO groupDTO;

        /** The list of validation errors if creation failed. */
        public List<String> errors;

        /**
         * Constructor for successful group creation.
         *
         * @param groupDTO The created GroupDTO object.
         */
        public GroupCreationResult(GroupDTO groupDTO) {
            this.groupDTO = groupDTO;
            this.errors = null;
        }

        /**
         * Constructor for failed group creation with validation errors.
         *
         * @param errors The list of error messages.
         */
        public GroupCreationResult(List<String> errors) {
            this.errors = errors;
            this.groupDTO = null;
        }
    }

    /**
     * 🏗️ Creates a new group and assigns the given user as the admin.
     *
     * 🔍 Steps:
     * - Validates admin user existence.
     * - Checks for group name uniqueness.
     * - Persists the new group.
     * - Assigns the admin user as a group member with 'admin' role.
     *
     * @param groupDTO The DTO containing group details (name, description, isOpen).
     * @param adminId  The ID of the user to be set as group admin.
     * @return GroupCreationResult containing either created GroupDTO or validation errors.
     */
    @Transactional
    public GroupCreationResult createGroup(GroupDTO groupDTO, Long adminId) {
        List<String> validationErrors = new ArrayList<>();

        // Validate admin user existence
        User adminUser = GroupUtil.validateAdminUser(adminId, entityManager, validationErrors);

        // Validate group name uniqueness
        GroupUtil.validateGroupNameUniqueness(groupDTO.getGroupName(), entityManager, validationErrors);

        // Return errors if validation fails
        if (!validationErrors.isEmpty()) {
            return new GroupCreationResult(validationErrors);
        }

        // Create and persist group
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        group.setDescription(groupDTO.getDescription());
        group.setIsOpen(groupDTO.getIsOpen());
        entityManager.persist(group);

        // Add admin to the group as membership
        GroupMembership membership = new GroupMembership();
        membership.setUser(adminUser);
        membership.setGroup(group);
        membership.setRole("admin");
        membership.setStatus(GroupMemberShipStatusEnum.approved);
        membership.setJoinedDate(new Date());
        entityManager.persist(membership);

        return new GroupCreationResult(GroupDTO.fromGroup(group));
    }

    /**
     * 🔍 Retrieves all approved memberships for a given group.
     *
     * @param groupId The ID of the group.
     * @return List of approved GroupMembership entities.
     */
    public List<GroupMembership> getApprovedMembershipsByGroupId(Long groupId) {
        return entityManager.createQuery(
                "SELECT gm FROM GroupMembership gm WHERE gm.group.groupId = :groupId AND gm.status = :status",
                GroupMembership.class)
                .setParameter("groupId", groupId)
                .setParameter("status", GroupMemberShipStatusEnum.approved)
                .getResultList();
    }
    
    boolean isUserAdmin(Long userId, Long groupId) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(gm) FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId AND gm.role = :role",
            Long.class)
            .setParameter("userId", userId)
            .setParameter("groupId", groupId)
            .setParameter("role", "admin")
            .getSingleResult();

        return count > 0;
    }
    
   
    public Map<String, Object> promoteToAdmin(Long currentUserId, Long targetUserId, Long groupId) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (!isUserAdmin(currentUserId, groupId)) {
            errors.add("You are not authorized to promote members in this group.");
        }

        GroupMembership membership = entityManager.createQuery(
            "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :uid AND gm.group.groupId = :gid",
            GroupMembership.class)
            .setParameter("uid", targetUserId)
            .setParameter("gid", groupId)
            .getResultStream().findFirst().orElse(null);

        if (membership == null) {
            errors.add("Target user is not a member of the group.");
        }

        if (!errors.isEmpty()) {
            result.put("errors", errors);
            return result;
        }

        membership.setRole("admin");
        entityManager.merge(membership);
        result.put("message", "User promoted to admin successfully.");
        return result;
    }
    
    
    public Map<String, Object> deleteGroup(Long userId, Long groupId) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (!isUserAdmin(userId, groupId)) {
            errors.add("You are not authorized to delete this group.");
        }

        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            errors.add("Group not found.");
        }

        if (!errors.isEmpty()) {
            result.put("errors", errors);
            return result;
        }

        // Remove memberships first due to foreign key constraints
        entityManager.createQuery("DELETE FROM GroupMembership gm WHERE gm.group.groupId = :gid")
            .setParameter("gid", groupId).executeUpdate();

        entityManager.remove(group);
        result.put("message", "Group deleted successfully.");
        return result;
    }
    
   
    public Map<String, Object> removeUserFromGroup(Long adminId, Long targetUserId, Long groupId) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (!isUserAdmin(adminId, groupId)) {
            errors.add("You are not authorized to remove members from this group.");
        }

        GroupMembership membership = entityManager.createQuery(
            "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :uid AND gm.group.groupId = :gid",
            GroupMembership.class)
            .setParameter("uid", targetUserId)
            .setParameter("gid", groupId)
            .getResultStream().findFirst().orElse(null);

        if (membership == null) {
            errors.add("Target user is not a member of the group.");
        }

        if (!errors.isEmpty()) {
            result.put("errors", errors);
            return result;
        }

        entityManager.remove(membership);
        result.put("message", "User removed from group.");
        return result;
    }




}
