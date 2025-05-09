package service;

import models.User;
import models.UserPost;
import models.UserPostDTO;
import models.Post;
import Utils.PostFactory;
import Utils.PostUtil;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PostService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

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
            post.setUser(user);  // Make sure the User is set in UserPost before creation
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

    public String editPost(Long userId, int postId, String newContent, String newImageUrl) {
        User user = em.find(User.class, userId);
        
        if (user == null) {
            return "User with ID: " + userId + "not Found.";
        }
        
        UserPost post = em.find(UserPost.class, postId);
        if (post == null) {
            return "Post with ID " + postId + " not found.";
        }

        // Use PostUtil to check if the user can edit the post
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

    public String deletePost(Long userId, int postId) {
        // Find the user and check if the user exists
        User user = em.find(User.class, userId);
        if (user == null) {
            return "User with ID " + userId + " not found.";
        }

        // Find the post by its ID
        UserPost post = em.find(UserPost.class, postId);
        if (post == null) {
            return "Post with ID " + postId + " not found.";
        }

        // Check if the post belongs to the user trying to delete it
        if (!PostUtil.canEditAndDeletePost(user, post)) {
            return "User does not have permission to delete this post.";
        }

        // Try to delete the post
        try {
            em.remove(post);
        } catch (PersistenceException e) {
            return "Error while deleting the post.";
        }

        return "Post deleted successfully";
    }
}