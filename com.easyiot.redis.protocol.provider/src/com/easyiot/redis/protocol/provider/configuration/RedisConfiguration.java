package com.easyiot.redis.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Redis protocol Configuration")
public @interface RedisConfiguration {
	@AttributeDefinition(name = "Instance ID", description = "Redis protocol instance ID", required = true)
	public String id() default "default.redis";

	@AttributeDefinition(name = "Redis Host IP", description = "IP of the host that the redis is running, default localhost", required = false)
	public String host() default "localhost";

	@AttributeDefinition(name = "Redis port", description = "Redis server port, default 6379", required = false)
	int port() default 6379;
	
}
