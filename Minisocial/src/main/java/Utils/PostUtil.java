package Utils;

import models.Group;
import models.User;
import models.UserPost;
import java.util.ArrayList;
import java.util.List;

/**
 *   PostUtil – Utilities for Post validation and query construction.
 * 
 * 	 Responsibilities:
 * - Validates post fields.
 * - Provides query strings for fetching posts.
 */
public class PostUtil {

  
    public static final String GET_POSTS_BY_USER_QUERY = 
        "SELECT p FROM UserPost p WHERE p.user.userId = :userId";

    /**
     * Validates the given post data (content, image URL, and author) 
     * for both creation and editing of posts.
     * 
     * @param content  The content of the post
     * @param imageUrl The image URL attached to the post
     * @param author   The author of the post
     * @param group    The group (for GroupPost type)
     * @param postType The type of post (user or group)
     * @return A list of validation error messages (if any)
     */
    public static List<String> validatePost(String content, String imageUrl, User author, Group group, String postType) {
        List<String> errors = new ArrayList<>();

       
        if (content == null || content.trim().isEmpty()) {
            errors.add("Content must not be empty");
        } else if (content.length() > 50) {
            errors.add("Content should not exceed 50 characters");
        }

       
        if (imageUrl != null && imageUrl.length() > 255) {
            errors.add("Image URL should not exceed 255 characters");
        }

      
        if (author == null || author.getUserId() == null) {
            errors.add("Author is required.");
        }

      
        if ("group".equalsIgnoreCase(postType) && group == null) {
            errors.add("Group is required for GroupPost.");
        }

        return errors;  // Return the list of validation errors
    }

    /**
     * Validates whether the user has permission to edit or delete the post.
     * The user is allowed to edit or delete the post only if they are the author.
     * 
     * @param user The user trying to edit or delete the post
     * @param post The post to check
     * @return true if the user is allowed to edit or delete the post, false otherwise
     */
    public static boolean canEditAndDeletePost(User user, UserPost post) {
        
        return post != null && post.getUser().getUserId().equals(user.getUserId());
    }
}
