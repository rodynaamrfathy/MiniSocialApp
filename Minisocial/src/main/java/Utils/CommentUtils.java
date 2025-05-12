package Utils;

import models.Comment;
import models.Friendships;
import models.GroupPost;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentUtils {

	public static final String GET_COMMENTS_BY_USER_POST_QUERY =
		    "SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.timestamp DESC";

		public static final String GET_COMMENTS_BY_GROUP_POST_QUERY =
		    "SELECT c FROM Comment c WHERE c.groupPost.postId = :postId AND c.groupPost.group.groupId = :groupId ORDER BY c.timestamp DESC";


    public static final String GET_ALL_FRIENDS_QUERY =
        "SELECT f FROM Friendships f WHERE f.user.userId = :userId OR f.friend.userId = :userId";

    public static List<String> validateComment(Comment comment, List<Friendships> friendships) {
        List<String> errors = new ArrayList<>();

        User creator = comment.getCreator();
        User author = comment.getPost() != null ? comment.getPost().getUser() : null;

        if (creator == null || creator.getUserId() == null) {
            errors.add("Invalid comment creator.");
        }

        if (comment.getPost() == null || comment.getPost().getPostId() <= 0) {
            errors.add("Invalid post selected.");
        }

        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            errors.add("Comment content cannot be empty.");
        }

        if (comment.getContent() != null && comment.getContent().length() > 250) {
            errors.add("Comment is too long (max 250 characters).");
        }

        // Rule: Post author cannot comment on their own post
        if (creator != null && author != null && creator.getUserId().equals(author.getUserId())) {
            errors.add("You cannot comment on your own post.");
        }

        // Rule: Only friends of the post author can comment
        if (creator != null && author != null && !areUsersFriends(creator, author, friendships)) {
            errors.add("You can only comment on posts created by your friends.");
        }

        return errors;
    }

    public static List<String> validateGroupComment(Comment comment, List<Friendships> friendships, Long groupId) {
        List<String> errors = new ArrayList<>();

        User creator = comment.getCreator();
        GroupPost groupPost = comment.getGroupPost();

        if (creator == null || creator.getUserId() == null) {
            errors.add("Invalid comment creator.");
        }

        if (groupPost == null || groupPost.getPostId() <= 0) {
            errors.add("Invalid group post selected.");
        }

        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            errors.add("Comment content cannot be empty.");
        }

        if (comment.getContent() != null && comment.getContent().length() > 250) {
            errors.add("Comment is too long (max 250 characters).");
        }

        // Rule: Check if the user is a member of the group (this is where you'll usually call a group membership check)
        boolean isGroupMember = isUserMemberOfGroup(creator, groupId);

        if (!isGroupMember) {
            errors.add("You must be a member of the group to comment on this post.");
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

    // 🆕 Stub method: Replace with actual group membership check logic later!
    private static boolean isUserMemberOfGroup(User user, Long groupId) {
        // TODO: Implement actual group membership check using DB/Service
        // For now, we assume the user is always a member (mock)
        return true;
    }
}
