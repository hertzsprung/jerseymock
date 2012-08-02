package uk.co.datumedge.jerseymock;

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.jerseymock.JerseyTestBuilder.aJerseyTest;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.spi.container.grizzly2.GrizzlyTestContainerFactory;

public class BlogAggregatorIntegrationTest {
	private final Article alicesMostRecentArticle = new Article(date(2012, 8, 1), "Alice's most recent article");
	private final Article bobsMostRecentArticle = new Article(date(2012, 7, 30), "Bob's most recent article");
	private Article anotherArticleByAlice = new Article(date(2012, 8, 1), "Another article by Alice");
	private Article anotherArticleByBob = new Article(date(2012, 8, 1), "Another article by Bob");
	
	private final Blog alicesBlog = new Blog(ImmutableList.of(alicesMostRecentArticle, anotherArticleByAlice));
	private final Blog bobsBlog = new Blog(ImmutableList.of(bobsMostRecentArticle, anotherArticleByBob));
	
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
		setThreadingPolicy(new Synchroniser());
	}};
	private final BlogResource alicesBlogResource = context.mock(BlogResource.class, "Alice's Blog");
	private final BlogResource bobsBlogResource = context.mock(BlogResource.class, "Bob's Blog");
	
	private final JerseyTest alicesServer = aJerseyTest()
			.withTestContainerFactory(new GrizzlyTestContainerFactory())
			.enable(FEATURE_POJO_MAPPING)
			.addResource(alicesBlogResource)
			.build();
	
	private final JerseyTest bobsServer = aJerseyTest()
			.withTestContainerFactory(new GrizzlyTestContainerFactory())
			.withPort(9999) // TODO: use 0
			.enable(FEATURE_POJO_MAPPING)
			.addResource(bobsBlogResource)
			.build();

	@Before
	public void start() throws Exception {
		alicesServer.setUp();
		bobsServer.setUp();
	}
	
	@After
	public void stop() throws Exception {
		alicesServer.tearDown();
		bobsServer.tearDown();
	}
	
	@Test public void providesFeedContainingMostRecentArticleFromEachBlog() {
		context.checking(new Expectations() {{
			allowing(alicesBlogResource).get(); will(returnValue(alicesBlog));
			allowing(bobsBlogResource).get(); will(returnValue(bobsBlog));
		}});
		
		BlogAggregator aggregator = new BlogAggregator(ImmutableList.of(
				clientFor(alicesServer),
				clientFor(bobsServer)));
		
		Feed feed = aggregator.aggregate();
		assertThat(feed, containsInAnyOrder(alicesMostRecentArticle, bobsMostRecentArticle));
	}

	private BlogResource clientFor(JerseyTest server) {
		return new RemoteBlogResource(server.resource().path("/"));
	}

	private DateTime date(int year, int month, int day) {
		return new DateTime(year, month, day, 0, 0, ISOChronology.getInstanceUTC());
	}
}
