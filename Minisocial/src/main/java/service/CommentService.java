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

@Stateless
public class CommentService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }

    public UserPost findUserPostById(int postId) {
        return em.find(UserPost.class, postId);
    }

    public GroupPost findGroupPostById(int postId) {
        return em.find(GroupPost.class, postId);
    }

    public List<Friendships> getAllFriendshipsForUser(User user) {
        TypedQuery<Friendships> query = em.createQuery(LikeUtils.GET_ALL_FRIENDS_QUERY, Friendships.class);
        query.setParameter("userId", user.getUserId());
        return query.getResultList();
    }

    // Get comments for UserPost
    public List<Comment> getCommentsForUserPost(int postId) {
        TypedQuery<Comment> query = em.createQuery(CommentUtils.GET_COMMENTS_BY_USER_POST_QUERY, Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    // Get comments for GroupPost
    public List<Comment> getCommentsForGroupPost(int postId, Long groupId) {
        TypedQuery<Comment> query = em.createQuery(CommentUtils.GET_COMMENTS_BY_GROUP_POST_QUERY, Comment.class);
        query.setParameter("postId", postId);
        query.setParameter("groupId", groupId);
        return query.getResultList();
    }

    // Add comment to UserPost
    public List<String> addCommentToUserPost(Long userId, int postId, String content) {
        User user = findUserById(userId);
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        // Ensure post exists
        UserPost post = findUserPostById(postId);
        if (post == null) return List.of("UserPost not found");

        // Create comment
        Comment comment = new Comment();
        comment.setCreator(user);
        comment.setContent(content);
        comment.setTimestamp(new Date());
        comment.setPost(post);

        // Validate comment
        List<String> errors = CommentUtils.validateComment(comment, friendships);
        if (!errors.isEmpty()) return errors;

        // Update comments count
        post.setCommentsCount(post.getCommentsCount() + 1);
        em.merge(post);
        em.persist(comment);

        return List.of();  // Success
    }

    // Add comment to GroupPost
    public List<String> addCommentToGroupPost(Long userId, int postId, Long groupId, String content) {
        User user = findUserById(userId);
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        // Ensure group post exists
        GroupPost groupPost = findGroupPostById(postId);
        if (groupPost == null) return List.of("GroupPost not found");

        // Create comment
        Comment comment = new Comment();
        comment.setCreator(user);
        comment.setContent(content);
        comment.setTimestamp(new Date());
        comment.setGroupPost(groupPost);

        // Validate comment
        List<String> errors = CommentUtils.validateGroupComment(comment, friendships, groupId);
        if (!errors.isEmpty()) return errors;

        // Update comments count
        groupPost.setCommentsCount(groupPost.getCommentsCount() + 1);
        em.merge(groupPost);
        em.persist(comment);

        return List.of();  // Success
    }
}
