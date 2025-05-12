package recources;

import models.Like;
import models.LikeDTO;
import service.LikeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/likes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LikeResource {

    @Inject
    private LikeService likeService;

    // ✅ Like a UserPost
    @POST
    @Path("/userpost/{postId}/{userId}")
    public Response likeUserPost(@PathParam("postId") int postId, @PathParam("userId") Long userId) {
        try {
            List<String> errors = likeService.likeUserPost(userId, postId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }

            return Response.status(Response.Status.CREATED)
                           .entity("User post liked successfully.")
                           .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to like user post: " + e.getMessage())
                           .build();
        }
    }

 // ✅ Like a GroupPost (FIXED DTO Mapping)
    @POST
    @Path("/group/{groupId}/{postId}/{userId}")
    public Response likeGroupPost(@PathParam("groupId") Long groupId, @PathParam("postId") int postId, @PathParam("userId") Long userId) {
        try {
            List<String> errors = likeService.likeGroupPost(userId, postId, groupId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }

            return Response.status(Response.Status.CREATED)
                           .entity("Group post liked successfully.")
                           .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to like group post: " + e.getMessage())
                           .build();
        }
    }


    // ✅ Get Likes for a UserPost (FIXED DTO Mapping)
    @GET
    @Path("/userpost/{postId}")
    public Response getLikesForUserPost(@PathParam("postId") int postId) {
        try {
            List<Like> likes = likeService.getLikesForUserPost(postId);

            if (likes.isEmpty()) {
                return Response.status(Response.Status.OK)
                               .entity("No likes for this user post.")
                               .build();
            }

            List<LikeDTO> likeDTOs = LikeDTO.fromLikeList(likes);

            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch likes for user post: " + e.getMessage())
                           .build();
        }
    }

    // ✅ Get Likes for a GroupPost (FIXED DTO Mapping)
    @GET
    @Path("/group/{groupId}/{postId}")
    public Response getLikesForGroupPost(@PathParam("groupId") Long groupId, @PathParam("postId") int postId) {
        try {
            List<Like> likes = likeService.getLikesForGroupPost(postId, groupId);

            if (likes.isEmpty()) {
                return Response.status(Response.Status.OK)
                               .entity("No likes for this group post.")
                               .build();
            }

            List<LikeDTO> likeDTOs = LikeDTO.fromLikeList(likes);

            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch likes for group post: " + e.getMessage())
                           .build();
        }
    }
}
