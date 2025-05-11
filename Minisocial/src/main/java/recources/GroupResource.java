package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import models.GroupDTO;
import models.GroupMembership;
import models.GroupMembershipDTO;
import service.GroupService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📦 GroupResource handles the RESTful API for group creation.
 * 
 * 🚀 Responsibilities:
 * - Handles creation of new groups
 * - Handles validation and error response formatting
 */
@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {

    @Inject
    private GroupService groupService;

    @POST
    @Path("/create/{adminId}")
    public Response createGroup(GroupDTO groupDTO, @PathParam("adminId") Long adminId) {
        try {
            // Try to create the group
            GroupDTO createdGroup = groupService.createGroup(groupDTO, adminId);
            return Response.status(Response.Status.CREATED)
                           .entity(createdGroup)
                           .build();
        } catch (WebApplicationException e) {
            // Return specific validation errors (Admin user not found or Group name conflict)
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)  // 400 Bad Request for validation errors
                           .entity(errorResponse)
                           .build();
        } catch (Exception e) {
            // Fallback for unforeseen errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", "Something went wrong: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(errorResponse)
                           .build();
        }
    }
    
    @GET
    @Path("/members/{groupId}")
    public Response getAllMembersInGroup(@PathParam("groupId") Long groupId) {
        // Retrieve all group memberships for the specified groupId
        List<GroupMembership> memberships = groupService.getGroupMembershipsByGroupId(groupId);
        
        // If no memberships are found, return a NOT_FOUND response
        if (memberships.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No members found for the group with ID: " + groupId)
                           .build();
        }

        // Convert the list of GroupMemberships to GroupMembershipDTOs
        List<GroupMembershipDTO> groupMembershipDTOs = new ArrayList<>();
        for (GroupMembership membership : memberships) {
            groupMembershipDTOs.add(GroupMembershipDTO.fromGroupMembership(membership));
        }

        // Return a successful response with the list of DTOs
        return Response.ok(groupMembershipDTOs).build();
    }

}
