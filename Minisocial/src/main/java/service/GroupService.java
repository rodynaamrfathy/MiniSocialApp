package service;

import models.Group;
import models.User;
import models.GroupMembership;
import enums.GroupMemberShipStatusEnum;
import Utils.GroupUtils;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class GroupService {
    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
    
    public Group createGroup(String name, String description, boolean isOpen, User creator) {
    	
        if (name == null || name.trim().isEmpty()) {
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                .entity("Group name cannot be empty")
                .build()
            );
        }
        
        if (creator == null) {
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                .entity("Creator cannot be null")
                .build()
            );
        }
        
        Group group = new Group();
        group.setGroupName(name);
        group.setIsOpen(isOpen);
        
        // Initialize sets if null
        if (group.getGroupAdmins() == null) {
            group.setGroupAdmins(new HashSet<>());
        }
        
        // Add creator as admin
        group.getGroupAdmins().add(creator);
        
        // Create initial membership for creator
        GroupMembership membership = GroupUtils.createMembership(
            group, 
            creator, 
            "ADMIN", 
            GroupMemberShipStatusEnum.approved
        );
        
        entityManager.persist(group);
        entityManager.persist(membership);
        
        return group;
    }
    
    public GroupMembership requestToJoinGroup(Long groupId, Long userId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }
        
        if (GroupUtils.isGroupMember(entityManager, group, user)) {
            throw new WebApplicationException("User is already a member of this group", Response.Status.BAD_REQUEST);
        }
        
        GroupMemberShipStatusEnum status = group.getIsOpen() ? 
            GroupMemberShipStatusEnum.approved : 
            GroupMemberShipStatusEnum.pending;
        
        GroupMembership membership = GroupUtils.createMembership(
            group,
            user,
            "MEMBER",
            status
        );
        
        entityManager.persist(membership);
        return membership;
    }
    
    public GroupMembership handleMembershipRequest(Long membershipId, Long adminId, boolean approve) {
        GroupMembership membership = entityManager.find(GroupMembership.class, membershipId);
        if (membership == null) {
            throw new WebApplicationException("Membership request not found", Response.Status.NOT_FOUND);
        }
        
        User admin = entityManager.find(User.class, adminId);
        if (!GroupUtils.isGroupAdmin(membership.getGroup(), admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        if (approve) {
            membership.setStatus(GroupMemberShipStatusEnum.approved);
            entityManager.merge(membership);
            return membership;
        } else {
            entityManager.remove(membership);
            return null;
        }
    }
    
    public List<GroupMembership> getPendingRequests(Long groupId, User admin) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        if (!GroupUtils.isGroupAdmin(group, admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        return GroupUtils.getPendingRequests(entityManager, group);
    }
    
    public GroupMembership promoteToAdmin(Long groupId, Long userId, Long adminId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User admin = entityManager.find(User.class, adminId);
        if (!GroupUtils.isGroupAdmin(group, admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }
        
        GroupMembership membership = GroupUtils.promoteToAdmin(entityManager, group, user);
        entityManager.merge(membership);
        entityManager.merge(group);
        
        return membership;
    }
    
    public void removeUserFromGroup(Long groupId, Long userId, Long adminId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User admin = entityManager.find(User.class, adminId);
        if (!GroupUtils.isGroupAdmin(group, admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }
        
        GroupUtils.removeUserFromGroup(entityManager, group, user);
    }
    
    public void removePostFromGroup(Long groupId, Long postId, Long adminId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User admin = entityManager.find(User.class, adminId);
        if (!GroupUtils.isGroupAdmin(group, admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        GroupUtils.removePostFromGroup(entityManager, group, postId);
    }
    
    public void deleteGroup(Long groupId, Long adminId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User admin = entityManager.find(User.class, adminId);
        if (!GroupUtils.isGroupAdmin(group, admin)) {
            throw new WebApplicationException("User is not an admin of this group", Response.Status.FORBIDDEN);
        }
        
        GroupUtils.deleteGroup(entityManager, group);
    }
    
    public void leaveGroup(Long groupId, Long userId) {
        Group group = entityManager.find(Group.class, groupId);
        if (group == null) {
            throw new WebApplicationException("Group not found", Response.Status.NOT_FOUND);
        }
        
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
        }
        
        // checks if user is last member 
        if (GroupUtils.isGroupAdmin(group, user) && group.getGroupAdmins().size() == 1) {
            throw new WebApplicationException(
                "Cannot leave group as the last admin. Please promote another admin or delete the group.",
                Response.Status.BAD_REQUEST
            );
        }
        
        GroupUtils.removeUserFromGroup(entityManager, group, user);
    }
}