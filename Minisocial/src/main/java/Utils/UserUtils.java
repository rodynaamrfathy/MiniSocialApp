package Utils;

import models.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ==========================================================
 * UserUtils.java 
 * ==========================================================
 * Utility class for performing common user-related operations.
 * Includes methods for user validation, email uniqueness check,
 * and predefined JPQL queries for user retrieval and search.
 * 
 * This class follows stateless utility patterns.
 * ==========================================================
 *
 */
public class UserUtils {

    /**
     * Regular expression pattern for basic email format validation.
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

    // ============================== JPQL Queries ==============================

    /**
     * Query to authenticate a user by username and password.
     */
    public static final String LOGIN_QUERY = 
        "SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password";

    /**
     * Query to retrieve a user by their unique userId.
     */
    public static final String GET_USER_BY_ID_QUERY = 
        "SELECT u FROM User u WHERE u.userId = :userId";

    /**
     * Query to fetch all users from the database.
     */
    public static final String GET_ALL_USERS_QUERY = 
        "SELECT u FROM User u";

    /**
     * Query to check if an email is already taken by another user.
     */
    public static final String EMAIL_TAKEN_QUERY = 
        "SELECT COUNT(u) FROM User u WHERE u.email = :email";

    /**
     * Query to search for users by matching username pattern.
     */
    public static final String SEARCH_BY_USERNAME_QUERY = 
        "SELECT u FROM User u WHERE u.userName LIKE :userName";

    /**
     * Query to search for users by matching email pattern.
     */
    public static final String SEARCH_BY_EMAIL_QUERY = 
        "SELECT u FROM User u WHERE u.email LIKE :email";

    // ============================== Utility Methods ==============================

    /**
     * Validates user input fields such as email format, password length,
     * and checks if the email is already registered in the system.
     *
     * @param user User object to be validated.
     * @param em EntityManager for database operations.
     * @return List of error messages found during validation.
     */
    public static List<String> validateUser(User user, EntityManager em) {
        List<String> errors = new ArrayList<>();

        // ✅ Email format validation
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("Invalid email format");
        }

        // ✅ Password length validation (minimum 8 characters)
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        // ✅ Check if email is already taken (only if email format is valid)
        if (!errors.contains("Invalid email format") && isEmailTaken(user.getEmail(), em)) {
            errors.add("Email is already in use");
        }

        return errors;
    }

    /**
     * Checks whether a given email is already registered in the system.
     *
     * @param email The email address to check for uniqueness.
     * @param em EntityManager for executing the query.
     * @return true if the email is already taken, false otherwise.
     */
    public static boolean isEmailTaken(String email, EntityManager em) {
        long count = em.createQuery(EMAIL_TAKEN_QUERY, Long.class)
                       .setParameter("email", email)
                       .getSingleResult();
        return count > 0;
    }
}
