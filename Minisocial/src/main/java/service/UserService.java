package service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.persistence.*;
import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.Friendships;
import models.User;

@Stateless
public class UserService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;
	@PersistenceContext(unitName = "hello")
	private EntityManager em;
	
    //@Inject
	//private UserTransaction tx;

    
    // kont ha use UserTransaction begin, commit, rollback bs hya already aslun ejb
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

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+\\..+$");

 // Get User by ID using a query and return their string representation as a list
    public List<String> getUserStringById(Long userId) {
        // Execute the query to find the user by ID
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                              .setParameter("userId", userId)
                              .getResultList();

        List<String> userStrings = new ArrayList<>();

        // If user is found, add their string representation to the list
        if (!users.isEmpty()) {
            userStrings.add(users.get(0).toString());
        } else {
            // Optionally, you can add "User not found" in the list for consistency
            userStrings.add("User not found");
        }

        return userStrings; // Return the list
    }



 // Get All Users and return their string representations
    public List<String> getAllUsers() {
        List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
        List<String> userStrings = new ArrayList<>();

        for (User user : users) {
            userStrings.add(user.toString()); // Converts each user to their string representation
        }

        return userStrings;
    }


    // 🧠 Returns a list of validation errors
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

    // ✅ Only called after passing validation
    public User saveUser(User user) {
        em.persist(user);
        return user;
    }

    private boolean isEmailTaken(String email) {
        long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                       .setParameter("email", email)
                       .getSingleResult();
        return count > 0;
    }
    
    /*
    //GetAllUsers from the database
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
    */
    
    //========================================================================2========================
    
    // Add Friend Request
    public boolean sendFriendRequest(User requester, User receiver) {
        if (isAlreadyFriends(requester, receiver) || hasPendingRequest(requester, receiver)) {
            return false; // Already friends or pending request exists
        }

        FriendshipRequests request = new FriendshipRequests();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setStatus(FriendshipStatus.PENDING);
        request.setTimeStamp(new java.util.Date());

        em.persist(request);
        return true; // Friend request sent successfully
    }

    // Accept Friend Request
    public boolean acceptFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.ACCEPTED);

            // Create friendship relationship
            Friendships friendship = new Friendships();
            friendship.setUser(request.getRequester());
            friendship.setFriend(request.getReceiver());
            friendship.setSince(new java.util.Date());
            em.persist(friendship);

            friendship = new Friendships();
            friendship.setUser(request.getReceiver());
            friendship.setFriend(request.getRequester());
            friendship.setSince(new java.util.Date());
            em.persist(friendship);

            em.merge(request);
            return true;
        }
        return false;
    }

    // Reject Friend Request
    public boolean rejectFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.REJECTED);
            em.merge(request);
            return true;
        }
        return false;
    }

    // Check if they are already friends
    private boolean isAlreadyFriends(User user1, User user2) {
        long count = em.createQuery("SELECT COUNT(f) FROM Friendships f WHERE (f.user = :user1 AND f.friend = :user2) OR (f.user = :user2 AND f.friend = :user1)", Long.class)
                       .setParameter("user1", user1)
                       .setParameter("user2", user2)
                       .getSingleResult();
        return count > 0;
    }

    // Check if there's already a pending request between users
    private boolean hasPendingRequest(User requester, User receiver) {
        long count = em.createQuery("SELECT COUNT(r) FROM FriendshipRequests r WHERE r.requester = :requester AND r.receiver = :receiver AND r.status = :status", Long.class)
                       .setParameter("requester", requester)
                       .setParameter("receiver", receiver)
                       .setParameter("status", FriendshipStatus.PENDING)
                       .getSingleResult();
        return count > 0;
    }

    // Get User by ID
    public User getUserById(Long userId) {
        return em.find(User.class, userId);
    }

    // Get FriendshipRequest by ID
    public FriendshipRequests getFriendshipRequestById(int requestId) {
        return em.find(FriendshipRequests.class, requestId);
    }
    
    
 // Get all pending friendship requests where the user is the receiver
    public List<String> getAllFriendshipRequests(Long userId) {
        // Retrieve all friendship requests where the user is the receiver and the status is pending
        List<FriendshipRequests> requests = em.createQuery("SELECT r FROM FriendshipRequests r WHERE r.receiver.id = :userId AND r.status = :status", FriendshipRequests.class)
                                               .setParameter("userId", userId)
                                               .setParameter("status", FriendshipStatus.PENDING)
                                               .getResultList();

        // Create a list to store the string representations of each request
        List<String> requestStrings = new ArrayList<>();

        // Add the toString() representation of each request to the list
        for (FriendshipRequests request : requests) {
            requestStrings.add(request.toString());  // Calls the toString() method
        }

        return requestStrings;  // Return the list of string representations
    }



 // Get a specific friendship request by its ID and return its string representation
    public String getFriendshipRequestById2(int requestId) {
        // Retrieve the friendship request by its ID
        FriendshipRequests request = em.createQuery("SELECT r FROM FriendshipRequests r WHERE r.friendship_request_id = :requestId", FriendshipRequests.class)
                                       .setParameter("requestId", requestId)
                                       .getSingleResult();

        // Return the string representation of the friendship request
        if (request != null) {
            return request.toString();  // Calls the toString() method
        }

        return "FriendshipRequest not found";  // If not found, return this message
    }


    
}
