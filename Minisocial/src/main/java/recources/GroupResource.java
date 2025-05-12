package recources;

import models.GroupDTO;
import models.GroupMembership;
import models.GroupMembershipDTO;
import service.GroupService;
import service.GroupService.GroupCreationResult;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

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
            GroupCreationResult result = groupService.createGroup(groupDTO, adminId);
            if (result.errors != null && !result.errors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errors", result.errors);
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            return Response.status(Response.Status.CREATED).entity(result.groupDTO).build();
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", Collections.singletonList("Something went wrong: " + e.getMessage()));
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
        }
    }

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
}
