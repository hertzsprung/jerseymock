package uk.co.datumedge.jerseymock;

import java.util.Iterator;
import java.util.List;

public class Blog implements Iterable<Article> {
	private List<Article> articles;

	public Blog(List<Article> articles) {
		this.articles = articles;
	}

	public Iterator<Article> iterator() {
		return articles.iterator();
	}
}
