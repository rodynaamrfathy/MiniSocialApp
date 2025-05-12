package Utils;

import models.Group;
import models.User;
import models.GroupPost;

import java.util.ArrayList;
import java.util.List;

/**
 *   GroupPostUtil – Utility class for GroupPost validation and query construction.
 *
 *   This class is responsible for:
 * 
 *   Validating the fields of a GroupPost entity before creation or modification.
 *   Providing JPQL query strings for fetching GroupPost entities based on group criteria.
 * 
 */
public class GroupPostUtil {

    /**
     * JPQL query string to fetch all posts associated with a specific group.
     * The query selects GroupPost entities where the group's ID matches the provided parameter.
     */
    public static final String GET_GROUP_POSTS_QUERY = 
        "SELECT p FROM GroupPost p WHERE p.group.groupId = :groupId";

    /**
     * Validates the input fields for creating or editing a GroupPost.
     *
     * @param content  The textual content of the post.
     * @param imageUrl The URL of an optional image associated with the post.
     * @param author   The User entity representing the post's author.
     * @param group    The Group entity to which the post belongs.
     * @return A list of error messages indicating any validation failures.
     */
    public static List<String> validateGroupPost(String content, String imageUrl, User author, Group group) {
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

       
        if (group == null) {
            errors.add("Group is required for GroupPost.");
        }

        return errors;
    }

    /**
     * Checks if the specified user has permission to edit or delete the given GroupPost.
     * A user can edit or delete a post only if they are the author of the post.
     *
     * @param user The User attempting the action.
     * @param post The GroupPost entity being edited or deleted.
     * @return True if the user is the author of the post; otherwise, false.
     */
    public static boolean canEditAndDeletePost(User user, GroupPost post) {
        // Verify that the post exists and belongs to the given user
        return post != null && post.getUser().getUserId().equals(user.getUserId());
    }
}
