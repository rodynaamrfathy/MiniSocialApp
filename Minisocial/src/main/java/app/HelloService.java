package app;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ejbs.Hello;

@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/hello")  // Path to resource
public class HelloService {
    @EJB
    Hello hello;

    @GET
    @Path("/helloworld")  // Path to method
    @Produces(MediaType.TEXT_PLAIN)
    public String helloworld() {
        return hello.helloworld();
    }
}
