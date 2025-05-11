package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import models.NotificationEntity;
import service.NotificationService;
import java.util.List;



@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationService notificationService;

    @GET
    public List<NotificationEntity> getUserNotifications(@QueryParam("userId") Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @POST
    @Path("/mark-read")
    public void markAsRead(@QueryParam("notificationId") Long id) {
        notificationService.markAsRead(id);
    }
}

