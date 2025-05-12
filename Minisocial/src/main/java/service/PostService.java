package service;

import models.User;
import models.UserPost;
import models.Post;
import Utils.PostFactory;
import Utils.PostUtil;
import dtos.UserPostDTO;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * The PostService class provides functionality for managing User Posts.
 * It includes operations such as creating, retrieving, editing, and deleting posts for a specific user.
 */
@Stateless
public class PostService {

    /**
     * EntityManager to interact with the persistence context.
     * Used to perform database operations related to posts and users.
     */
    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    /**
     * Creates a new post for the user.
     * 
     * @param userId The ID of the user creating the post.
     * @param post The UserPost object containing the post's content and image URL.
     * @return A status message indicating the result of the post creation.
     */
    public String createPostForUser(Long userId, UserPost post) {

        
        User user = em.find(User.class, userId);
        if (user == null) {
            return "User with ID " + userId + " does not exist.";
        }

        
        List<String> validationErrors = PostUtil.validatePost(post.getContent(), post.getImageUrl(), user, null, "user");
        if (!validationErrors.isEmpty()) {
            return "Post validation failed: " + String.join(", ", validationErrors);
        }

        Post createdPost = null;
        try {
            
            post.setUser(user);
            createdPost = PostFactory.createPost(
                "user", user, post.getContent(), post.getImageUrl(), null
            );
        } catch (Exception e) {
            return "Error during post creation.";
        }

     
        if (!(createdPost instanceof UserPost)) {
            return "Factory did not return a UserPost instance";
        }

        try {
      
            em.persist(createdPost);
        } catch (PersistenceException e) {
            return "Error while persisting the post.";
        }

        return "Post created successfully";  
    }

    /**
     * Retrieves all posts made by a specific user.
     * 
     * @param userId The ID of the user whose posts are to be retrieved.
     * @return A list of UserPostDTO objects containing the post details.
     */
    public List<UserPostDTO> getAllPostsByUser(Long userId) {


        TypedQuery<UserPost> query = em.createQuery(PostUtil.GET_POSTS_BY_USER_QUERY, UserPost.class);
        query.setParameter("userId", userId);
        
        try {
        
            List<UserPost> posts = query.getResultList();
            return UserPostDTO.fromUserPostList(posts);  
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the timeline for a user, including posts made by the user and their friends.
     * 
     * @param userId The ID of the user whose timeline is to be retrieved.
     * @return A list of UserPostDTO objects representing the user's timeline.
     */
    public List<UserPostDTO> getUserTimeline(long userId) {
       
        User user = em.find(User.class, userId );
        if (user == null) {
            return null;
        }

      
        String queryStr = "SELECT p FROM UserPost p WHERE p.user.userId = :userId " +
                "OR p.user IN (SELECT f.friend FROM Friendships f WHERE f.user.userId = :userId)";
        
        TypedQuery<UserPost> query = em.createQuery(queryStr, UserPost.class);
        query.setParameter("userId", userId);

        try {
            
            List<UserPost> posts = query.getResultList();
            return UserPostDTO.fromUserPostList(posts);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Edits an existing post made by the user.
     * 
     * @param userId The ID of the user editing the post.
     * @param postId The ID of the post being edited.
     * @param newContent The new content for the post.
     * @param newImageUrl The new image URL for the post.
     * @return A status message indicating the result of the edit operation.
     */
    public String editPost(Long userId, int postId, String newContent, String newImageUrl) {
        
        User user = em.find(User.class, userId);
        
        if (user == null) {
            return "User with ID: " + userId + "not Found.";
        }
        
     
        UserPost post = em.find(UserPost.class, postId);
        if (post == null) {
            return "Post with ID " + postId + " not found.";
        }

       
        if (!PostUtil.canEditAndDeletePost(user, post)) {
            return "User does not have permission to edit this post.";
        }
        
     
        List<String> validationErrors = PostUtil.validatePost(newContent, newImageUrl, user, null, newImageUrl);
        if (!validationErrors.isEmpty()) {
            return "Post validation failed: " + String.join(", ", validationErrors);
        }

        try {
      
            post.setContent(newContent);
            post.setImageUrl(newImageUrl);
            em.merge(post);
        } catch (PersistenceException e) {
            return "Error while updating the post.";
        }

        return "Post updated successfully";
    }

    /**
     * Deletes an existing post made by the user.
     * 
     * @param userId The ID of the user deleting the post.
     * @param postId The ID of the post to be deleted.
     * @return A status message indicating the result of the delete operation.
     */
    public String deletePost(Long userId, int postId) {
   
        User user = em.find(User.class, userId);
        if (user == null) {
            return "User with ID " + userId + " not found.";
        }

      
        UserPost post = em.find(UserPost.class, postId);
        if (post == null) {
            return "Post with ID " + postId + " not found.";
        }

    
        if (!PostUtil.canEditAndDeletePost(user, post)) {
            return "User does not have permission to delete this post.";
        }

        try {
      
            em.remove(post);
        } catch (PersistenceException e) {
            return "Error while deleting the post.";
        }

        return "Post deleted successfully";
    }
}
