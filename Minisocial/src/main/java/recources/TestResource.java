package recources;

import models.Test;
import service.TestService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/tests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @Inject
    private TestService testService;

    // 🚀 POST /tests
    @POST
    public Response addTest(Test test) {
        Test created = testService.addTest(test);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // 📥 GET /tests
    @GET
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }
}
