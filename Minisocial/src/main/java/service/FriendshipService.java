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

@Stateless
public class FriendshipService {

    @PersistenceContext(unitName = "hello")
    private EntityManager em;

    public List<User> suggestFriends(Long userId) {
        return FriendshipUtils.suggestFriends(em, userId);
    }

    public boolean sendFriendRequest(User requester, User receiver) {
        return FriendshipUtils.sendFriendRequest(em, requester, receiver);
    }

    public boolean acceptFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.ACCEPTED);
            FriendshipUtils.createMutualFriendship(em, request.getRequester(), request.getReceiver());
            em.merge(request);
            return true;
        }
        return false;
    }

    public boolean rejectFriendRequest(FriendshipRequests request) {
        if (request.getStatus() == FriendshipStatus.PENDING) {
            request.setStatus(FriendshipStatus.REJECTED);
            em.merge(request);
            return true;
        }
        return false;
    }

    public List<String> getAllPendingRequests(Long userId) {
        return FriendshipUtils.getAllPendingRequests(em, userId);
    }

    public FriendshipRequests getRequestById(int requestId) {
        return em.find(FriendshipRequests.class, requestId);
    }

    public String getRequestStringById(int requestId) {
        FriendshipRequests request = em.find(FriendshipRequests.class, requestId);
        return request != null ? request.toString() : "FriendshipRequest not found";
    }

    public boolean isAlreadyFriends(User u1, User u2) {
        return FriendshipUtils.isAlreadyFriends(em, u1, u2);
    }

    public boolean isSelfRequest(User requester, User receiver) {
        return requester.getUserId().equals(receiver.getUserId());
    }

    public boolean isSelfProfile(Long userId, Long friendId) {
        return userId.equals(friendId);
    }

    public List<User> getAllFriendsOfUser(User user) {
        return FriendshipUtils.getAllFriendsOfUser(em, user);
    }

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
