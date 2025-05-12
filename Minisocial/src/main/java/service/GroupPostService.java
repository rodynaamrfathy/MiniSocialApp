package service;

import models.User;
import models.GroupPost;
import models.Group;
import models.GroupMembership;
import Utils.PostFactory;
import dtos.GroupPostDTO;
import enums.GroupMemberShipStatusEnum;
import Utils.GroupPostUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * GroupPostService is a state less EJB responsible for managing group posts in a social platform.
 * It provides methods to create, retrieve, edit, and delete group posts for a user within a group.
 * It also includes validation for posts and ensures the user has the necessary permissions to perform actions.
 */
@Stateless
public class GroupPostService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    @Inject
    private GroupService groupService;

    
    // Method to create a GroupPost for a user within a group
    /**
     * Creates a new group post for a user within a specific group.
     * This method validates the existence of the user and group, validates the post content,
     * and uses the PostFactory to create a GroupPost, which is then persisted in the database.
     * 
     * @param userId The ID of the user creating the post.
     * @param post The GroupPost object containing the content and image URL.
     * @param groupId The ID of the group where the post is being created.
     * @return A message indicating whether the post creation was successful or if any errors occurred.
     */
    public String createGroupPost(Long userId, GroupPost post, Long groupId) {

        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

      
        if (user == null) {
            return "User with ID " + userId + " does not exist.";
        }
        
        if (group == null) {
            return "Group with ID " + groupId + " does not exist.";
        }

       
        List<String> validationErrors = GroupPostUtil.validateGroupPost(post.getContent(), post.getImageUrl(), user, group);
        if (!validationErrors.isEmpty()) {
            return "GroupPost validation failed: " + String.join(", ", validationErrors);
        }

  
        GroupPost createdPost = null;
        try {
            createdPost = (GroupPost) PostFactory.createPost(
                "group", user, post.getContent(), post.getImageUrl(), group
            );
        } catch (IllegalArgumentException e) {
            return "Error during group post creation: " + e.getMessage();
        }

    
        try {
            em.persist(createdPost);
        } catch (Exception e) {
            return "Error while persisting the group post.";
        }

        return "GroupPost created successfully";
    }

    /**
     * Retrieves all group posts associated with a specific group.
     * 
     * @param groupId The ID of the group whose posts are being retrieved.
     * @return A list of GroupPostDTO objects representing the posts in the group.
     */
    public List<GroupPostDTO> getAllGroupPosts(Long groupId) {

        TypedQuery<GroupPost> query = em.createQuery(GroupPostUtil.GET_GROUP_POSTS_QUERY, GroupPost.class);
        query.setParameter("groupId", groupId);
        
        try {
            List<GroupPost> posts = query.getResultList();
            return GroupPostDTO.fromGroupPostList(posts);  
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Edits an existing group post's content and/or image URL.
     * This method ensures the user has permission to edit the post and validates the new content.
     * 
     * @param userId The ID of the user requesting the edit.
     * @param postId The ID of the post being edited.
     * @param newContent The new content to update the post with.
     * @param newImageUrl The new image URL to update the post with.
     * @return A message indicating whether the post was successfully updated or if any errors occurred.
     */
    public String editGroupPost(Long userId, int postId, String newContent, String newImageUrl) {
        User user = em.find(User.class, userId);
        
        if (user == null) {
            return "User with ID: " + userId + " not found.";
        }

        GroupPost post = em.find(GroupPost.class, postId);
        if (post == null) {
            return "GroupPost with ID " + postId + " not found.";
        }

        // Check if the user can edit the post
        if (!GroupPostUtil.canEditAndDeletePost(user, post)) {
            return "User does not have permission to edit this group post.";
        }

        
        List<String> validationErrors = GroupPostUtil.validateGroupPost(newContent, newImageUrl, user, post.getGroup());
        if (!validationErrors.isEmpty()) {
            return "GroupPost validation failed: " + String.join(", ", validationErrors);
        }

        try {
            post.setContent(newContent);
            post.setImageUrl(newImageUrl);
            em.merge(post);
        } catch (Exception e) {
            return "Error while updating the group post.";
        }

        return "GroupPost updated successfully";
    }

    /**
     * Deletes an existing group post.
     * This method ensures that the user has permission to delete the post and removes the post from the database.
     * 
     * @param userId The ID of the user requesting the deletion.
     * @param postId The ID of the post being deleted.
     * @return A message indicating whether the post was successfully deleted or if any errors occurred.
     */
    public String deleteGroupPost(Long userId, int postId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return "User with ID " + userId + " not found.";
        }

        GroupPost post = em.find(GroupPost.class, postId);
        if (post == null) {
            return "GroupPost with ID " + postId + " not found.";
        }

        Long groupId = post.getGroup().getGroupId();

        boolean isCreator = post.getUser().getUserId().equals(userId);
        boolean isAdmin = groupService.isUserAdmin(userId, groupId);

        if (!isCreator && !isAdmin) {
            return "You do not have permission to delete this group post. Only the post creator or a group admin can delete it.";
        }

      
        if (!GroupPostUtil.canEditAndDeletePost(user, post)) {
            return "User does not have permission to delete this group post.";
        }

       
        try {
            em.remove(post);
        } catch (Exception e) {
            return "Error while deleting the group post.";
        }

        return "GroupPost deleted successfully";
    }

    
    /**
     * Checks if a user is a member of a specific group.
     * 
     * @param userId The ID of the user to check.
     * @param groupId The ID of the group to check membership for.
     * @return true if the user is a member of the group, false otherwise.
     */
    public boolean isUserMemberOfGroup(Long userId, Long groupId) {
        // Query to find the membership record for the user and group
        TypedQuery<GroupMembership> query = em.createQuery(
            "SELECT gm FROM GroupMembership gm WHERE gm.user.id = :userId AND gm.group.id = :groupId", 
            GroupMembership.class
        );
        query.setParameter("userId", userId);
        query.setParameter("groupId", groupId);

        List<GroupMembership> memberships = query.getResultList();

        // If the membership exists and the status is active, return true
        for (GroupMembership membership : memberships) {
            if (membership.getStatus() == GroupMemberShipStatusEnum.approved) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Finds a Group by its ID.
     * @param groupId The ID of the group.
     * @return Group entity if found, null otherwise.
     */
    public Group findGroupById(Long groupId) {
        return em.find(Group.class, groupId);
    }


}
