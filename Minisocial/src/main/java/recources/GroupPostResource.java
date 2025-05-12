package recources;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import dtos.GroupPostDTO;
import models.Group;
import models.GroupPost;
import service.GroupPostService;

/**
 * GroupPostResource class provides RESTful end points for managing group posts.
 * Includes operations for creating, editing, retrieving, and deleting group posts.
 */
@Path("/groupPosts")
@Produces("application/json")
@Consumes("application/json")
public class GroupPostResource {

    @Inject
    GroupPostService groupPostService;

    /**
     * Creates a group post for a user within a group.
     * 
     * @param userId The ID of the user creating the post
     * @param groupId The ID of the group where the post is being created
     * @param groupPostDTO The DTO containing the content and image URL of the post
     * @return A response containing the created group post or an error message
     */
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
                               .entity(result)
                               .build();
            }

            return Response.status(Response.Status.CREATED)
                           .entity("Post Created Successfully!")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("An error occurred while creating the group post.")
                           .build();
        }
    }

    /**
     * Edits an existing group post.
     * 
     * @param userId The ID of the user editing the post
     * @param postId The ID of the post being edited
     * @param groupPostDTO The DTO containing the updated content and image URL of the post
     * @return A response containing the result of the edit operation or an error message
     */
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

    /**
     * Retrieves all group posts for a specific group.
     * 
     * @param userid The ID of the User which will view the group time line and should be a member 
     * @param groupId The ID of the group whose posts are being retrieved
     * @return A response containing a list of group posts or an error message
     */
    @GET
    @Path("/grouptimeline/{userId}/{groupId}")
    public Response getAllGroupPostsByGroup(@PathParam("userId") Long userId, 
                                             @PathParam("groupId") Long groupId) {
        try {
            // Check if Group Exists
            Group group = groupPostService.findGroupById(groupId);
            if (group == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Group with ID " + groupId + " does not exist.")
                               .build();
            }

            // Check if the user is a member of the group
            boolean isMember = groupPostService.isUserMemberOfGroup(userId, groupId);

            if (!isMember) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("User is not a member of the group. Please join the group first.")
                               .build();
            }

            // Retrieve the posts
            List<GroupPostDTO> groupPosts = groupPostService.getAllGroupPosts(groupId);

            if (groupPosts == null || groupPosts.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("No posts found for group with ID: " + groupId)
                               .build();
            }

            // Return found posts
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


    /**
     * Deletes a group post.
     * 
     * @param userId The ID of the user attempting to delete the post
     * @param postId The ID of the post being deleted
     * @return A response containing the result of the delete operation or an error message
     */
    @DELETE
    @Path("/delete/{userId}/{postId}")
    public Response deleteGroupPost(@PathParam("userId") Long userId, @PathParam("postId") int postId) {
        try {
            String result = groupPostService.deleteGroupPost(userId, postId);

            if (result.contains("not found") || result.contains("does not have permission")) {
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
                           .entity("An error occurred while deleting the group post.")
                           .build();
        }
    }
}
