package com.easyiot.websocket.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Auslora Device Admin Configuration")
public @interface AusloraDeviceServiceConfig {
	/**
	 * Auslora admin URL
	 */
	@AttributeDefinition(name = "Admin URL", description = "URL of the admin services for Auslora.", required = true)
	public String adminUrl();

	@AttributeDefinition(name = "API key", description = "API key for Auslora login. Can be found under API keys of the ausloar account.", required = true)
	String apiKey();

}
