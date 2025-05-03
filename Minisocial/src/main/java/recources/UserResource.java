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
