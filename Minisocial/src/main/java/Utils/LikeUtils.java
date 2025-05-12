package Utils;

import models.Friendships;
import models.GroupPost;
import models.User;
import models.UserPost;
import java.util.ArrayList;
import java.util.List;


public class LikeUtils {


    
    public static final String GET_LIKES_BY_USER_POST_QUERY =
        "SELECT l FROM Like l WHERE l.post.postId = :postId ORDER BY l.timestamp DESC";

    public static final String GET_LIKES_BY_GROUP_POST_QUERY =
        "SELECT l FROM Like l WHERE l.groupPost.postId = :postId AND l.groupPost.group.groupId = :groupId ORDER BY l.timestamp DESC";

    public static final String GET_ALL_FRIENDS_QUERY =
        "SELECT f FROM Friendships f WHERE f.user.userId = :userId OR f.friend.userId = :userId";

    public static final String CHECK_GROUP_MEMBERSHIP_QUERY =
    	    "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId AND gm.status = 'approved'";

    
    public static List<String> validateUserPostLike(User user, UserPost post, List<Friendships> friendships) {
        List<String> errors = new ArrayList<>();

        if (user == null || user.getUserId() == null) {
            errors.add("Invalid user.");
        }

        if (post == null || post.getPostId() <= 0) {
            errors.add("Invalid post selected.");
        }

        // Rule: Post author cannot like their own post
        if (user != null && post != null && user.getUserId().equals(post.getUser().getUserId())) {
            errors.add("You cannot like your own post.");
        }

        // Rule: Only friends of the post author can like the post
        if (user != null && post != null && !areUsersFriends(user, post.getUser(), friendships)) {
            errors.add("You can only like posts created by your friends.");
        }

        return errors;
    }

    public static List<String> validateGroupPostLike(User user, GroupPost groupPost, List<Friendships> friendships, boolean isGroupMember) {
        List<String> errors = new ArrayList<>();

        if (user == null || user.getUserId() == null) {
            errors.add("Invalid user.");
        }

        if (groupPost == null || groupPost.getPostId() <= 0) {
            errors.add("Invalid group post selected.");
        }

        // Rule: Check if the user is a member of the group
        if (!isGroupMember) {
            errors.add("You must be a member of the group to like this post.");
        }

        return errors;
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
