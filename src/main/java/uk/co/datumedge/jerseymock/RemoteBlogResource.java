package uk.co.datumedge.jerseymock;

import java.util.Arrays;

import com.sun.jersey.api.client.WebResource;

public class RemoteBlogResource implements BlogResource {
	private final WebResource resource;

	public RemoteBlogResource(WebResource resource) {
		this.resource = resource;
	}

	@Override
	public Blog get() {
		return new Blog(Arrays.asList(resource.get(Article[].class)));
	}
}
