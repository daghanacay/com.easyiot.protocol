package com.easyiot.gpio.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Configuration for Gpio Protocol
 * 
 * @author daghan
 *
 */
@ObjectClassDefinition(name = "GPIO Configuration")
public @interface GpioProtocolConfiguration {

	@AttributeDefinition(name = "Instance ID", description = "GPIO protocol instance ID", required = true)
	public String id() default "rasberry.pi";

}
