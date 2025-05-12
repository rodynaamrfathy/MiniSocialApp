package recources;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

<<<<<<< HEAD
import dtos.UserPostDTO;
=======
import messaging.ActivityLogEvent;
import messaging.ActivityLogProducer;
>>>>>>> abd03d5ce0325ac573dac2762219035a663f4f3a
import models.UserPost;
import service.PostService;

/**
 * PostResource – RESTful API for handling user posts
 * 
 * This resource provides end points for creating, editing, deleting, and retrieving user posts
 * as well as retrieving a user's time line. It interacts with the PostService for business logic.
 * 
 * End points:
 *   - GET /posts/time line/{userId} – Get the user's time line of posts
 *   - GET /posts/{userId} – Get all posts made by a specific user
 *   - POST /posts/createPost/{userId} – Create a new post for a user
 *   - PUT /posts/edit/{userId}/{postId} – Edit an existing post
 *   - DELETE /posts/delete/{userId}/{postId} – Delete a specific post
 */

@Path("/posts")
@Produces("application/json")
@Consumes("application/json")
public class PostResource {

    
    @Inject
    PostService postService;

<<<<<<< HEAD
    /**
     * GET /posts/time line/{userId} - Retrieves the time line of posts for a specific user.
     * 
     * @param userId The ID of the user whose time line is being fetched.
     * @return A Response containing the list of user posts or an error message.
     */
=======
    
    @Inject
    private ActivityLogProducer activityLogProducer;
    

>>>>>>> abd03d5ce0325ac573dac2762219035a663f4f3a
    @GET
    @Path("/timeline/{userId}")
    public Response getUserTimeline(@PathParam("userId") Long userId) {
        try {
            
            List<UserPostDTO> userPosts = postService.getUserTimeline(userId);

            
            if (userPosts == null || userPosts.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No posts found for the user with ID: " + userId)
                        .build();
            }

            
            return Response.status(Response.Status.OK)
                    .entity(userPosts)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching the timeline feed.")
                    .build();
        }
    }

    /**
     * GET /posts/{userId} - Retrieves all posts made by a specific user.
     * 
     * @param userId The ID of the user whose posts are being fetched.
     * @return A Response containing the list of user posts or an error message.
     */
    @GET
    @Path("/{userId}")
    public Response getAllPostsByUser(@PathParam("userId") Long userId) {
        try {
            
            List<UserPostDTO> userPosts = postService.getAllPostsByUser(userId);

            
            if (userPosts == null || userPosts.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No posts found for the user with ID: " + userId)
                        .build();
            }

            
            return Response.status(Response.Status.OK)
                    .entity(userPosts)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while fetching posts.")
                    .build();
        }
    }

    /**
     * POST /posts/createPost/{userId} - Creates a new post for a specific user.
     * 
     * @param userId The ID of the user creating the post.
     * @param userPostDTO The DTO object containing the details of the post.
     * @return A Response indicating the result of the post creation.
     */
    @POST
    @Path("/createPost/{userId}")
    public Response createPostForUser(@PathParam("userId") Long userId, UserPostDTO userPostDTO) {
        try {
            
            UserPost userPost = new UserPost();
            userPost.setContent(userPostDTO.getContent());
            userPost.setImageUrl(userPostDTO.getImageUrl());

            
            String result = postService.createPostForUser(userId, userPost);

            
            if (result.contains("validation failed") || result.contains("does not exist") || result.contains("did not return a UserPost")) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(result)
                        .build();
            }

<<<<<<< HEAD
            

=======
            UserPostDTO createdPostDTO = UserPostDTO.fromUserPost(userPost);
			// log activity
            activityLogProducer.sendActivityLog(
            	    new ActivityLogEvent(userId, "Created a Post",  " ")
            	);
            
>>>>>>> abd03d5ce0325ac573dac2762219035a663f4f3a
            return Response.status(Response.Status.CREATED)
                    .entity("Post Created Successfully!")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred while creating the post.")
                    .build();
        }
    }

    /**
     * PUT /posts/edit/{userId}/{postId} - Edits an existing post made by a user.
     * 
     * @param userId The ID of the user editing the post.
     * @param postId The ID of the post being edited.
     * @param userPostDTO The DTO object containing the updated post details.
     * @return A Response indicating the result of the post editing.
     */
    @PUT
    @Path("/edit/{userId}/{postId}")
    public Response editPost(@PathParam("userId") Long userId,
                             @PathParam("postId") int postId,
                             UserPostDTO userPostDTO) {
        try {
            
            String result = postService.editPost(userId, postId, userPostDTO.getContent(), userPostDTO.getImageUrl());

            
            if (result.contains("validation failed") || result.contains("does not exist") || result.contains("not found") || result.contains("permission")) {
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
                    .entity("An error occurred while editing the post.")
                    .build();
        }
    }

    /**
     * DELETE /posts/delete/{userId}/{postId} - Deletes a specific post made by a user.
     * 
     * @param userId The ID of the user deleting the post.
     * @param postId The ID of the post to be deleted.
     * @return A Response indicating the result of the post deletion.
     */
    @DELETE
    @Path("/delete/{userId}/{postId}")
    public Response deletePost(@PathParam("userId") Long userId,
                               @PathParam("postId") int postId) {
        try {
            
            String result = postService.deletePost(userId, postId);

           
            if (result.contains("not found") || result.contains("permission")) {
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
                    .entity("An error occurred while deleting the post.")
                    .build();
        }
    }
}
