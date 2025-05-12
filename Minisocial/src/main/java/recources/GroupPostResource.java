package recources;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import models.GroupPost;
import models.GroupPostDTO;
import service.GroupPostService;

@Path("/groupPosts")
@Produces("application/json")
@Consumes("application/json")
public class GroupPostResource {

    @Inject
    GroupPostService groupPostService;

    // Method to create a GroupPost for a user within a group
    @POST
    @Path("/createGroupPost/{userId}/{groupId}")
    public Response createGroupPostForUser(@PathParam("userId") Long userId, 
                                           @PathParam("groupId") Long groupId, 
                                           GroupPostDTO groupPostDTO) {
        try {
            GroupPost groupPost = new GroupPost();
            groupPost.setContent(groupPostDTO.getContent());
            groupPost.setImageUrl(groupPostDTO.getImageUrl());

            String result = groupPostService.createGroupPost(userId, groupPost, groupId);

            if (result.contains("validation failed") || result.contains("does not exist") || result.contains("error")) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(result)  // Return the exact message from the service
                               .build();
            }

            GroupPostDTO createdGroupPostDTO = GroupPostDTO.fromGroupPost(groupPost);
            return Response.status(Response.Status.CREATED)
                           .entity(createdGroupPostDTO)
                           .build();
        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred while creating the group post.")
                           .build();
        }
    }

    // Method to edit an existing group post
    @PUT
    @Path("/edit/{userId}/{postId}")
    public Response editGroupPost(@PathParam("userId") Long userId, 
                                  @PathParam("postId") int postId, 
                                  GroupPostDTO groupPostDTO) {
        try {
            String result = groupPostService.editGroupPost(userId, postId, groupPostDTO.getContent(), groupPostDTO.getImageUrl());

            if (result.contains("validation failed") || result.contains("does not exist")) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(result) 
                               .build();
            }

            return Response.status(Response.Status.OK)
                           .entity(result)
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred while editing the group post.")
                           .build();
        }
    }

    // Method to get all group posts by groupId
    @GET
    @Path("/{groupId}")
    public Response getAllGroupPostsByGroup(@PathParam("groupId") Long groupId) {
        try {
            List<GroupPostDTO> groupPosts = groupPostService.getAllGroupPosts(groupId);

            if (groupPosts == null || groupPosts.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("No posts found for group with ID: " + groupId)
                               .build();
            }

            return Response.status(Response.Status.OK)
                           .entity(groupPosts)
                           .build();
        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred while fetching group posts.")
                           .build();
        }
    }

    // Method to delete a group post
    @DELETE
    @Path("/delete/{userId}/{postId}")
    public Response deleteGroupPost(@PathParam("userId") Long userId, @PathParam("postId") int postId) {
        try {
            String result = groupPostService.deleteGroupPost(userId, postId);

            if (result.contains("not found") || result.contains("does not have permission")) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(result)  // Return the exact message from the service
                               .build();
            }

            return Response.status(Response.Status.OK)
                           .entity(result)  // Success message
                           .build();
        } catch (Exception e) {
            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred while deleting the group post.")
                           .build();
        }
    }

    
    

}
