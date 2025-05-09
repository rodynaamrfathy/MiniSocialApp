package Utils;

import models.Comment;
import models.Friendships;
import models.User;

import java.util.ArrayList;
import java.util.List;

public class CommentUtils {

    public static final String GET_COMMENTS_BY_POST_QUERY =
        "SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.timestamp ASC";

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
