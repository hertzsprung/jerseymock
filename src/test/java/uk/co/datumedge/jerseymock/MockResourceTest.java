package uk.co.datumedge.jerseymock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.datumedge.jerseymock.JerseyTestBuilder.aJerseyTest;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.jmock.lib.concurrent.Synchroniser;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import uk.co.datumedge.jerseymock.MyResource;

import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.spi.container.grizzly2.GrizzlyTestContainerFactory;

public class MockResourceTest {
	@Rule public JUnitRuleMockery context = new JUnitRuleMockery() {{
		setThreadingPolicy(new Synchroniser());
	}};
	private final MyResource fakeResource = context.mock(MyResource.class);
	private final JerseyTest jerseyTest = aJerseyTest()
			.withTestContainerFactory(new GrizzlyTestContainerFactory())
			.addResource(fakeResource)
			.build();

	@Before
	public void start() throws Exception {
		jerseyTest.setUp();
	}
	
	@After
	public void stop() throws Exception {
		jerseyTest.tearDown();
	}
	
	@Test
	public void respondsToGetRequest() {
		context.checking(new Expectations() {{
			allowing(fakeResource).get(); will(returnValue("foo"));
		}});
		
		String actualResponse = jerseyTest.resource().path("/").get(String.class);
		assertThat(actualResponse, is("foo"));
	}
}
