package recources;

import models.Comment;
import models.GroupPost;
import models.User;
import models.UserPost;
import service.CommentService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import dtos.CommentDTO;
import messaging.NotificationEvent;
import messaging.NotificationProducer;

import java.util.List;
import java.util.Map;

/**
 * The CommentResource class provides RESTful endpoints for managing comments.
 * It allows adding comments to posts and retrieving comments for posts.
 * Comments can be associated with either UserPosts or GroupPosts.
 */
@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    // Injecting the CommentService for handling business logic related to comments
    @Inject
    private CommentService commentService;

    @Inject
    private NotificationProducer notificationProducer;
    
    
    /**
     * Adds a comment to a UserPost.
     * The comment is associated with a user and a post.
     * @param postId the ID of the UserPost.
     * @param userId the ID of the user making the comment.
     * @param json the request body containing the comment's content.
     * @return a Response indicating the success or failure of the operation.
     */
    @POST
    @Path("/userpost/{postId}/{userId}")
    public Response addCommentToUserPost(@PathParam("postId") int postId,
                                         @PathParam("userId") Long userId,
                                         Map<String, String> json) {
    	
    	
        User commenter = commentService.findUserById(userId);
        UserPost post     = commentService.findUserPostById(postId);
        User author       = post != null ? post.getUser() : null;

      
        if (commenter != null && author != null && !commenter.getUserId().equals(author.getUserId())) {
            String message = commenter.getFirstName() + " commented on your post.";
            NotificationEvent event = new NotificationEvent(
                commenter.getUserId(),
                author.getUserId(),
                "COMMENT_ADDED",
                message
            );
            notificationProducer.sendNotification(event);
        }

    	
      
        return handleAddComment(postId, userId, json, null, "userpost");
    }

    /**
     * Adds a comment to a GroupPost.
     * The comment is associated with a user, a post, and a group.
     * @param postId the ID of the GroupPost.
     * @param userId the ID of the user making the comment.
     * @param groupId the ID of the group associated with the post.
     * @param json the request body containing the comment's content.
     * @return a Response indicating the success or failure of the operation.
     */
    @POST
    @Path("/grouppost/{postId}/{userId}/{groupId}")
    public Response addCommentToGroupPost(@PathParam("postId") int postId,
                                          @PathParam("userId") Long userId,
                                          @PathParam("groupId") Long groupId,
                                          Map<String, String> json) {
    	
     
        User commenter = commentService.findUserById(userId);
        GroupPost post  = commentService.findGroupPostById(postId);
        User author     = post != null ? post.getUser() : null;

     
        if (commenter != null && author != null && !commenter.getUserId().equals(author.getUserId())) {
            String message = commenter.getFirstName() + " commented on your group post in \"" 
                           + post.getGroup().getGroupName() + "\".";
            NotificationEvent event = new NotificationEvent(
                commenter.getUserId(),
                author.getUserId(),
                "COMMENT_ADDED",
                message
            );
            notificationProducer.sendNotification(event);
        }
        
   
        return handleAddComment(postId, userId, json, groupId, "grouppost");
    }

    /**
     * Common handler method for adding comments to either UserPost or GroupPost.
     * This method checks for empty content, delegates to the CommentService, and handles success or failure responses.
     * @param postId the ID of the post.
     * @param userId the ID of the user making the comment.
     * @param json the request body containing the comment's content.
     * @param groupId the ID of the group (nullable for UserPost).
     * @param type the type of post (either "userpost" or "grouppost").
     * @return a Response indicating the success or failure of the operation.
     */
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

    /**
     * Retrieves the list of comments for a UserPost.
     * @param postId the ID of the UserPost.
     * @return a Response containing the list of comments or a message if no comments exist.
     */
    @GET
    @Path("/userpost/{postId}")
    public Response getCommentsForUserPost(@PathParam("postId") int postId) {
        // Delegating the logic to the common handler method for retrieving comments
        return handleGetComments(postId, null, "userpost");
    }

    /**
     * Retrieves the list of comments for a GroupPost.
     * @param postId the ID of the GroupPost.
     * @param groupId the ID of the group associated with the post.
     * @return a Response containing the list of comments or a message if no comments exist.
     */
    @GET
    @Path("/grouppost/{postId}/{groupId}")
    public Response getCommentsForGroupPost(@PathParam("postId") int postId,
                                            @PathParam("groupId") Long groupId) {
   
        return handleGetComments(postId, groupId, "grouppost");
    }

    /**
     * Common handler method for getting comments for either UserPost or GroupPost.
     * This method fetches the list of comments, converts them to DTOs, and returns them.
     * @param postId the ID of the post.
     * @param groupId the ID of the group (nullable for UserPost).
     * @param type the type of post (either "userpost" or "grouppost").
     * @return a Response containing the list of CommentDTO objects or a message if no comments exist.
     */
    private Response handleGetComments(int postId, Long groupId, String type) {
        try {
            // Fetching the list of comments from the service layer
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
