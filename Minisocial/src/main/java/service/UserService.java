package service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.ws.rs.WebApplicationException;

import models.User;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

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

    // User login
    public User login(String userName, String password) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.userName = :userName AND u.password = :password", User.class);
        query.setParameter("userName", userName);
        query.setParameter("password", password);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    // Validate user input
    public List<String> validateUser(User user) {
        List<String> errors = new ArrayList<>();

        if (user.getEmail() == null || !EMAIL_PATTERN.matcher(user.getEmail()).matches()) {
            errors.add("Invalid email format");
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }

        if (!errors.contains("Invalid email format") && isEmailTaken(user.getEmail())) {
            errors.add("Email is already in use");
        }

        return errors;
    }

    // Save new user
    public User saveUser(User user) {
        em.persist(user);
        return user;
    }

    private boolean isEmailTaken(String email) {
        long count = em.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
            .setParameter("email", email)
            .getSingleResult();
        return count > 0;
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return em.find(User.class, userId);
    }

    // Get user string representation by ID
    public List<String> getUserStringById(Long userId) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                              .setParameter("userId", userId)
                              .getResultList();

        List<String> userStrings = new ArrayList<>();
        if (!users.isEmpty()) {
            userStrings.add(users.get(0).toString());
        } else {
            userStrings.add("User not found");
        }

        return userStrings;
    }

    // Get all users (string representations)
    public List<String> getAllUsers() {
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        List<String> userStrings = new ArrayList<>();

        for (User user : users) {
            userStrings.add(user.toString());
        }

        return userStrings;
    }
}
