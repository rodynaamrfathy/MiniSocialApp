package Utils;

import models.Comment;
import models.Friendships;
import models.GroupPost;
import models.User;
import service.CommentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing methods related to comments, including validation and query strings.
 */
public class CommentUtils {

    // Constant for querying comments by user post ID
    public static final String GET_COMMENTS_BY_USER_POST_QUERY =
            "SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.timestamp DESC";

    // Constant for querying comments by group post ID and group ID
    public static final String GET_COMMENTS_BY_GROUP_POST_QUERY =
            "SELECT c FROM Comment c WHERE c.groupPost.postId = :postId AND c.groupPost.group.groupId = :groupId ORDER BY c.timestamp DESC";

    // Constant for querying all friendships of a user
    public static final String GET_ALL_FRIENDS_QUERY =
            "SELECT f FROM Friendships f WHERE f.user.userId = :userId OR f.friend.userId = :userId";

    // Constant for checking if a user is a member of a group
    public static final String CHECK_GROUP_MEMBERSHIP_QUERY =
            "SELECT gm FROM GroupMembership gm WHERE gm.user.userId = :userId AND gm.group.groupId = :groupId AND gm.status = 'approved'";

    /**
     * Validates a comment based on several rules.
     *
     * @param comment     The comment to be validated.
     * @param friendships The list of friendships to check the relationship between users.
     * @return A list of error messages if validation fails, otherwise an empty list.
     */
    public static List<String> validateComment(Comment comment, List<Friendships> friendships) {
        List<String> errors = new ArrayList<>();

        User creator = comment.getCreator();
        User author = comment.getPost() != null ? comment.getPost().getUser() : null;

        // Validate creator
        if (creator == null || creator.getUserId() == null) {
            errors.add("Invalid comment creator.");
        }

        // Validate post
        if (comment.getPost() == null || comment.getPost().getPostId() <= 0) {
            errors.add("Invalid post selected.");
        }

        // Validate content
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

    /**
     * Validates a comment on a group post based on group membership and other rules.
     *
     * @param comment      The comment to be validated.
     * @param friendships  The list of friendships to check the relationship between users.
     * @param groupId      The ID of the group.
     * @param commentService The comment service to check group membership.
     * @return A list of error messages if validation fails, otherwise an empty list.
     */
    public static List<String> validateGroupComment(Comment comment, List<Friendships> friendships, Long groupId, CommentService commentService) {
        List<String> errors = new ArrayList<>();

        User creator = comment.getCreator();
        GroupPost groupPost = comment.getGroupPost();

        // Validate creator
        if (creator == null || creator.getUserId() == null) {
            errors.add("Invalid comment creator.");
        }

        // Validate group post
        if (groupPost == null || groupPost.getPostId() <= 0) {
            errors.add("Invalid group post selected.");
        }

        // Validate content
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            errors.add("Comment content cannot be empty.");
        }
        if (comment.getContent() != null && comment.getContent().length() > 250) {
            errors.add("Comment is too long (max 250 characters).");
        }

        // Rule: Check if the user is a member of the group using the service method
        boolean isGroupMember = commentService.isUserMemberOfGroup(creator, groupId);

        if (!isGroupMember) {
            errors.add("You must be a member of the group to comment on this post.");
        }

        return errors;
    }

    /**
     * Checks if two users are friends based on the provided list of friendships.
     *
     * @param u1           The first user.
     * @param u2           The second user.
     * @param friendships  The list of friendships to check.
     * @return True if the users are friends, otherwise false.
     */
    public static boolean areUsersFriends(User u1, User u2, List<Friendships> friendships) {
        if (u1 == null || u2 == null || friendships == null) return false;

        Long id1 = u1.getUserId();
        Long id2 = u2.getUserId();

        // Check if either user is friends with the other
        return friendships.stream().anyMatch(f ->
                (f.getUser().getUserId().equals(id1) && f.getFriend().getUserId().equals(id2)) ||
                        (f.getUser().getUserId().equals(id2) && f.getFriend().getUserId().equals(id1))
        );
    }
}
