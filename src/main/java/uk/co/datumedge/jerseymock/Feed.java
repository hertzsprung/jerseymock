package uk.co.datumedge.jerseymock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Feed implements Iterable<Article> {

	private Collection<Article> articles;

	public static class Builder {
		private final Collection<Article> articles = new ArrayList<>();

		public static Builder aFeed() {
			return new Builder();
		}
		
		public Builder addArticle(Article article) {
			this.articles.add(article);
			return this;
		}
		
		public Feed build() {
			return new Feed(articles);
		}
	}
	
	public Feed(Collection<Article> articles) {
		this.articles = articles;
	}

	public Iterator<Article> iterator() {
		return articles.iterator();
	}
}
