package recources;

import service.GroupMembershipService;
import service.GroupService;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.NotificationEvent;
import messaging.NotificationProducer;
import models.User;

import java.util.Collections;
import java.util.List;

@Path("/group-memberships")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupMembershipResource {

    @Inject
    private GroupMembershipService service;

    @Inject
    private UserService userService;

    @Inject
    private GroupService groupService;

    @Inject
    private NotificationProducer notificationProducer;


    @POST
    @Path("/join/{userId}/{groupId}")
    public Response joinGroup(@PathParam("userId") Long userId, @PathParam("groupId") Long groupId) {
        Object result = service.requestToJoinGroup(userId, groupId);
        if (result instanceof List && !((List<?>) result).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        
        // send notification to group admin
        // Notify all group admins
        User requester = userService.getUserById(userId);
        groupService.getApprovedMembershipsByGroupId(groupId).stream()
            .filter(m -> m.getRole().equalsIgnoreCase("admin"))
            .forEach(admin -> {
                String msg = requester.getFirstName() + " requested to join your group.";
                notificationProducer.sendNotification(new NotificationEvent(
                    requester.getUserId(), admin.getUser().getUserId(), "GROUP_JOIN_REQUEST", msg
                ));
            });
        
        return Response.ok(result).build();
    }

    @GET
    @Path("/pending/{groupId}")
    public Response getPendingRequests(@PathParam("groupId") Long groupId) {
        Object result = service.getPendingRequestsForGroup(groupId);
        if (result instanceof List && !((List<?>) result).isEmpty() && ((List<?>) result).get(0) instanceof String) {
            return Response.status(Response.Status.NOT_FOUND).entity(result).build();
        }
        
        return Response.ok(result).build();
    }

    @POST
    @Path("/respond/{groupId}/{userId}/{approve}")
    public Response respondToRequest(@PathParam("groupId") Long groupId,
                                     @PathParam("userId") Long userId,
                                     @PathParam("approve") boolean approve) {
        Object result = service.respondToRequest(groupId, userId, approve);
        if (result instanceof List && !((List<?>) result).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
        
        // on approve send notification to user
        // Notify the user who sent the join request
        User user = userService.getUserById(userId);
        String msg = approve ? "Your request to join the group has been approved." :
                               "Your request to join the group was rejected.";
        notificationProducer.sendNotification(new NotificationEvent(
            null, userId, "GROUP_JOIN_RESPONSE", msg
        ));
        
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/leave/{userId}/{groupId}")
    public Response leaveGroup(@PathParam("userId") Long userId, @PathParam("groupId") Long groupId) {
        List<String> errors = service.leaveGroup(userId, groupId);
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        
        // on user leave send notification to admin 
        // Notify all group admins
        User user = userService.getUserById(userId);
        groupService.getApprovedMembershipsByGroupId(groupId).stream()
            .filter(m -> m.getRole().equalsIgnoreCase("admin"))
            .forEach(admin -> {
                String msg = user.getFirstName() + " has left your group.";
                notificationProducer.sendNotification(new NotificationEvent(
                    userId, admin.getUser().getUserId(), "GROUP_LEFT", msg
                ));
            });
        
        return Response.ok(Collections.singletonMap("message", "Left the group")).build();
    }
}
