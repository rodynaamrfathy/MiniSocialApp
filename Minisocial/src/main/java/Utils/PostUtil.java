package Utils;

import models.Group;
import models.User;
import models.UserPost;
import java.util.ArrayList;
import java.util.List;

/**
 * 📦 PostUtil – Utilities for Post validation and query construction.
 * 
 * ✅ Responsibilities:
 * - Validates post fields.
 * - Provides query strings for fetching posts.
 */
public class PostUtil {

    // 🔍 Query to fetch posts by user
    public static final String GET_POSTS_BY_USER_QUERY = 
        "SELECT p FROM UserPost p WHERE p.user.userId = :userId";

    // 📝 Method to validate the post (for creating and editing posts)
    public static List<String> validatePost(String content, String imageUrl, User author, Group group, String postType) {
        List<String> errors = new ArrayList<>();

        // Validate content length and image URL
        if (content == null || content.trim().isEmpty()) {
            errors.add("Content must not be empty");
        } else if (content.length() > 50) {
            errors.add("Content should not exceed 50 characters");
        }

        if (imageUrl != null && imageUrl.length() > 255) {
            errors.add("Image URL should not exceed 255 characters");
        }

        // Validate author existence
        if (author == null || author.getUserId() == null) {
            errors.add("Author is required.");
        }

        // Validate group existence (for GroupPost only)
        if ("group".equalsIgnoreCase(postType) && group == null) {
            errors.add("Group is required for GroupPost.");
        }

        return errors;
    }

    // 📝 Method to validate if the user has permission to edit or delete the post
    public static boolean canEditAndDeletePost(User user, UserPost post) {
        // Check if the post belongs to the user
        return post != null && post.getUser().getUserId().equals(user.getUserId());
    }
}