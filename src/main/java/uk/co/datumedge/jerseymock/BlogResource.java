package uk.co.datumedge.jerseymock;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public interface BlogResource {
	@GET @Produces(APPLICATION_JSON) Blog get();
}
