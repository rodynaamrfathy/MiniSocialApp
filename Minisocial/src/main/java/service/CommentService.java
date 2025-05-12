package service;

import Utils.CommentUtils;
import Utils.LikeUtils;
import models.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Service class for handling comment-related operations.
 * Provides methods for adding and retrieving comments for UserPosts and GroupPosts.
 * Also provides functionality to check group membership for users.
 */
@Stateless
public class CommentService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    /**
     * Finds a user by their ID.
     * 
     * @param userId the ID of the user.
     * @return the user object or null if not found.
     */
    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }

    /**
     * Finds a UserPost by its ID.
     * 
     * @param postId the ID of the UserPost.
     * @return the UserPost object or null if not found.
     */
    public UserPost findUserPostById(int postId) {
        return em.find(UserPost.class, postId);
    }

    /**
     * Finds a GroupPost by its ID.
     * 
     * @param postId the ID of the GroupPost.
     * @return the GroupPost object or null if not found.
     */
    public GroupPost findGroupPostById(int postId) {
        return em.find(GroupPost.class, postId);
    }

    /**
     * Retrieves all friendships for a given user.
     * 
     * @param user the user whose friendships are to be retrieved.
     * @return a list of Friendships associated with the user.
     */
    public List<Friendships> getAllFriendshipsForUser(User user) {
        TypedQuery<Friendships> query = em.createQuery(LikeUtils.GET_ALL_FRIENDS_QUERY, Friendships.class);
        query.setParameter("userId", user.getUserId());
        return query.getResultList();
    }

    /**
     * Retrieves comments for a specific UserPost.
     * 
     * @param postId the ID of the UserPost.
     * @return a list of comments associated with the given UserPost.
     */
    public List<Comment> getCommentsForUserPost(int postId) {
        TypedQuery<Comment> query = em.createQuery(CommentUtils.GET_COMMENTS_BY_USER_POST_QUERY, Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    /**
     * Retrieves comments for a specific GroupPost.
     * 
     * @param postId the ID of the GroupPost.
     * @param groupId the ID of the group to which the post belongs.
     * @return a list of comments associated with the given GroupPost.
     */
    public List<Comment> getCommentsForGroupPost(int postId, Long groupId) {
        TypedQuery<Comment> query = em.createQuery(CommentUtils.GET_COMMENTS_BY_GROUP_POST_QUERY, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    /**
     * Adds a comment to a UserPost.
     * 
     * @param userId the ID of the user adding the comment.
     * @param postId the ID of the UserPost.
     * @param content the content of the comment.
     * @return a list of error messages, or an empty list if successful.
     */
    public List<String> addCommentToUserPost(Long userId, int postId, String content) {
        // Find the user
        User user = findUserById(userId);
        if (user == null) {
            return List.of("User not found");
        }

        // Get all friendships for the user
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        // Ensure post exists
        UserPost post = findUserPostById(postId);
        if (post == null) {
            return List.of("UserPost not found");
        }

        // Check if the user is trying to comment on their own post
        if (post.getUser().getUserId().equals(userId)) {
            return List.of("You cannot comment on your own post.");
        }

        // Create and validate the comment
        Comment comment = new Comment();
        comment.setCreator(user);
        comment.setContent(content);
        comment.setTimestamp(new Date());
        comment.setPost(post);

        List<String> errors = CommentUtils.validateComment(comment, friendships);
        if (!errors.isEmpty()) {
            return errors;  // Return validation errors
        }

        // Update the comments count
        post.setCommentsCount(post.getCommentsCount() + 1);
        em.merge(post);
        em.persist(comment);

        return List.of();  // Success
    }

    /**
     * Adds a comment to a GroupPost.
     * 
     * @param userId the ID of the user adding the comment.
     * @param postId the ID of the GroupPost.
     * @param groupId the ID of the group to which the post belongs.
     * @param content the content of the comment.
     * @return a list of error messages, or an empty list if successful.
     */
    public List<String> addCommentToGroupPost(Long userId, int postId, Long groupId, String content) {
        User user = findUserById(userId);

        // Ensure group post exists
        GroupPost groupPost = findGroupPostById(postId);
        if (groupPost == null) return List.of("GroupPost not found");

        // Create and validate the comment
        Comment comment = new Comment();
        comment.setCreator(user);
        comment.setContent(content);
        comment.setTimestamp(new Date());
        comment.setGroupPost(groupPost);

        // Update comments count
        groupPost.setCommentsCount(groupPost.getCommentsCount() + 1);
        em.merge(groupPost);
        em.persist(comment);

        return List.of();  // Success
    }

    /**
     * Checks if a user is a member of a specific group.
     * 
     * @param user the user whose membership is to be checked.
     * @param groupId the ID of the group.
     * @return true if the user is a member of the group, false otherwise.
     */
    public boolean isUserMemberOfGroup(User user, Long groupId) {
        if (user == null || user.getUserId() == null || groupId == null) {
            return false;
        }

        TypedQuery<GroupMembership> query = em.createQuery(LikeUtils.CHECK_GROUP_MEMBERSHIP_QUERY, GroupMembership.class);
        query.setParameter("userId", user.getUserId());
        query.setParameter("groupId", groupId);

        List<GroupMembership> memberships = query.getResultList();
        return !memberships.isEmpty();
    }
}
