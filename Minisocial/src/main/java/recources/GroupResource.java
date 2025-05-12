package recources;

import models.GroupMembership;
import service.GroupService;
import service.GroupService.GroupCreationResult;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import dtos.GroupDTO;
import dtos.GroupMembershipDTO;

import java.util.*;

/**
 * GroupResource
 *
 * RESTful resource for managing group-related operations.
 * Exposes end points for:
 * - Creating groups with admin assignment
 * - Retrieving approved group members
 *
 * Works with GroupService to handle business logic.
 */
@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupResource {

    /**
     * Injected instance of GroupService to perform group operations.
     */
    @Inject
    private GroupService groupService;

    /**
     * End point to create a new group and assign an admin.
     *
     * 🔗 URL: POST /groups/create/{adminId}
     *
     * @param groupDTO The DTO containing group details (name, description, isOpen).
     * @param adminId  The ID of the user to be assigned as group admin.
     * @return Response with created group or validation errors.
     */
    @POST
    @Path("/create/{adminId}")
    public Response createGroup(GroupDTO groupDTO, @PathParam("adminId") Long adminId) {
        try {
            GroupCreationResult result = groupService.createGroup(groupDTO, adminId);

        
            if (result.errors != null && !result.errors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errors", result.errors);
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

         
            return Response.status(Response.Status.CREATED).entity(result.groupDTO).build();

        } catch (Exception e) {
            // Handle unexpected exceptions
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", Collections.singletonList("Something went wrong: " + e.getMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

    /**
     * End point to retrieve all approved members of a group.
     *
     * URL: GET /groups/members/{groupId}
     *
     * @param groupId The ID of the group.
     * @return Response with list of approved GroupMembershipDTOs or error if none found.
     */
    @GET
    @Path("/members/{groupId}")
    public Response getAllApprovedMembersInGroup(@PathParam("groupId") Long groupId) {
        List<GroupMembership> approvedMemberships = groupService.getApprovedMembershipsByGroupId(groupId);

        if (approvedMemberships.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Collections.singletonMap("errors", Collections.singletonList("No members found for the group with ID: " + groupId)))
                    .build();
        }

     
        List<GroupMembershipDTO> dtoList = GroupMembershipDTO.fromEntityList(approvedMemberships);
        return Response.ok(dtoList).build();
    }
    
    @PUT
    @Path("/{groupId}/promote/{adminId}/{targetUserId}")
    public Response promoteToAdmin(@PathParam("adminId") Long adminId,
                                   @PathParam("targetUserId") Long targetUserId,
                                   @PathParam("groupId") Long groupId) {
        Map<String, Object> response = groupService.promoteToAdmin(adminId, targetUserId, groupId);
        if (response.containsKey("errors")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{groupId}/delete/{adminId}")
    public Response deleteGroup(@PathParam("adminId") Long adminId,
                                @PathParam("groupId") Long groupId) {
        Map<String, Object> response = groupService.deleteGroup(adminId, groupId);
        if (response.containsKey("errors")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{groupId}/remove/{adminId}/{targetUserId}")
    public Response removeUserFromGroup(@PathParam("adminId") Long adminId,
                                        @PathParam("targetUserId") Long targetUserId,
                                        @PathParam("groupId") Long groupId) {
        Map<String, Object> response = groupService.removeUserFromGroup(adminId, targetUserId, groupId);
        if (response.containsKey("errors")) {
            return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
        }
        return Response.ok(response).build();
    }

}
