package recources;

import enums.FriendshipStatus;
import models.FriendshipRequests;
import models.User;
import service.FriendshipService;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/friendships")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FriendshipResource {

    @Inject
    private FriendshipService friendshipService;

    @Inject
    private UserService userService;

    @POST
    @Path("/{requesterId}/request/{receiverId}")
    public Response sendFriendRequest(@PathParam("requesterId") Long requesterId,
                                      @PathParam("receiverId") Long receiverId) {
        User requester = userService.getUserById(requesterId);
        User receiver = userService.getUserById(receiverId);

        if (requester == null || receiver == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }

        boolean success = friendshipService.sendFriendRequest(requester, receiver);
        if (!success) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Friend request already exists or users are already friends.")
                           .build();
        }

        return Response.status(Response.Status.CREATED)
                       .entity("Friend request sent successfully.")
                       .build();
    }

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
}
