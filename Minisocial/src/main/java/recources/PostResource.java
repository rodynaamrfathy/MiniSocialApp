package recources;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import models.UserPost;
import models.UserPostDTO;
import service.PostService;

@Path("/posts")
@Produces("application/json")
@Consumes("application/json")
public class PostResource {

    @Inject
    PostService postService;

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

            UserPostDTO createdPostDTO = UserPostDTO.fromUserPost(userPost);
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
