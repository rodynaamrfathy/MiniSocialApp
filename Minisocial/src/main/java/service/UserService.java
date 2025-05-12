package service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.WebApplicationException;
import Utils.UserUtils;
import models.User;

/**
 * Service class responsible for handling user-related operations.
 * This class provides methods for user registration, profile management,
 * login, validation, and user data retrieval.
 */
@Stateless
public class UserService {

    /**
     * Injected EntityManager for database operations.
     */
    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    /**
     * Registers a new user.
     * 
     * @param user the user to be registered.
     * @return the registered user.
     */
    public User register(User user) {
        em.persist(user);
        return user;
    }

    /**
     * Manages or updates the user profile.
     * If the user does not exist, throws a WebApplicationException with a 404 status.
     * 
     * @param user the user object containing the updated profile data.
     */
    public void manageProfile(User user) {
        User existingUserToUpdate = em.find(User.class, user.getUserId());

        if (existingUserToUpdate == null) {
            throw new WebApplicationException("User not found", 404);
        }

        existingUserToUpdate.setFirstName(user.getFirstName());
        existingUserToUpdate.setLastName(user.getLastName());
        existingUserToUpdate.setEmail(user.getEmail());
        existingUserToUpdate.setBirthdate(user.getBirthdate());
        existingUserToUpdate.setBio(user.getBio());
        existingUserToUpdate.setPassword(user.getPassword());
    }

    /**
     * Handles user login by verifying the username and password.
     * Returns true if the user is found with matching credentials, false otherwise.
     * 
     * @param userName the username of the user.
     * @param password the password of the user.
     * @return true if the user is found and the credentials match, false otherwise.
     */
    public boolean login(String userName, String password) {
        TypedQuery<User> query = em.createQuery(
            UserUtils.LOGIN_QUERY, User.class);
        query.setParameter("userName", userName);
        query.setParameter("password", password);

        List<User> users = query.getResultList();
        return !users.isEmpty(); // Return true if user is found
    }

    /**
     * Validates the provided user data by delegating to the UserUtils class.
     * 
     * @param user the user object containing the data to be validated.
     * @return a list of validation errors, or an empty list if no errors.
     */
    public List<String> validateUser(User user) {
        // Delegate to UserUtils for validation
        return UserUtils.validateUser(user, em);
    }

    /**
     * Retrieves a user by their ID.
     * 
     * @param userId the ID of the user to retrieve.
     * @return the user object if found, or null if no user with that ID exists.
     */
    public User getUserById(Long userId) {
        TypedQuery<User> query = em.createQuery(
            UserUtils.GET_USER_BY_ID_QUERY, User.class);
        query.setParameter("userId", userId);
        List<User> users = query.getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    /**
     * Retrieves the string representation of a user by their ID.
     * 
     * @param userId the ID of the user to retrieve.
     * @return a list containing the user's string representation, or a "User not found" message.
     */
    public List<String> getUserStringById(Long userId) {
        User user = getUserById(userId);

        List<String> userStrings = new ArrayList<>();
        if (user != null && user.getUserId() != null) {
            userStrings.add(user.toString());
        } else {
            userStrings.add("User not found");
        }

        return userStrings;
    }

    /**
     * Retrieves all users and returns their string representations.
     * 
     * @return a list of all users' string representations.
     */
    public List<String> getAllUsers() {
        TypedQuery<User> query = em.createQuery(
            UserUtils.GET_ALL_USERS_QUERY, User.class);
        List<User> users = query.getResultList();
        List<String> userStrings = new ArrayList<>();

        for (User user : users) {
            userStrings.add(user.toString());
        }

        return userStrings;
    }

    /**
     * Searches for users by username.
     * 
     * @param userName the username or part of the username to search for.
     * @return a list of users whose username matches the search query.
     */
    public List<String> searchUsersByUsername(String userName) {
        TypedQuery<User> query = em.createQuery(UserUtils.SEARCH_BY_USERNAME_QUERY, User.class);
        query.setParameter("userName", "%" + userName + "%");

        List<User> users = query.getResultList();
        List<String> userStrings = new ArrayList<>();
        for (User user : users) {
            userStrings.add(user.toString());
        }

        return userStrings;
    }

    /**
     * Searches for users by email.
     * 
     * @param email the email or part of the email to search for.
     * @return a list of users whose email matches the search query.
     */
    public List<String> searchUsersByEmail(String email) {
        TypedQuery<User> query = em.createQuery(UserUtils.SEARCH_BY_EMAIL_QUERY, User.class);
        query.setParameter("email", "%" + email + "%");

        List<User> users = query.getResultList();
        List<String> userStrings = new ArrayList<>();
        for (User user : users) {
            userStrings.add(user.toString());
        }

        return userStrings;
    }
}
