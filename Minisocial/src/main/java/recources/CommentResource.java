package recources;

import models.Comment;
import models.CommentDTO;
import service.CommentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Map;

@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    @Inject
    private CommentService commentService;

    // ✅ Add Comment to UserPost
    @POST
    @Path("/userpost/{postId}/{userId}")
    public Response addCommentToUserPost(@PathParam("postId") int postId,
                                         @PathParam("userId") Long userId,
                                         Map<String, String> json) {
        return handleAddComment(postId, userId, json, null, "userpost");
    }

    // ✅ Add Comment to GroupPost
    @POST
    @Path("/grouppost/{postId}/{userId}/{groupId}")
    public Response addCommentToGroupPost(@PathParam("postId") int postId,
                                          @PathParam("userId") Long userId,
                                          @PathParam("groupId") Long groupId,
                                          Map<String, String> json) {
        return handleAddComment(postId, userId, json, groupId, "grouppost");
    }

    // 🛠 Common Handler for Adding Comments
    private Response handleAddComment(int postId, Long userId, Map<String, String> json, Long groupId, String type) {
        try {
            String content = json.get("content");

            if (content == null || content.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("Content field is required.")
                               .build();
            }

            List<String> errors;
            if ("userpost".equals(type)) {
                errors = commentService.addCommentToUserPost(userId, postId, content);
            } else {
                errors = commentService.addCommentToGroupPost(userId, postId, groupId, content);
            }

            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }

            return Response.status(Response.Status.CREATED)
                           .entity("Comment added successfully.")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to add comment: " + e.getMessage())
                           .build();
        }
    }

    // ✅ Get Comments for UserPost
    @GET
    @Path("/userpost/{postId}")
    public Response getCommentsForUserPost(@PathParam("postId") int postId) {
        return handleGetComments(postId, null, "userpost");
    }

    // ✅ Get Comments for GroupPost
    @GET
    @Path("/grouppost/{postId}/{groupId}")
    public Response getCommentsForGroupPost(@PathParam("postId") int postId,
                                            @PathParam("groupId") Long groupId) {
        return handleGetComments(postId, groupId, "grouppost");
    }

    // 🛠 Common Handler for Getting Comments
    private Response handleGetComments(int postId, Long groupId, String type) {
        try {
            List<Comment> comments;
            if ("userpost".equals(type)) {
                comments = commentService.getCommentsForUserPost(postId);
            } else {
                comments = commentService.getCommentsForGroupPost(postId, groupId);
            }

            if (comments.isEmpty()) {
                return Response.status(Response.Status.OK)
                               .entity("No comments for this post.")
                               .build();
            }

            List<CommentDTO> dtos = CommentDTO.fromCommentList(comments);
            return Response.ok(dtos).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch comments: " + e.getMessage())
                           .build();
        }
    }
}
