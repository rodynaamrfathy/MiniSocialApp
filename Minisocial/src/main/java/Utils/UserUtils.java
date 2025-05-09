package Utils;

import models.User;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

    // Hardcoded query strings
    public static final String LOGIN_QUERY = "SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password";
    public static final String GET_USER_BY_ID_QUERY = "SELECT u FROM User u WHERE u.userId = :userId";
    public static final String GET_ALL_USERS_QUERY = "SELECT u FROM User u";
    public static final String EMAIL_TAKEN_QUERY = "SELECT COUNT(u) FROM User u WHERE u.email = :email";
    
    public static final String SEARCH_BY_USERNAME_QUERY =
    	    "SELECT u FROM User u WHERE u.userName LIKE :userName";

    	public static final String SEARCH_BY_EMAIL_QUERY =
    	    "SELECT u FROM User u WHERE u.email LIKE :email";


    // Validate user input
    public static List<String> validateUser(User user, EntityManager em) {
        List<String> errors = new ArrayList<>();

        // Email validation
        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("Invalid email format");
        }

        // Password length validation
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        // Check if email is already taken
        if (!errors.contains("Invalid email format") && isEmailTaken(user.getEmail(), em)) {
            errors.add("Email is already in use");
        }

        return errors;
    }

    // Check if the email is already taken
    public static boolean isEmailTaken(String email, EntityManager em) {
        long count = em.createQuery(EMAIL_TAKEN_QUERY, Long.class)
            .setParameter("email", email)
            .getSingleResult();
        return count > 0;
    }
}
