package uk.co.datumedge.jerseymock;

import org.joda.time.DateTime;

public class Article {
	private DateTime date;
	private String content;

	public Article(DateTime date, String content) {
		this.date = date;
		this.content = content;
	}
}
