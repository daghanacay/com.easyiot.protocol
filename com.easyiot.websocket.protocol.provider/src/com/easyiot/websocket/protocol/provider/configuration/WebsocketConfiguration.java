package com.easyiot.websocket.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Websocket Configuration")
public @interface WebsocketConfiguration {

	/**
	 * Id of the protocol instance
	 */
	@AttributeDefinition(name = "Instance ID", description = "Websocket protocol instance ID", required = true)
	public String id() default "websocket";

	/**
	 * Websocket Server Address
	 */
	@AttributeDefinition(name = "Websocket Server", description = "Websocket Server Address", required = true)
	public String host() default "nwk.auslora.com.au";

	/**
	 * Websocket Port
	 */
	@AttributeDefinition(name = "Websocket Port", description = "Websocket Connection Port", required = true)
	public int port() default 80;

	@AttributeDefinition(name = "Websocket protocol", description = "WSS for secure and WS for non secure communication", required = true)
	public ProtocolEnum protocol() default ProtocolEnum.wss;
}
