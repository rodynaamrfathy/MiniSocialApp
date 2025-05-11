package service;

import models.Group;
import models.User;
import models.GroupDTO;
import models.GroupMembership;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;

import enums.GroupMemberShipStatusEnum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 📦 GroupService handles the business logic for creating a group and assigning an admin.
 * 
 * 🚀 Responsibilities:
 * - Creates new groups
 * - Ensures group name uniqueness
 * - Assigns group admin
 */
@Stateless
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public GroupDTO createGroup(GroupDTO groupDTO, Long adminId) {
        List<String> validationErrors = new ArrayList<>();
        
        // Find the admin user
        User adminUser = entityManager.find(User.class, adminId);
        if (adminUser == null) {
            validationErrors.add("Admin user not found.");
        }

        // Check if a group with the same name already exists
        boolean groupExists = entityManager
            .createQuery("SELECT COUNT(g) FROM Group g WHERE g.groupName = :name", Long.class)
            .setParameter("name", groupDTO.getGroupName())
            .getSingleResult() > 0;

        if (groupExists) {
            validationErrors.add("Group with the same name already exists.");
        }

        // If validation errors exist, throw WebApplicationException with error messages
        if (!validationErrors.isEmpty()) {
            throw new WebApplicationException(buildErrorResponse(validationErrors), 400);
        }

        // Create and persist the new group
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        group.setDescription(groupDTO.getDescription());
        group.setIsOpen(groupDTO.getIsOpen());
        entityManager.persist(group);
        System.out.println("Group created: " + group.getGroupName()); // Debug print

        // Call the method to handle the GroupMembership persistence
        persistGroupMembership(adminId, group);  // Call this method for the GroupMembership persistence

        return GroupDTO.fromGroup(group);
    }

    // Separate method to persist the GroupMembership
    private void persistGroupMembership(Long adminId, Group group) {
        // Retrieve the admin user
        User adminUser = entityManager.find(User.class, adminId);
        if (adminUser == null) {
            throw new WebApplicationException("Admin user not found.", 400);
        }

        // Insert the admin as a GroupMembership entry
        GroupMembership groupMembership = new GroupMembership();
        groupMembership.setUser(adminUser);      // Set the admin user
        groupMembership.setGroup(group);         // Set the newly created group
        groupMembership.setRole("admin");        // Role is 'admin' for the admin user
        groupMembership.setStatus(GroupMemberShipStatusEnum.approved);  // Assume status is active
        groupMembership.setJoinedDate(new Date()); // Set current date as the join date

        // Debug print
        System.out.println("Inserting GroupMembership for admin user: " + adminUser.getUserName() 
                            + " in group: " + group.getGroupName());

        // Persist the new group membership
        entityManager.persist(groupMembership);

        // Debug print
        System.out.println("GroupMembership inserted for user: " + adminUser.getUserName() 
                            + " in group: " + group.getGroupName());
    }

    private String buildErrorResponse(List<String> validationErrors) {
        StringBuilder errorMessage = new StringBuilder("Errors: ");
        for (String error : validationErrors) {
            errorMessage.append("\n").append(error);
        }
        return errorMessage.toString();
    }

    public List<GroupMembership> getGroupMembershipsByGroupId(Long groupId) {
        // Query to find all group memberships for a specific group
        return entityManager.createQuery("SELECT gm FROM GroupMembership gm WHERE gm.group.id = :groupId", GroupMembership.class)
                            .setParameter("groupId", groupId)
                            .getResultList();
    }


}


