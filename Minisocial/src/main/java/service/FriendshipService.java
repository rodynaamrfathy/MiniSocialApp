package service;

import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.Friendships;
import models.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class FriendshipService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    // Send Friend Request
    public boolean sendFriendRequest(User requester, User receiver) {
        if (isAlreadyFriends(requester, receiver) || hasPendingRequest(requester, receiver)) {
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

    // Accept Friend Request
    public boolean acceptFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.ACCEPTED);

            createMutualFriendship(request.getRequester(), request.getReceiver());

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

    // Get all pending friendship requests for a user
    public List<String> getAllPendingRequests(Long userId) {
        List<FriendshipRequests> requests = em.createQuery(
            "SELECT r FROM FriendshipRequests r WHERE r.receiver.id = :userId AND r.status = :status",
            FriendshipRequests.class)
            .setParameter("userId", userId)
            .setParameter("status", FriendshipStatus.PENDING)
            .getResultList();

        List<String> result = new ArrayList<>();
        for (FriendshipRequests request : requests) {
            result.add(request.toString());
        }
        return result;
    }

    // Get request by ID
    public FriendshipRequests getRequestById(int requestId) {
        return em.find(FriendshipRequests.class, requestId);
    }

    public String getRequestStringById(int requestId) {
        FriendshipRequests request = em.createQuery(
                "SELECT r FROM FriendshipRequests r WHERE r.friendship_request_id = :requestId",
                FriendshipRequests.class)
                .setParameter("requestId", requestId)
                .getSingleResult();

        return request != null ? request.toString() : "FriendshipRequest not found";
    }

    // ========== Internal Helpers ==========

    private boolean isAlreadyFriends(User user1, User user2) {
        Long count = em.createQuery(
            "SELECT COUNT(f) FROM Friendships f WHERE " +
            "(f.user = :user1 AND f.friend = :user2) OR (f.user = :user2 AND f.friend = :user1)",
            Long.class)
            .setParameter("user1", user1)
            .setParameter("user2", user2)
            .getSingleResult();
        return count > 0;
    }

    private boolean hasPendingRequest(User requester, User receiver) {
        Long count = em.createQuery(
            "SELECT COUNT(r) FROM FriendshipRequests r WHERE " +
            "r.requester = :requester AND r.receiver = :receiver AND r.status = :status",
            Long.class)
            .setParameter("requester", requester)
            .setParameter("receiver", receiver)
            .setParameter("status", FriendshipStatus.PENDING)
            .getSingleResult();
        return count > 0;
    }

    private void createMutualFriendship(User user1, User user2) {
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
}
