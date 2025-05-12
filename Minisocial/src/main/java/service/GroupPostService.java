package service;

import models.User;
import models.GroupPost;
import models.GroupPostDTO;
import models.Group;
import Utils.PostFactory;
import Utils.GroupPostUtil;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class GroupPostService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    @Inject
    private GroupService groupService;

    
    // Method to create a GroupPost for a user within a group
    public String createGroupPost(Long userId, GroupPost post, Long groupId) {

        // Find the user and group by their respective IDs
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        // Validate if user and group exist
        if (user == null) {
            return "User with ID " + userId + " does not exist.";
        }
        
        if (group == null) {
            return "Group with ID " + groupId + " does not exist.";
        }

        // Validate the group post fields using GroupPostUtil
        List<String> validationErrors = GroupPostUtil.validateGroupPost(post.getContent(), post.getImageUrl(), user, group);
        if (!validationErrors.isEmpty()) {
            return "GroupPost validation failed: " + String.join(", ", validationErrors);
        }

        // Use the PostFactory to create a new GroupPost
        GroupPost createdPost = null;
        try {
            createdPost = (GroupPost) PostFactory.createPost(
                "group", user, post.getContent(), post.getImageUrl(), group
            );
        } catch (IllegalArgumentException e) {
            return "Error during group post creation: " + e.getMessage();
        }

        // Persist the newly created post in the database
        try {
            em.persist(createdPost);
        } catch (Exception e) {
            return "Error while persisting the group post.";
        }

        return "GroupPost created successfully";
    }

    // Method to retrieve all GroupPosts by group
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

    // Method to edit an existing group post
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

        // Validate the new group post fields
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

    // Method to delete a group post
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

        try {
            em.remove(post);
        } catch (Exception e) {
            return "Error while deleting the group post.";
        }

        return "GroupPost deleted successfully";
    }

    
    
}
