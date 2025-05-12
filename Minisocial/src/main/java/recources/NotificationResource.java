package recources;

import models.NotificationEntity;
import service.NotificationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

/**
 * REST resource class for managing notifications.
 * This class provides end points to fetch user notifications, and mark notifications as read.
 */
@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    /**
     * Injected NotificationService instance for handling business logic related to notifications.
     */
    @Inject
    private NotificationService notificationService;

    /**
     * End point to retrieve notifications for a specific user.
     * 
     * @param userId the ID of the user whose notifications are to be fetched.
     * @return a list of notifications for the user, or an error response if no notifications exist.
     */
    @GET
    public Response getUserNotifications(@QueryParam("userId") Long userId) {
        // Check if userId is provided
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Missing userId query parameter.")
                           .build();
        }

        // Retrieve notifications for the user
        List<NotificationEntity> notifications = notificationService.getNotificationsForUser(userId);

        // Return appropriate response based on whether notifications were found
        if (notifications.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(notifications).build();
    }

    /**
     * End point to mark a specific notification as read.
     * 
     * @param id the ID of the notification to be marked as read.
     * @return a success message indicating that the notification has been marked as read.
     */
    @PUT
    @Path("/{id}/read")
    public Response markAsRead(@PathParam("id") Long id) {
        // Mark the notification as read
        notificationService.markAsRead(id);
        return Response.ok("Notification marked as read.").build();
    }
}
