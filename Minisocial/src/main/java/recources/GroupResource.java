package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.GroupService;
import service.UserService;
import models.Group;
import models.User;
import models.GroupMembership;
import java.util.List;
import models.GroupCreationDTO;
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
    @Inject
    private GroupService groupService;

    @Inject
    private UserService userService;

    public GroupResource() {
    }
    
    @POST
    @Path("/create/{creatorId}")
    public Response createGroup(GroupCreationDTO request, @PathParam("creatorId") Long creatorId) {
        // verify that the user exists
        User creator = userService.getUserById(creatorId);
        if (creator == null) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity("User not found")
                         .build();
        }
        
        // Input validation
        if (request.getName() == null ) {
            return Response.status(Response.Status.BAD_REQUEST)
                         .entity("Group name cannot be empty")
                         .build();
        }
        
        Group group = groupService.createGroup(
            request.getName(),
            request.getDescription(),
            request.isOpen(),
            creator
        );
        
        return Response.status(Response.Status.CREATED)
                      .entity(group)
                      .build();
    }
   
    @POST
    @Path("/{groupId}/join/{userId}")
    public Response joinGroup(
        @PathParam("groupId") Long groupId, 
        @PathParam("userId") Long userId
    ) {
        GroupMembership membership = groupService.requestToJoinGroup(groupId, userId);
        
        return Response.status(Response.Status.CREATED)
                      .entity(membership)
                      .build();
    }

    @PUT
    @Path("/{groupId}/memberships/{membershipId}/approve/{adminId}")
    public Response approveMembershipRequest(
        @PathParam("groupId") Long groupId,
        @PathParam("membershipId") Long membershipId,
        @PathParam("adminId") Long adminId
    ) {
        GroupMembership membership = groupService.handleMembershipRequest(
            membershipId,
            adminId,
            true
        );
        
        return Response.ok(membership).build();
    }
    
    @PUT
    @Path("/{groupId}/memberships/{membershipId}/reject/{adminId}")
    public Response rejectMembershipRequest(
        @PathParam("groupId") Long groupId,
        @PathParam("membershipId") Long membershipId,
        @PathParam("adminId") Long adminId
    ) {
        GroupMembership membership = groupService.handleMembershipRequest(
            membershipId,
            adminId,
            false
        );
        
        return Response.ok(membership).build();
    }
    
    @GET
    @Path("/{groupId}/pending-requests/{adminId}")
    public Response getPendingRequests(
        @PathParam("groupId") Long groupId,
        @PathParam("adminId") Long adminId
    ) {
        User admin = new User();
        admin.setUserId(adminId);
        
        List<GroupMembership> requests = groupService.getPendingRequests(groupId, admin);
        
        if (requests.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                         .entity("No pending requests found.")
                         .build();
        }
        
        return Response.ok(requests).build();
    }
  
    @PUT
    @Path("/{groupId}/promote/{userId}/by/{adminId}")
    public Response promoteToAdmin(
        @PathParam("groupId") Long groupId,
        @PathParam("userId") Long userId,
        @PathParam("adminId") Long adminId
    ) {
        GroupMembership membership = groupService.promoteToAdmin(groupId, userId, adminId);
        return Response.ok(membership).build();
    }
   
    @DELETE
    @Path("/{groupId}/members/{userId}/remove/{adminId}")
    public Response removeUserFromGroup(
        @PathParam("groupId") Long groupId,
        @PathParam("userId") Long userId,
        @PathParam("adminId") Long adminId
    ) {
        groupService.removeUserFromGroup(groupId, userId, adminId);
        return Response.noContent().build();
    }
  
    @DELETE
    @Path("/{groupId}/posts/{postId}/remove/by/{adminId}")
    public Response removePostFromGroup(
        @PathParam("groupId") Long groupId,
        @PathParam("postId") Long postId,
        @PathParam("adminId") Long adminId
    ) {
        groupService.removePostFromGroup(groupId, postId, adminId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{groupId}/delete/{adminId}")
    public Response deleteGroup(
        @PathParam("groupId") Long groupId,
        @PathParam("adminId") Long adminId
    ) {
        groupService.deleteGroup(groupId, adminId);
        return Response.noContent().build();
    }
  
    @DELETE
    @Path("/{groupId}/leave/{userId}")
    public Response leaveGroup(
        @PathParam("groupId") Long groupId,
        @PathParam("userId") Long userId
    ) {
        groupService.leaveGroup(groupId, userId);
        return Response.noContent().build();
    }
}