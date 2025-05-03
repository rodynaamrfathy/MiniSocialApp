package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.User;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user) {
        List<String> validationErrors = userService.validateUser(user);

        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorResponse)
                           .type(MediaType.APPLICATION_JSON)
                           .build();
        }

        User createdUser = userService.saveUser(user);

        return Response.status(Response.Status.CREATED)
                       .entity(createdUser)
                       .build();
    }

    // Endpoint to send a friend request
    @POST
    @Path("/{requesterId}/friendRequest/{receiverId}")
    public Response sendFriendRequest(@PathParam("requesterId") Long requesterId, @PathParam("receiverId") Long receiverId) {
        User requester = userService.getUserById(requesterId);
        User receiver = userService.getUserById(receiverId);

        if (requester == null || receiver == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User not found.")
                           .build();
        }

        boolean success = userService.sendFriendRequest(requester, receiver);

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Friend request already exists or users are already friends.")
                           .build();
        }

        return Response.status(Response.Status.CREATED)
                       .entity("Friend request sent successfully.")
                       .build();
    }

    // Endpoint to accept a friend request
    @POST
    @Path("/acceptFriendRequest/{requestId}")
    public Response acceptFriendRequest(@PathParam("requestId") int requestId) {
        FriendshipRequests request = userService.getFriendshipRequestById(requestId);

        if (request == null || request.getStatus() != FriendshipStatus.PENDING) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid or non-pending friend request.")
                           .build();
        }

        boolean success = userService.acceptFriendRequest(request);

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to accept friend request.")
                           .build();
        }

        return Response.status(Response.Status.OK)
                       .entity("Friend request accepted.")
                       .build();
    }

    // Endpoint to reject a friend request
    @POST
    @Path("/rejectFriendRequest/{requestId}")
    public Response rejectFriendRequest(@PathParam("requestId") int requestId) {
        FriendshipRequests request = userService.getFriendshipRequestById(requestId);

        if (request == null || request.getStatus() != FriendshipStatus.PENDING) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid or non-pending friend request.")
                           .build();
        }

        boolean success = userService.rejectFriendRequest(request);

        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Failed to reject friend request.")
                           .build();
        }

        return Response.status(Response.Status.OK)
                       .entity("Friend request rejected.")
                       .build();
    }

    @GET
    @Path("/{userId}/friendRequests")
    public Response getAllFriendshipRequests(@PathParam("userId") Long userId) {
        // Get the list of friendship requests in string format
        List<String> friendshipRequests = userService.getAllFriendshipRequests(userId);

        if (friendshipRequests.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No pending friendship requests found.")
                           .build();
        }

        // Return the list of string representations of friendship requests
        return Response.ok(friendshipRequests).build();
    }


    @GET
    @Path("/friendRequests/{requestId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriendshipRequestById(@PathParam("requestId") int requestId) {
        // Get the friendship request string representation
        String request = userService.getFriendshipRequestById2(requestId);

        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Friendship request not found.")
                           .build();
        }

        // Return the string representation of the friendship request
        return Response.ok(request).build();
    }

    // Endpoint to get all users as string representations
    @GET
    @Path("/allUsers")  // Path changed from /all to /allUsers
    public Response getAllUsers() {
        List<String> users = userService.getAllUsers();
        return Response.ok(users).build(); // Return the list of users' string representations
    }

 // 
    @GET
    @Path("/allUsers/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("userId") Long userId) {
        // Call the service method to get the string representation of the user by ID
        List<String> userStrings = userService.getUserStringById(userId);

        // If the user was found, return their string representation
        if (userStrings.contains("User not found")) {
            // Return a 404 NOT FOUND if user is not found
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(userStrings.get(0))  // Return "User not found"
                           .build();
        }

        // Return the string representation of the user
        return Response.ok(userStrings.get(0)).build();  // Return the user's string representation
    }



}
