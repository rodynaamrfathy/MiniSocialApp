package Utils;

import models.User;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 🛠️ GroupUtil
 *
 * Utility class providing helper methods for Group-related operations.
 * Handles:
 * - Group name uniqueness validation.
 * - Admin user existence validation.
 * - Query constants for group operations.
 */
public class GroupUtil {

    /**
     * 🔍 JPQL query to check if a group with the specified name already exists.
     */
    public static final String CHECK_GROUP_NAME_EXISTS_QUERY =
            "SELECT COUNT(g) FROM Group g WHERE g.groupName = :name";

    // ============================
    // ✅ Validation Methods
    // ============================

    /**
     * Validates the existence of an admin user by ID.
     *
     * @param adminId        ID of the admin user to validate.
     * @param entityManager  JPA EntityManager for database operations.
     * @param errors         List to accumulate validation error messages.
     * @return User entity if found, otherwise null (error added to list if not found).
     */
    public static User validateAdminUser(Long adminId, EntityManager entityManager, List<String> errors) {
        User adminUser = entityManager.find(User.class, adminId);
        if (adminUser == null) {
            errors.add("Admin user not found.");
        }
        return adminUser;
    }

    /**
     * ✅ Validates the uniqueness of a group name.
     *
     * @param groupName      Group name to validate.
     * @param entityManager  JPA EntityManager for database access.
     * @param errors         List to accumulate validation error messages.
     * @return true if a group with the same name already exists, false otherwise.
     */
    public static boolean validateGroupNameUniqueness(String groupName, EntityManager entityManager, List<String> errors) {
        boolean exists = entityManager
                .createQuery(CHECK_GROUP_NAME_EXISTS_QUERY, Long.class)
                .setParameter("name", groupName)
                .getSingleResult() > 0;

        if (exists) {
            errors.add("Group with the same name already exists.");
        }
        return exists;
    }
}
