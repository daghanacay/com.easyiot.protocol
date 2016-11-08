package com.easyiot.bluetooth.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Bluetooth Configuration")
public @interface BluetoothConfiguration {

	/**
	 * Id of the protocol instance
	 */
	@AttributeDefinition(name = "Instance ID", description = "Bluetooth protocol instance ID", required = true)
	public String id() default "easy.iot.bluetooth";

	
	/**
	 * MQTT broker Server Address
	 */
	@AttributeDefinition(name = "Host address", description = "Bluetooth host address", required = false)
	public String host() default "";
}
