package com.easyiot.websocket.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Auslora Websocket Configuration")
public @interface AusloraWebsocketConfiguration {

	/**
	 * Id of the protocol instance
	 */
	@AttributeDefinition(name = "Instance ID", description = "Auslora Websocket protocol instance ID", required = true)
	public String id() default "auslora.websocket";

	/**
	 * Websocket Server Address
	 */
	@AttributeDefinition(name = "Websocket Server", description = "Auslora Websocket Server Address", required = true)
	public String host() default "nwk.auslora.com.au";

	/**
	 * Websocket Port
	 */
	@AttributeDefinition(name = "Websocket Port", description = "Auslora Websocket Connection Port", required = true)
	public int port() default 80;
	
}
