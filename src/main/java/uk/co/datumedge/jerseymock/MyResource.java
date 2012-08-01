package uk.co.datumedge.jerseymock;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public interface MyResource {
	@GET String get();
}
