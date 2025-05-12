package recources;

import models.NotificationEntity;
import service.NotificationService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    @GET
    public Response getUserNotifications(@QueryParam("userId") Long userId) {
        if (userId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Missing userId query parameter.")
                           .build();
        }

        List<NotificationEntity> notifications = notificationService.getNotificationsForUser(userId);

        if (notifications.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(notifications).build();
    }

    @PUT
    @Path("/{id}/read")
    public Response markAsRead(@PathParam("id") Long id) {
        notificationService.markAsRead(id);
        return Response.ok("Notification marked as read.").build();
    }
}
