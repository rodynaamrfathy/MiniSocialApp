package service;

import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.User;
import Utils.FriendshipUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * Service class responsible for managing friendship-related operations.
 * This class provides methods to send, accept, reject friendship requests,
 * suggest friends, check friendship status, and map friendship information.
 */
@Stateless
public class FriendshipService {

    /**
     * Injected EntityManager for database operations.
     */
    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    /**
     * Suggests potential friends for the given user.
     * 
     * @param userId the ID of the user for whom friends are being suggested.
     * @return a list of suggested friends.
     */
    public List<User> suggestFriends(Long userId) {
        return FriendshipUtils.suggestFriends(em, userId);
    }

    /**
     * Sends a friendship request from one user to another.
     * 
     * @param requester the user sending the friend request.
     * @param receiver the user receiving the friend request.
     * @return true if the request was successfully sent, false otherwise.
     */
    public boolean sendFriendRequest(User requester, User receiver) {
        return FriendshipUtils.sendFriendRequest(em, requester, receiver);
    }

    /**
     * Accepts a pending friendship request.
     * 
     * @param request the friendship request to be accepted.
     * @return true if the request was accepted, false if the status is not PENDING.
     */
    public boolean acceptFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.ACCEPTED);
            FriendshipUtils.createMutualFriendship(em, request.getRequester(), request.getReceiver());
            em.merge(request);
            return true;
        }
        return false;
    }

    /**
     * Rejects a pending friendship request.
     * 
     * @param request the friendship request to be rejected.
     * @return true if the request was rejected, false if the status is not PENDING.
     */
    public boolean rejectFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.REJECTED);
            em.merge(request);
            return true;
        }
        return false;
    }

    /**
     * Retrieves all pending friendship requests for a user.
     * 
     * @param userId the ID of the user whose pending requests are to be fetched.
     * @return a list of pending request strings.
     */
    public List<String> getAllPendingRequests(Long userId) {
        return FriendshipUtils.getAllPendingRequests(em, userId);
    }

    /**
     * Retrieves a friendship request by its ID.
     * 
     * @param requestId the ID of the request.
     * @return the FriendshipRequests entity associated with the given ID.
     */
    public FriendshipRequests getRequestById(int requestId) {
        return em.find(FriendshipRequests.class, requestId);
    }

    /**
     * Retrieves the string representation of a friendship request by its ID.
     * 
     * @param requestId the ID of the request.
     * @return the string representation of the request, or a message if not found.
     */
    public String getRequestStringById(int requestId) {
        FriendshipRequests request = em.find(FriendshipRequests.class, requestId);
        return request != null ? request.toString() : "FriendshipRequest not found";
    }

    /**
     * Checks if two users are already friends.
     * 
     * @param u1 the first user.
     * @param u2 the second user.
     * @return true if the users are friends, false otherwise.
     */
    public boolean isAlreadyFriends(User u1, User u2) {
        return FriendshipUtils.isAlreadyFriends(em, u1, u2);
    }

    /**
     * Checks if a user is attempting to send a friend request to themselves.
     * 
     * @param requester the user sending the friend request.
     * @param receiver the user receiving the friend request.
     * @return true if the requester and receiver are the same user, false otherwise.
     */
    public boolean isSelfRequest(User requester, User receiver) {
        return requester.getUserId().equals(receiver.getUserId());
    }

    /**
     * Checks if the user is viewing their own profile.
     * 
     * @param userId the ID of the user viewing the profile.
     * @param friendId the ID of the profile being viewed.
     * @return true if the user is viewing their own profile, false otherwise.
     */
    public boolean isSelfProfile(Long userId, Long friendId) {
        return userId.equals(friendId);
    }

    /**
     * Retrieves all friends of a specific user.
     * 
     * @param user the user whose friends are to be fetched.
     * @return a list of the user's friends.
     */
    public List<User> getAllFriendsOfUser(User user) {
        return FriendshipUtils.getAllFriendsOfUser(em, user);
    }

    /**
     * Maps a list of friends to a list of maps containing user info.
     * 
     * @param friends the list of friends to map.
     * @return a list of maps, each containing user info.
     */
    public List<Map<String, Object>> mapFriendInfos(List<User> friends) {
        List<Map<String, Object>> friendInfos = friends.stream().map(friend -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", friend.getUserId());
            map.put("firstName", friend.getFirstName());
            map.put("lastName", friend.getLastName());
            map.put("bio", friend.getBio());
            map.put("birthdate", friend.getBirthdate());
            return map;
        }).collect(Collectors.toList());
        return friendInfos;
    }

    /**
     * Maps a single friend's information to a map.
     * 
     * @param friend the friend whose information is to be mapped.
     * @return a map containing the friend's information.
     */
    public Map<String, Object> mapFriendInfo(User friend) {
        Map<String, Object> friendInfo = new HashMap<>();
        friendInfo.put("id", friend.getUserId());
        friendInfo.put("firstName", friend.getFirstName());
        friendInfo.put("lastName", friend.getLastName());
        friendInfo.put("bio", friend.getBio());
        friendInfo.put("birthdate", friend.getBirthdate());
        return friendInfo;
    }
}
