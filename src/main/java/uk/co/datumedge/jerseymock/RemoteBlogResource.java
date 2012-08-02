package uk.co.datumedge.jerseymock;

import com.sun.jersey.api.client.WebResource;

public class RemoteBlogResource implements BlogResource {
	private final WebResource resource;

	public RemoteBlogResource(WebResource resource) {
		this.resource = resource;
	}

	@Override
	public Blog get() {
		return resource.get(Blog.class);
	}
}
