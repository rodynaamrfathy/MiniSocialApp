package service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.WebApplicationException;
import Utils.UserUtils;
import models.User;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    // Register User
    public User register(User user) {
        em.persist(user);
        return user;
    }

    // Manage or update user profile
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

    
    // User login - returns boolean without exceptions
    public boolean login(String userName, String password) {
        TypedQuery<User> query = em.createQuery(
            UserUtils.LOGIN_QUERY, User.class);
        query.setParameter("userName", userName);
        query.setParameter("password", password);

        List<User> users = query.getResultList();
        return !users.isEmpty(); // Return true if user is found
    }

    // Validate user input
    public List<String> validateUser(User user) {
        // Delegate to UserUtils for validation
        return UserUtils.validateUser(user, em);
    }

    // Get user by ID
    public User getUserById(Long userId) {
        TypedQuery<User> query = em.createQuery(
            UserUtils.GET_USER_BY_ID_QUERY, User.class);
        query.setParameter("userId", userId);
        List<User> users = query.getResultList();

        return users.isEmpty() ? null : users.get(0);
    }

    // Get user string representation by ID
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

    // Get all users (string representations)
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
