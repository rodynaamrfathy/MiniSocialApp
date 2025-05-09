package Utils;

import models.Group;
import models.User;
import models.GroupMembership;
import models.GroupPost;
import enums.GroupMemberShipStatusEnum;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Set;

//
public class GroupUtils {
    

    public static boolean isGroupAdmin(Group group, User user) {
        return group.getGroupAdmins().contains(user);
    }
    
    
    public static boolean isGroupMember(EntityManager entityManager, Group group, User user) {
        TypedQuery<GroupMembership> query = entityManager.createQuery(
            "SELECT m FROM GroupMembership m WHERE m.group = :group AND m.user = :user",
            GroupMembership.class
        );
        query.setParameter("group", group);
        query.setParameter("user", user);
        return !query.getResultList().isEmpty();
    }
    

    public static GroupMembership createMembership(Group group, User user, String role, GroupMemberShipStatusEnum status) {
        GroupMembership membership = new GroupMembership();
        membership.setGroup(group);
        membership.setUser(user);
        membership.setRole(role);
        membership.setJoinedDate(new Date());
        membership.setStatus(status);
        return membership;
    }
      
    public static List<GroupMembership> getPendingRequests(EntityManager entityManager, Group group) {
        TypedQuery<GroupMembership> query = entityManager.createQuery(
            "SELECT m FROM GroupMembership m WHERE m.group = :group AND m.status = :status",
            GroupMembership.class
        );
        query.setParameter("group", group);
        query.setParameter("status", GroupMemberShipStatusEnum.pending);
        return query.getResultList();
    }
    
    
    public static GroupMembership promoteToAdmin(EntityManager entityManager, Group group, User user) {
        TypedQuery<GroupMembership> query = entityManager.createQuery(
            "SELECT m FROM GroupMembership m WHERE m.group = :group AND m.user = :user",
            GroupMembership.class
        );
        query.setParameter("group", group);
        query.setParameter("user", user);
        
        GroupMembership membership = query.getSingleResult();
        membership.setRole("ADMIN");
        
        //  add to admins array
        Set<User> admins = group.getGroupAdmins();
        admins.add(user);
        group.setGroupAdmins(admins);
        
        return membership;
    }
    
    
    public static void removeUserFromGroup(EntityManager entityManager, Group group, User user) {
        TypedQuery<GroupMembership> query = entityManager.createQuery(
            "SELECT m FROM GroupMembership m WHERE m.group = :group AND m.user = :user",
            GroupMembership.class
        );
        query.setParameter("group", group);
        query.setParameter("user", user);
        
        GroupMembership membership = query.getSingleResult();
        
        // remove from group admins if they are an admin
        if (membership.getRole().equals("ADMIN")) {
            Set<User> admins = group.getGroupAdmins();
            admins.remove(user);
            group.setGroupAdmins(admins);
        }
        
        entityManager.remove(membership);
    }
    
   
    public static void removePostFromGroup(EntityManager entityManager, Group group, Long postId) {
        TypedQuery<GroupPost> query = entityManager.createQuery(
            "SELECT p FROM GroupPost p WHERE p.group = :group AND p.postId = :postId",
            GroupPost.class
        );
        query.setParameter("group", group);
        query.setParameter("postId", postId);
        
        GroupPost post = query.getSingleResult();
        entityManager.remove(post);
    }
    
    
    public static void deleteGroup(EntityManager entityManager, Group group) {
        // remove all memberships
        TypedQuery<GroupMembership> membershipQuery = entityManager.createQuery(
            "SELECT m FROM GroupMembership m WHERE m.group = :group",
            GroupMembership.class
        );
        membershipQuery.setParameter("group", group);
        List<GroupMembership> memberships = membershipQuery.getResultList();
        for (GroupMembership membership : memberships) {
            entityManager.remove(membership);
        }
        
        // remove all posts
        TypedQuery<GroupPost> postQuery = entityManager.createQuery(
            "SELECT p FROM GroupPost p WHERE p.group = :group",
            GroupPost.class
        );
        postQuery.setParameter("group", group);
        List<GroupPost> posts = postQuery.getResultList();
        for (GroupPost post : posts) {
            entityManager.remove(post);
        }
        
        // remove the group
        entityManager.remove(group);
    }
} 