package Utils;

import models.Friendships;
import models.Like;
import models.User;
import models.UserPost;

import java.util.ArrayList;
import java.util.List;

public class LikeUtils {

    public static final String GET_LIKES_BY_POST_QUERY =
        "SELECT l FROM Like l WHERE l.post.postId = :postId";

    public static List<String> validateLike(User user, UserPost post, List<Like> existingLikes, List<Friendships> friendships) {
        List<String> errors = new ArrayList<>();

        if (user == null || user.getUserId() == null) {
            errors.add("Invalid user selected.");
        }

        if (post == null || post.getPostId() <= 0) {
            errors.add("Invalid post selected.");
        }
        
        if (user != null && post != null && user.getUserId().equals(post.getUser().getUserId())) {
            errors.add("You cannot like your own post.");
        }

        if (user != null && post != null && !areUsersFriends(user, post.getUser(), friendships)) {
            errors.add("You can only like posts created by your friends.");
        }

        if (user != null && post != null && hasUserAlreadyLiked(user, post, existingLikes)) {
            errors.add("You have already liked this post.");
        }

        return errors;
    }

    public static boolean hasUserAlreadyLiked(User user, UserPost post, List<Like> existingLikes) {
        if (user == null || post == null || existingLikes == null) return false;

        return existingLikes.stream()
            .anyMatch(like -> like.getUser() != null &&
                              like.getPost() != null &&
                              like.getUser().getUserId().equals(user.getUserId()) &&
                              like.getPost().getPostId() == post.getPostId());
    }

    public static boolean areUsersFriends(User u1, User u2, List<Friendships> friendships) {
        if (u1 == null || u2 == null || friendships == null) return false;

        Long id1 = u1.getUserId();
        Long id2 = u2.getUserId();

        return friendships.stream().anyMatch(f ->
            (f.getUser().getUserId().equals(id1) && f.getFriend().getUserId().equals(id2)) ||
            (f.getUser().getUserId().equals(id2) && f.getFriend().getUserId().equals(id1))
        );
    }
}
