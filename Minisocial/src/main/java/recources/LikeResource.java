package recources;

import models.Like;
import models.User;
import models.UserPost;
import service.CommentService;
import service.LikeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import dtos.LikeDTO;
import messaging.ActivityLogEvent;
import messaging.ActivityLogProducer;
import messaging.NotificationEvent;
import messaging.NotificationProducer;

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
    
    @Inject
    private CommentService commentService; // Needed to fetch user and post

    @Inject
    private NotificationProducer notificationProducer;
    
    @Inject
    private ActivityLogProducer activityLogProducer;


    // Like a UserPost
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
          
            List<String> errors = likeService.likeUserPost(userId, postId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }
            
     
            User liker = commentService.findUserById(userId);
            UserPost post = commentService.findUserPostById(postId);
            User author = post != null ? post.getUser() : null;

<<<<<<< HEAD
           
=======

			// log activity
            activityLogProducer.sendActivityLog(
            	    new ActivityLogEvent(userId, "LIKED", "Liked post #" + postId)
            	);
            
            // Send notification if not self-like
>>>>>>> abd03d5ce0325ac573dac2762219035a663f4f3a
            if (liker != null && author != null && !liker.getUserId().equals(author.getUserId())) {
                String message = liker.getFirstName() + " liked your post.";
                NotificationEvent event = new NotificationEvent(
                    liker.getUserId(),
                    author.getUserId(),
                    "POST_LIKED",
                    message
                );
                notificationProducer.sendNotification(event);
            }

    
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

    // Like a GroupPost (with DTO Mapping)
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
       
            List<String> errors = likeService.likeGroupPost(userId, postId, groupId);
            if (!errors.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity(errors)
                               .build();
            }
            
       
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

    // Get Likes for a UserPost (with DTO Mapping)
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
          
            List<Like> likes = likeService.getLikesForUserPost(postId);

            if (likes.isEmpty()) {
                // Return message if no likes exist for the post
                return Response.status(Response.Status.OK)
                               .entity("No likes for this user post.")
                               .build();
            }

           
            List<LikeDTO> likeDTOs = LikeDTO.fromLikeList(likes);

           
            return Response.ok(likeDTOs).build();

        } catch (Exception e) {
            // Log and return failure response in case of error
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to fetch likes for user post: " + e.getMessage())
                           .build();
        }
    }

    // Get Likes for a GroupPost (with DTO Mapping)
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
