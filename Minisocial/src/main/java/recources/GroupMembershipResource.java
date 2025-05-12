package recources;

import service.GroupMembershipService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Path("/group-memberships")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupMembershipResource {

    @Inject
    private GroupMembershipService service;

    @POST
    @Path("/join/{userId}/{groupId}")
    public Response joinGroup(@PathParam("userId") Long userId, @PathParam("groupId") Long groupId) {
        Object result = service.requestToJoinGroup(userId, groupId);
        if (result instanceof List && !((List<?>) result).isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        }
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
        return Response.ok(result).build();
    }

    @DELETE
    @Path("/leave/{userId}/{groupId}")
    public Response leaveGroup(@PathParam("userId") Long userId, @PathParam("groupId") Long groupId) {
        List<String> errors = service.leaveGroup(userId, groupId);
        if (!errors.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        return Response.ok(Collections.singletonMap("message", "Left the group")).build();
    }
}
