package recources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;

import models.User;
import service.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN) // returning plain strings
public class UserResource {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public String createUser(User user) {
        userService.createUser(user);
        return "User Created Successfully!";
    }

    @GET
    @Path("/{id}")
    public String getUser(@PathParam("id") int id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "User not found";
        }
        return user.toString();
    }

    @GET
    @Path("/all")
    public String getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return "No users found";
        }

        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }
        return sb.toString();
    }
}
