package recources;

import enums.FriendshipStatus;
import messaging.NotificationEvent;
import messaging.NotificationProducer;
import models.FriendshipRequests;
import models.User;
import service.FriendshipService;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

/**
 * FriendshipResource – Handles friendship-related REST end points for managing friend requests, suggestions, and friendships.
 * 
 * This class exposes several RESTful web services for managing friendships:
 *   - Send, accept, reject friend requests.
 *   - Get pending requests and friend suggestions.
 *   - Retrieve all friends and friend profiles.
 * 
 * Key Responsibilities:
 *   - Sends friend requests and handles acceptance/rejection.
 *   - Suggests friends based on the user’s network.
 *   - Manages friend profiles with visibility checks.
 */
@Path("/friendships")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FriendshipResource {

    /** Service for managing friendships */
    @Inject
    private FriendshipService friendshipService;

    /** 🧑‍🤝‍🧑 Service for managing users */
    @Inject
    private UserService userService;
    
    /** Producer for sending notification events */
    @Inject
    private NotificationProducer notificationProducer;
    
    /**
     * Get suggested friends for a user.
     * 
     * This method returns a list of users who are recommended as potential friends for the given user.
     * 
     * @param userId The ID of the user requesting friend suggestions.
     * @return Response containing the list of suggested friends or a message if no suggestions exist.
     */
    @GET
    @Path("/{userId}/suggestions")
    public Response getSuggestedFriends(@PathParam("userId") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }

        List<User> suggestedUsers = friendshipService.suggestFriends(userId);
        if (suggestedUsers.isEmpty()) {
            return Response.ok("No friend suggestions found.").build();
        }

        List<Map<String, Object>> suggestedInfos = friendshipService.mapFriendInfos(suggestedUsers);
        return Response.ok(suggestedInfos).build();
    }

    /**
     * Send a friend request from one user to another.
     * 
     * This method initiates a friend request from the requester to the receiver.
     * It also sends a notification to the receiver about the request.
     * 
     * @param requesterId The ID of the user sending the request.
     * @param receiverId The ID of the user receiving the request.
     * @return Response indicating whether the request was successfully sent.
     */
    @POST
    @Path("/{requesterId}/request/{receiverId}")
    public Response sendFriendRequest(@PathParam("requesterId") Long requesterId,
                                      @PathParam("receiverId") Long receiverId) {
        User requester = userService.getUserById(requesterId);
        User receiver = userService.getUserById(receiverId);

        if (requester == null || receiver == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }

        if (friendshipService.isSelfRequest(requester, receiver)) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("🚫 You can't send a friend request to yourself.").build();
        }

        boolean success = friendshipService.sendFriendRequest(requester, receiver);
        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Friend request already exists or users are already friends.")
                           .build();
        }

        // Send a notification to the receiver about the new friend request
        String message = requester.getFirstName() + " " + requester.getLastName() + " sent you a friend request.";
        NotificationEvent event = new NotificationEvent(
            requester.getUserId(),
            receiver.getUserId(),
            "FRIEND_REQUEST_RECEIVED",
            message
        );
        notificationProducer.sendNotification(event);

        return Response.status(Response.Status.CREATED)
                       .entity("Friend request sent successfully.")
                       .build();
    }

    /**
     * Accept a pending friend request.
     * 
     * This method accepts a pending friend request by updating its status.
     * 
     * @param requestId The ID of the pending friend request to accept.
     * @return Response indicating the success or failure of the request acceptance.
     */
    @POST
    @Path("/accept/{requestId}")
    public Response acceptFriendRequest(@PathParam("requestId") int requestId) {
        FriendshipRequests request = friendshipService.getRequestById(requestId);

        if (request == null || request.getStatus() != FriendshipStatus.PENDING) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid or non-pending friend request.")
                           .build();
        }

        boolean success = friendshipService.acceptFriendRequest(request);
        return success ? Response.ok("Friend request accepted.").build()
                       : Response.status(Response.Status.BAD_REQUEST)
                                 .entity("Failed to accept friend request.")
                                 .build();
    }

    /**
     * Reject a pending friend request.
     * 
     * This method rejects a pending friend request by updating its status.
     * 
     * @param requestId The ID of the pending friend request to reject.
     * @return Response indicating the success or failure of the request rejection.
     */
    @POST
    @Path("/reject/{requestId}")
    public Response rejectFriendRequest(@PathParam("requestId") int requestId) {
        FriendshipRequests request = friendshipService.getRequestById(requestId);

        if (request == null || request.getStatus() != FriendshipStatus.PENDING) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid or non-pending friend request.")
                           .build();
        }

        boolean success = friendshipService.rejectFriendRequest(request);
        return success ? Response.ok("Friend request rejected.").build()
                       : Response.status(Response.Status.BAD_REQUEST)
                                 .entity("Failed to reject friend request.")
                                 .build();
    }

    /**
     * Get all pending friendship requests for a user.
     * 
     * This method retrieves all pending friendship requests for a user.
     * 
     * @param userId The ID of the user whose pending requests are being queried.
     * @return Response containing a list of pending requests or a message if no pending requests exist.
     */
    @GET
    @Path("/{userId}/pending")
    public Response getAllFriendshipRequests(@PathParam("userId") Long userId) {
        List<String> friendshipRequests = friendshipService.getAllPendingRequests(userId);

        if (friendshipRequests.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No pending friendship requests found.")
                           .build();
        }

        return Response.ok(friendshipRequests).build();
    }

    /**
     * Get a specific friendship request by its ID.
     * 
     * This method retrieves the details of a friendship request by its ID.
     * 
     * @param requestId The ID of the friendship request to retrieve.
     * @return Response containing the friendship request details or an error message.
     */
    @GET
    @Path("/request/{requestId}")
    public Response getFriendshipRequestById(@PathParam("requestId") int requestId) {
        String request = friendshipService.getRequestStringById(requestId);

        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Friendship request not found.")
                           .build();
        }

        return Response.ok(request).build();
    }

    /**
     * Get all friends of a user.
     * 
     * This method retrieves the list of all friends for a given user.
     * 
     * @param userId The ID of the user whose friends are being queried.
     * @return Response containing the list of friends or an error message.
     */
    @GET
    @Path("/{userId}/friends")
    public Response getAllFriends(@PathParam("userId") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found.")
                           .build();
        }

        List<User> friends = friendshipService.getAllFriendsOfUser(user);
        List<Map<String, Object>> friendInfos = friendshipService.mapFriendInfos(friends);

        return Response.ok(friendInfos).build();
    }

    /**
     * Get the profile of a friend.
     * 
     * This method retrieves the profile information of a friend if the user is friends with them.
     * 
     * @param userId The ID of the user requesting the friend profile.
     * @param friendId The ID of the friend whose profile is being requested.
     * @return Response containing the friend profile or an error message.
     */
    @GET
    @Path("/friendProfile/{userId}/{friendId}")
    public Response getFriendProfile(@PathParam("userId") Long userId,
                                     @PathParam("friendId") Long friendId) {
        User user = userService.getUserById(userId);
        User friend = userService.getUserById(friendId);

        if (friendshipService.isSelfProfile(userId, friendId)) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("You can't view your own profile through this endpoint.")
                           .build();
        }

        if (user == null || friend == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User or friend not found.")
                           .build();
        }

        if (!friendshipService.isAlreadyFriends(user, friend)) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("You are not friends with this user.")
                           .build();
        }

        Map<String, Object> friendInfo = friendshipService.mapFriendInfo(friend);
        return Response.ok(friendInfo).build();
    }
}
