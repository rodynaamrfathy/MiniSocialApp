package Utils;

import models.Group;
import models.User;
import models.GroupPost;

import java.util.ArrayList;
import java.util.List;

/**
 * 📦 GroupPostUtil – Utilities for GroupPost validation and query construction.
 * 
 * ✅ Responsibilities:
 * - Validates group post fields.
 * - Provides query strings for fetching group posts.
 */
public class GroupPostUtil {

    // 🔍 Query to fetch group posts by group
    public static final String GET_GROUP_POSTS_QUERY = 
        "SELECT p FROM GroupPost p WHERE p.group.groupId = :groupId";

    // 📝 Method to validate the group post (for creating and editing group posts)
    public static List<String> validateGroupPost(String content, String imageUrl, User author, Group group) {
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

        // Validate group existence for GroupPost
        if (group == null) {
            errors.add("Group is required for GroupPost.");
        }

        return errors;
    }

    // 📝 Method to validate if the user has permission to edit or delete the group post
    public static boolean canEditAndDeletePost(User user, GroupPost post) {
        // Check if the post belongs to the user
        return post != null && post.getUser().getUserId().equals(user.getUserId());
    }
}
