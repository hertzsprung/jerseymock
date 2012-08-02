package uk.co.datumedge.jerseymock;

import static uk.co.datumedge.jerseymock.Feed.Builder.aFeed;

import java.util.Collection;

public class BlogAggregator {
	private Collection<BlogResource> blogResources;

	public BlogAggregator(Collection<BlogResource> blogResources) {
		this.blogResources = blogResources;
	}

	public Feed aggregate() {
		Feed.Builder feed = aFeed();
		for (BlogResource blogResource : blogResources) {
			// assuming blogs have at least one article, and they're in reverse chronological order
			feed.addArticle(blogResource.get().iterator().next());
		}
		return feed.build();
	}
}
