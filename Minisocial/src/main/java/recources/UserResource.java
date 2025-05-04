package recources;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import models.User;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user) {
        List<String> validationErrors = userService.validateUser(user);

        if (!validationErrors.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errors", validationErrors);
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorResponse)
                           .build();
        }

        User createdUser = userService.saveUser(user);

        return Response.status(Response.Status.CREATED)
                       .entity(createdUser)
                       .build();
    }

    @PUT
    @Path("/manageprofile")
    public Response manageProfile(User user) {
        try {
            userService.manageProfile(user);
            return Response.ok().build();
        } catch (WebApplicationException e) {
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

    @GET
    @Path("/allUsers")
    public Response getAllUsers() {
        List<String> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/allUsers/{userId}")
    public Response getUserById(@PathParam("userId") Long userId) {
        List<String> userStrings = userService.getUserStringById(userId);

        if (userStrings.contains("User not found")) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity(userStrings.get(0))
                           .build();
        }

        return Response.ok(userStrings.get(0)).build();
    }
}
