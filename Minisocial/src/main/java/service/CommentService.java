package service;

import Utils.CommentUtils;
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

    public UserPost findPostById(int postId) {
        return em.find(UserPost.class, postId);
    }

    public List<Friendships> getAllFriendshipsForUser(User user) {
        String jpql = "SELECT f FROM Friendships f WHERE f.user.userId = :userId OR f.friend.userId = :userId";
        TypedQuery<Friendships> query = em.createQuery(jpql, Friendships.class);
        query.setParameter("userId", user.getUserId());
        return query.getResultList();
    }

    public List<Comment> getCommentsForPost(int postId) {
        TypedQuery<Comment> query = em.createQuery(CommentUtils.GET_COMMENTS_BY_POST_QUERY, Comment.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    public List<String> addComment(Long userId, int postId, String content) {
        User user = findUserById(userId);
        UserPost post = findPostById(postId);
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        Comment comment = new Comment();
        comment.setCreator(user);
        comment.setPost(post);
        comment.setContent(content);
        comment.setTimestamp(new Date());

        List<String> errors = CommentUtils.validateComment(comment, friendships);
        if (!errors.isEmpty()) {
            return errors;
        }

        em.persist(comment);
        return List.of();
    }
}
