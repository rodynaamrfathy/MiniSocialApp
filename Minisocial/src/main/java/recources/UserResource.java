package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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

    @POST
    @Path("/login")
    public Response login(User loginUser) {
        String userName = loginUser.getUserName();
        String password = loginUser.getPassword();

        User user = userService.login(userName, password);
        if (user != null) {
            return Response.ok("Login successful").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password")
                    .build();
        }
    }
}
