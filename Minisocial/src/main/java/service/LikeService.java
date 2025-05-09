package service;

import Utils.LikeUtils;
import models.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class LikeService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }

    public List<Like> getLikesForPost(int postId) {
        TypedQuery<Like> query = em.createQuery(LikeUtils.GET_LIKES_BY_POST_QUERY, Like.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    public List<Friendships> getAllFriendshipsForUser(User user) {
        TypedQuery<Friendships> query = em.createQuery(LikeUtils.GET_ALL_FRIENDS_QUERY, Friendships.class);
        query.setParameter("userId", user.getUserId());
        return query.getResultList();
    }

    public List<String> likePost(User user, int postId) {
        UserPost post = em.find(UserPost.class, postId);
        if (post == null) {
            return List.of("Post not found.");
        }

        List<Like> existingLikes = getLikesForPost(postId);
        List<Friendships> friendships = getAllFriendshipsForUser(user);

        List<String> errors = LikeUtils.validateLike(user, post, existingLikes, friendships);
        if (!errors.isEmpty()) {
            return errors;
        }

        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        em.persist(like);
        
        post.setLikesCount(post.getLikesCount() + 1);
        em.merge(post);

        return List.of();
    }
}