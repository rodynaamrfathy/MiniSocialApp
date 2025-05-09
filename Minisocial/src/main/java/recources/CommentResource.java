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

    @POST
    @Path("/{postId}/{userId}")
    public Response addComment(@PathParam("postId") int postId,
                               @PathParam("userId") Long userId,
                               Map<String, String> json) {
        try {
            String content = json.get("content");

            if (content == null || content.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("Content field is required.")
                               .build();
            }

            List<String> errors = commentService.addComment(userId, postId, content);
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

    @GET
    @Path("/post/{postId}")
    public Response getCommentsForPost(@PathParam("postId") int postId) {
        try {
            List<Comment> comments = commentService.getCommentsForPost(postId);
            
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
