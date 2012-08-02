package uk.co.datumedge.jerseymock;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.LowLevelAppDescriptor;
import com.sun.jersey.test.framework.spi.container.TestContainerException;
import com.sun.jersey.test.framework.spi.container.TestContainerFactory;

/**
 * A builder of configured JerseyTest instances to be composed into test classes.
 */
public class JerseyTestBuilder {
	private final Collection<Object> resources = new HashSet<>();
	private final Map<String, Boolean> features = new HashMap<>();
	private TestContainerFactory testContainerFactory;
	private Integer port;
	
	public static JerseyTestBuilder aJerseyTest() {
		return new JerseyTestBuilder();
	}
	
	/**
	 * Configure the TestContainerFactory.
	 * 
	 * @param testContainerFactory
	 *            a TestContainerFactory instance. If null, the default TestContainerFactory will be used.
	 * 
	 * @return the configured builder
	 */
	public JerseyTestBuilder withTestContainerFactory(TestContainerFactory testContainerFactory) {
		this.testContainerFactory = testContainerFactory;
		return this;
	}

	public JerseyTestBuilder withPort(int port) {
		this.port = port;
		return this;
	}
	
	public JerseyTestBuilder enable(String key) {
		this.features.put(key, true);
		return this;
	}
	
	public JerseyTestBuilder addResource(Object resource) {
		resources.add(resource);
		return this;
	}

	public JerseyTest build() {
		return new JerseyTest() {
			@Override
			protected TestContainerFactory getTestContainerFactory() throws TestContainerException {
				if (testContainerFactory == null) {
					return super.getTestContainerFactory();
				} else {
					return testContainerFactory;
				}
			}
			
			@Override
			protected int getPort(int defaultPort) {
				if (port == null) {
					return super.getPort(defaultPort);
				} else {
					return port;
				}
			}
			
			@Override
			protected AppDescriptor configure() {
				DefaultResourceConfig resourceConfig = new DefaultResourceConfig();
				resourceConfig.getFeatures().putAll(features);
				for (Object resource : resources) {
					resourceConfig.getSingletons().add(resource);
				}
				return new LowLevelAppDescriptor.Builder(resourceConfig).build();
			}
		};
	}
}
