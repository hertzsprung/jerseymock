package uk.co.datumedge.jerseymock;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class BlogAggregatorUnitTest {
	private final Article alicesMostRecentArticle = new Article(date(2012, 8, 1), "Alice's most recent article");
	private final Article bobsMostRecentArticle = new Article(date(2012, 7, 30), "Bob's most recent article");
	private Article anotherArticleByAlice = new Article(date(2012, 8, 1), "Another article by Alice");
	private Article anotherArticleByBob = new Article(date(2012, 8, 1), "Another article by Bob");
	
	private final Blog alicesBlog = new Blog(ImmutableList.of(alicesMostRecentArticle, anotherArticleByAlice));
	private final Blog bobsBlog = new Blog(ImmutableList.of(bobsMostRecentArticle, anotherArticleByBob));
	
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	private final BlogResource alicesBlogResource = context.mock(BlogResource.class, "Alice's Blog");
	private final BlogResource bobsBlogResource = context.mock(BlogResource.class, "Bob's Blog");
	
	@Test public void providesFeedContainingMostRecentArticleFromEachBlog() {
		context.checking(new Expectations() {{
			allowing(alicesBlogResource).get(); will(returnValue(alicesBlog));
			allowing(bobsBlogResource).get(); will(returnValue(bobsBlog));
		}});
		
		Feed feed = new BlogAggregator(ImmutableList.of(alicesBlogResource, bobsBlogResource)).aggregate();
		assertThat(feed, containsInAnyOrder(alicesMostRecentArticle, bobsMostRecentArticle));
	}

	private DateTime date(int year, int month, int day) {
		return new DateTime(year, month, day, 0, 0, ISOChronology.getInstanceUTC());
	}
}