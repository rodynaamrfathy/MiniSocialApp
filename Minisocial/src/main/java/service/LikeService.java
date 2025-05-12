package service;

import Utils.LikeUtils;
import models.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Service class responsible for handling like-related functionality,
 * including liking user and group posts, checking for duplicate likes,
 * and fetching likes for posts.
 */
@Stateless
public class LikeService {

    // Injecting the EntityManager for database operations
    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    // -------------------- User & Post Finders --------------------
    
    /**
     * Retrieves a User object by its ID.
     *
     * @param userId the ID of the user
     * @return the User object or null if not found
     */
    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }

    /**
     * Retrieves a UserPost object by its ID.
     *
     * @param postId the ID of the post
     * @return the UserPost object or null if not found
     */
    public UserPost findUserPostById(int postId) {
        return em.find(UserPost.class, postId);
    }

    /**
     * Retrieves a GroupPost object by its ID.
     *
     * @param postId the ID of the group post
     * @return the GroupPost object or null if not found
     */
    public GroupPost findGroupPostById(int postId) {
        return em.find(GroupPost.class, postId);
    }

    // -------------------- Friendships --------------------

    /**
     * Fetches all friendships associated with a specific user.
     *
     * @param user the User whose friendships are to be fetched
     * @return a list of Friendships for the user
     */
    public List<Friendships> getAllFriendshipsForUser(User user) {
        TypedQuery<Friendships> query = em.createQuery(LikeUtils.GET_ALL_FRIENDS_QUERY, Friendships.class);
        query.setParameter("userId", user.getUserId());
        return query.getResultList();
    }

    // -------------------- Get Likes for UserPost --------------------

    /**
     * Fetches all likes for a specific UserPost.
     *
     * @param postId the ID of the user post
     * @return a list of likes for the post
     * @throws IllegalArgumentException if the post is not found
     */
    public List<Like> getLikesForUserPost(int postId) {
        UserPost post = findUserPostById(postId);
        if (post == null) {
            throw new IllegalArgumentException("UserPost not found for ID: " + postId);
        }

        TypedQuery<Like> query = em.createQuery(LikeUtils.GET_LIKES_BY_USER_POST_QUERY, Like.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    // -------------------- Get Likes for GroupPost --------------------

    /**
     * Fetches all likes for a specific GroupPost.
     *
     * @param postId the ID of the group post
     * @param groupId the ID of the group
     * @return a list of likes for the group post
     * @throws IllegalArgumentException if the post is not found
     */
    public List<Like> getLikesForGroupPost(int postId, Long groupId) {
        GroupPost groupPost = findGroupPostById(postId);
        if (groupPost == null) {
            throw new IllegalArgumentException("GroupPost not found for ID: " + postId);
        }

        TypedQuery<Like> query = em.createQuery(LikeUtils.GET_LIKES_BY_GROUP_POST_QUERY, Like.class);
        query.setParameter("postId", postId);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    // -------------------- Like a UserPost --------------------

    /**
     * Likes a specific UserPost by a user.
     * Validates friendship and prevents duplicate likes.
     *
     * @param userId the ID of the user liking the post
     * @param postId the ID of the post being liked
     * @return a list of error messages if validation fails, otherwise an empty list
     */
    public List<String> likeUserPost(Long userId, int postId) {
        User user = findUserById(userId);
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        UserPost post = findUserPostById(postId);
        if (post == null) return List.of("UserPost not found");

        // Validate Like
        List<String> errors = LikeUtils.validateUserPostLike(user, post, friendships);
        if (!errors.isEmpty()) return errors;

        // Prevent duplicate like
        TypedQuery<Like> likeQuery = em.createQuery(LikeUtils.GET_LIKES_BY_USER_POST_QUERY, Like.class);
        likeQuery.setParameter("postId", postId);
        List<Like> existingLikes = likeQuery.getResultList();

        for (Like like : existingLikes) {
            if (like.getUser().getUserId().equals(userId)) {
                errors.add("You can only like a post once.");
                break;
            }
        }

        if (!errors.isEmpty()) return errors;

        // Create Like
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        like.setTimestamp(new Date());

        // Update like count
        post.setLikesCount(post.getLikesCount() + 1);
        em.merge(post);
        em.persist(like);

        return List.of(); // Success
    }

    // -------------------- Like a GroupPost --------------------

    /**
     * Likes a specific GroupPost by a user.
     * Validates group membership, prevents liking own post, and avoids duplicate likes.
     *
     * @param userId the ID of the user liking the post
     * @param postId the ID of the post being liked
     * @param groupId the ID of the group containing the post
     * @return a list of error messages if validation fails, otherwise an empty list
     */
    public List<String> likeGroupPost(Long userId, int postId, Long groupId) {
        User user = findUserById(userId);
        if (user == null) return List.of("User not found");

        GroupPost groupPost = findGroupPostById(postId);
        if (groupPost == null) return List.of("GroupPost not found");

        // Check group membership only
        boolean isGroupMember = isUserMemberOfGroup(user, groupId);
        if (!isGroupMember) {
            return List.of("You must be a member of the group to like this post.");
        }

        // Prevent liking own post
        if (user.getUserId().equals(groupPost.getUser().getUserId())) {
            return List.of("You cannot like your own group post.");
        }

        // Prevent duplicate likes
        TypedQuery<Like> likeQuery = em.createQuery(LikeUtils.GET_LIKES_BY_GROUP_POST_QUERY, Like.class);
        likeQuery.setParameter("postId", postId);
        likeQuery.setParameter("groupId", groupId);
        List<Like> existingLikes = likeQuery.getResultList();

        for (Like like : existingLikes) {
            if (like.getUser().getUserId().equals(userId)) {
                return List.of("You can only like a post once.");
            }
        }

        // Create Like
        Like like = new Like();
        like.setUser(user);
        like.setGroupPost(groupPost);
        like.setTimestamp(new Date());

        // Update like count
        groupPost.setLikesCount(groupPost.getLikesCount() + 1);
        em.merge(groupPost);
        em.persist(like);

        return List.of(); // Success
    }

    // -------------------- Group Membership Check --------------------

    /**
     * Checks if a user is a member of a specific group.
     *
     * @param user the user to check
     * @param groupId the ID of the group
     * @return true if the user is a member, false otherwise
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
