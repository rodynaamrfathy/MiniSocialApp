package recources;

import models.ActivityLog;
import service.ActivityLogService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("/activity-log")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityLogResource {

    @Inject
    private ActivityLogService activityLogService;

    /**
     * GET /activity-log/user/{userId}
     * Fetches recent activity logs for the given user.
     */
    @GET
    @Path("/user/{userId}")
    public Response getUserActivityLogs(@PathParam("userId") Long userId) {
        List<ActivityLog> logs = activityLogService.getLogsForUser(userId);

        if (logs.isEmpty()) {
            return Response.status(Response.Status.OK)
                           .entity("No activity logs found for user " + userId)
                           .build();
        }

        return Response.ok(logs).build();
    }

    /**
     * GET /activity-log
     * Fetches all recent activity logs (useful for admin monitoring).
     */
    @GET
    public Response getAllLogs() {
        List<ActivityLog> logs = activityLogService.getAllLogs();
        return Response.ok(logs).build();
    }
}
