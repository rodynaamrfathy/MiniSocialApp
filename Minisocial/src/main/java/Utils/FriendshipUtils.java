package Utils;

import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.Friendships;
import models.User;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FriendshipUtils provides utility methods for managing friendships and friendship requests.
 * It encapsulates JPQL queries and business logic related to sending requests, checking friendships,
 * creating mutual friendships, and suggesting friends based on mutual connections.
 */
public class FriendshipUtils {

    // ===================== JPQL Queries =====================

    /** Query to get all pending friendship requests for a user */
    public static final String GET_PENDING_REQUESTS_QUERY =
            "SELECT r FROM FriendshipRequests r WHERE r.receiver.id = :userId AND r.status = :status";

    /** Query to check if two users are already friends */
    public static final String IS_ALREADY_FRIENDS_QUERY =
            "SELECT COUNT(f) FROM Friendships f WHERE " +
            "(f.user = :user1 AND f.friend = :user2) OR (f.user = :user2 AND f.friend = :user1)";

    /** Query to check if a pending request already exists between two users */
    public static final String HAS_PENDING_REQUEST_QUERY =
            "SELECT COUNT(r) FROM FriendshipRequests r WHERE " +
            "r.requester = :requester AND r.receiver = :receiver AND r.status = :status";

    /** Query to retrieve all friends of a user */
    public static final String GET_ALL_FRIENDS_QUERY =
            "SELECT f.friend FROM Friendships f WHERE f.user = :user";

    /** 
     * Query to suggest friends based on mutual connections.
     * Excludes current friends and the user themselves.
     */
    public static final String SUGGEST_FRIENDS_QUERY =
            "SELECT u FROM User u WHERE u.id <> :userId " +
            "AND u.id NOT IN (" +
                "SELECT f.friend.id FROM Friendships f WHERE f.user.id = :userId" +
            ") AND u.id IN (" +
                "SELECT f2.friend.id FROM Friendships f1 " +
                "JOIN Friendships f2 ON f1.friend.id = f2.user.id " +
                "WHERE f1.user.id = :userId AND f2.friend.id <> :userId " +
            ")";

    // ===================== Utility Logic =====================

    /**
     * Suggests a list of users as potential friends based on mutual connections.
     *
     * @param em EntityManager instance
     * @param userId ID of the current user
     * @return List of suggested User entities
     */
    public static List<User> suggestFriends(EntityManager em, Long userId) {
        return em.createQuery(SUGGEST_FRIENDS_QUERY, User.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }

    /**
     * Sends a friend request from requester to receiver if conditions are met.
     *
     * @param em EntityManager instance
     * @param requester User sending the request
     * @param receiver User receiving the request
     * @return true if request was sent, false otherwise
     */
    public static boolean sendFriendRequest(EntityManager em, User requester, User receiver) {
        if (requester.getUserId().equals(receiver.getUserId())) {
            System.out.println("❌ You can't send a friend request to yourself.");
            return false;
        }

        if (isAlreadyFriends(em, requester, receiver) || hasPendingRequest(em, requester, receiver)) {
            return false;
        }

        FriendshipRequests request = new FriendshipRequests();
        request.setRequester(requester);
        request.setReceiver(receiver);
        request.setStatus(FriendshipStatus.PENDING);
        request.setTimeStamp(new Date());

        em.persist(request);
        return true;
    }

    /**
     * Checks if two users are already friends.
     *
     * @param em EntityManager instance
     * @param user1 First user
     * @param user2 Second user
     * @return true if they are friends, false otherwise
     */
    public static boolean isAlreadyFriends(EntityManager em, User user1, User user2) {
        Long count = em.createQuery(IS_ALREADY_FRIENDS_QUERY, Long.class)
                .setParameter("user1", user1)
                .setParameter("user2", user2)
                .getSingleResult();
        return count > 0;
    }

    /**
     * Checks if a pending friend request already exists between requester and receiver.
     *
     * @param em EntityManager instance
     * @param requester User who sent the request
     * @param receiver User who received the request
     * @return true if a pending request exists, false otherwise
     */
    public static boolean hasPendingRequest(EntityManager em, User requester, User receiver) {
        Long count = em.createQuery(HAS_PENDING_REQUEST_QUERY, Long.class)
                .setParameter("requester", requester)
                .setParameter("receiver", receiver)
                .setParameter("status", FriendshipStatus.PENDING)
                .getSingleResult();
        return count > 0;
    }

    /**
     * Creates a mutual friendship between two users by persisting two entries (bidirectional).
     *
     * @param em EntityManager instance
     * @param user1 First user
     * @param user2 Second user
     */
    public static void createMutualFriendship(EntityManager em, User user1, User user2) {
        Friendships f1 = new Friendships();
        f1.setUser(user1);
        f1.setFriend(user2);
        f1.setSince(new Date());
        em.persist(f1);

        Friendships f2 = new Friendships();
        f2.setUser(user2);
        f2.setFriend(user1);
        f2.setSince(new Date());
        em.persist(f2);
    }

    /**
     * Retrieves all pending friend requests for a user as string representations.
     *
     * @param em EntityManager instance
     * @param userId ID of the user
     * @return List of pending request strings
     */
    public static List<String> getAllPendingRequests(EntityManager em, Long userId) {
        List<FriendshipRequests> requests = em.createQuery(GET_PENDING_REQUESTS_QUERY, FriendshipRequests.class)
                .setParameter("userId", userId)
                .setParameter("status", FriendshipStatus.PENDING)
                .getResultList();

        return requests.stream().map(FriendshipRequests::toString).collect(Collectors.toList());
    }

    /**
     * Retrieves all friends of a given user.
     *
     * @param em EntityManager instance
     * @param user User entity
     * @return List of User entities who are friends of the given user
     */
    public static List<User> getAllFriendsOfUser(EntityManager em, User user) {
        return em.createQuery(GET_ALL_FRIENDS_QUERY, User.class)
                .setParameter("user", user)
                .getResultList();
    }
}
