package recources;

import models.Like;
import models.LikeDTO;
import models.User;
import service.LikeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/likes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LikeResource {

    @Inject
    private LikeService likeService;

    @POST
    @Path("/{postId}/{userId}")
    public Response likePost(@PathParam("postId") int postId, @PathParam("userId") Long userId) {
        try {
            User user = likeService.findUserById(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User with ID " + userId + " not found.")
                        .build();
            }

            List<String> errors = likeService.likePost(user, postId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(errors)
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity("Post liked successfully.")
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/post/{postId}")
    public Response getLikesForPost(@PathParam("postId") int postId) {
        try {
            List<Like> likes = likeService.getLikesForPost(postId);
            
            if (likes.isEmpty()) {
                return Response.status(Response.Status.OK)
                               .entity("No likes for this post.")
                               .build();
            }
            
            List<LikeDTO> likeDTOs = likes.stream()
                                          .map(LikeDTO::fromLike)
                                          .collect(Collectors.toList());

            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to fetch likes: " + e.getMessage())
                    .build();
        }
    }
}
