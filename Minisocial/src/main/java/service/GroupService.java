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
import java.util.List;

/**
 * GroupService
 *
 * This stateless EJB handles business logic related to Group management,
 * including group creation, membership assignment, and retrieval of approved group members.
 * 
 * Responsibilities:
 * - Validating admin user existence and group name uniqueness
 * - Persisting new groups and admin memberships
 * - Fetching approved group memberships
 *
 * Works closely with:
 * - GroupUtil for validation logic
 * - EntityManager for database operations
 */
@Stateless
public class GroupService {

    /**
     * EntityManager instance for interacting with the persistence context.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * GroupCreationResult
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
     * Creates a new group and assigns the given user as the admin.
     *
     * Steps:
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
     * Retrieves all approved memberships for a given group.
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
}
