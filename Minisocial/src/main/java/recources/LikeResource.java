package recources;

import models.Like;
import service.LikeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import dtos.LikeDTO;

import java.util.List;

/**
 * LikeResource provides REST endpoints for managing 'Like' actions in the application.
 * It allows users to like both UserPosts and GroupPosts, as well as retrieve the likes
 * for a specific post.
 */
@Path("/likes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LikeResource {

    // Injecting the LikeService to handle business logic
    @Inject
    private LikeService likeService;

    // ➡️ Like a UserPost
    /**
     * Allows a user to like a specific UserPost.
     *
     * @param postId the ID of the UserPost to like
     * @param userId the ID of the user who is liking the post
     * @return a Response indicating success or failure
     */
    @POST
    @Path("/userpost/{postId}/{userId}")
    public Response likeUserPost(@PathParam("postId") int postId, @PathParam("userId") Long userId) {
        try {
            // Attempt to like the UserPost
            List<String> errors = likeService.likeUserPost(userId, postId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }

            // Return success response if no errors
            return Response.status(Response.Status.CREATED)
                           .entity("User post liked successfully.")
                           .build();

        } catch (Exception e) {
            // Log and return failure response in case of error
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to like user post: " + e.getMessage())
                           .build();
        }
    }

    // ➡️ Like a GroupPost (with DTO Mapping)
    /**
     * Allows a user to like a specific GroupPost within a group.
     *
     * @param groupId the ID of the group containing the post
     * @param postId  the ID of the GroupPost to like
     * @param userId  the ID of the user who is liking the post
     * @return a Response indicating success or failure
     */
    @POST
    @Path("/group/{groupId}/{postId}/{userId}")
    public Response likeGroupPost(@PathParam("groupId") Long groupId, @PathParam("postId") int postId, @PathParam("userId") Long userId) {
        try {
            // Attempt to like the GroupPost
            List<String> errors = likeService.likeGroupPost(userId, postId, groupId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }

            // Return success response if no errors
            return Response.status(Response.Status.CREATED)
                           .entity("Group post liked successfully.")
                           .build();

        } catch (Exception e) {
            // Log and return failure response in case of error
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to like group post: " + e.getMessage())
                           .build();
        }
    }

    // ➡️ Get Likes for a UserPost (with DTO Mapping)
    /**
     * Retrieves all likes for a specific UserPost.
     *
     * @param postId the ID of the UserPost to fetch likes for
     * @return a Response containing the list of likes or an error message
     */
    @GET
    @Path("/userpost/{postId}")
    public Response getLikesForUserPost(@PathParam("postId") int postId) {
        try {
            // Fetch likes for the specified UserPost
            List<Like> likes = likeService.getLikesForUserPost(postId);

            if (likes.isEmpty()) {
                // Return message if no likes exist for the post
                return Response.status(Response.Status.OK)
                               .entity("No likes for this user post.")
                               .build();
            }

            // Convert Like entities to DTOs
            List<LikeDTO> likeDTOs = LikeDTO.fromLikeList(likes);

            // Return the list of likes as a response
            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            // Log and return failure response in case of error
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch likes for user post: " + e.getMessage())
                           .build();
        }
    }

    // ➡️ Get Likes for a GroupPost (with DTO Mapping)
    /**
     * Retrieves all likes for a specific GroupPost within a group.
     *
     * @param groupId the ID of the group containing the post
     * @param postId  the ID of the GroupPost to fetch likes for
     * @return a Response containing the list of likes or an error message
     */
    @GET
    @Path("/group/{groupId}/{postId}")
    public Response getLikesForGroupPost(@PathParam("groupId") Long groupId, @PathParam("postId") int postId) {
        try {
            // Fetch likes for the specified GroupPost
            List<Like> likes = likeService.getLikesForGroupPost(postId, groupId);

            if (likes.isEmpty()) {
                // Return message if no likes exist for the post
                return Response.status(Response.Status.OK)
                               .entity("No likes for this group post.")
                               .build();
            }

            // Convert Like entities to DTOs
            List<LikeDTO> likeDTOs = LikeDTO.fromLikeList(likes);

            // Return the list of likes as a response
            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            // Log and return failure response in case of error
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch likes for group post: " + e.getMessage())
                           .build();
        }
    }
}
