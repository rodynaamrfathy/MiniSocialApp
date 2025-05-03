package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import models.User;
import service.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @PUT
    @Path("/manageprofile")
    public Response manageProfile(User user) {
        try {
            userService.manageProfile(user);
            return Response.ok().build(); // 200 OK
        } catch (WebApplicationException e) {
        	// function btrg3 404 lw user does not exist
            return Response.status(e.getResponse().getStatus()).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Something went wrong: " + e.getMessage())
                           .build();
        }
    }
}