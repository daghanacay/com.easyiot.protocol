package com.easyiot.http.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration for Gpio Protocol
 * 
 * @author daghan
 *
 */
@ObjectClassDefinition(name = "Http Configuration")
public @interface HttpProtocolConfiguration {

	@AttributeDefinition(name = "Instance ID", description = "GPIO protocol instance ID", required = true)
	public String id() default "rasberry.pi";

	@AttributeDefinition(name = "Server URL", description = "Server URL to connect to", required = true)
	public String url() default "http://google.com";

}
