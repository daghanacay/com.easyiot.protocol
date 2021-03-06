/*******************************************************************************
 * Copyright 2015 OSGi Alliance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.easyiot.mqtt.protocol.provider.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;



/**
 * Metatype Configurations for MQTT Communication
 */

@ObjectClassDefinition(name = "MQTT Configuration")
public @interface MqttConfiguration {
	/**
	 * Id of the protocol instance
	 */
	@AttributeDefinition(name = "Instance ID", description = "MQTT protocol instance ID", required = true)
	public String id() default "mqtt";

	
	/**
	 * MQTT broker Server Address
	 */
	@AttributeDefinition(name = "MQTT Server", description = "MQTT Server Address", required = true)
	public String host() default "mqtt.server";

	/**
	 * MQTT Broker Port
	 */
	@AttributeDefinition(name = "MQTT Port", description = "MQTT Connection Port", required = true)
	public int port() default 1883;

	/**
	 * MQTT Broker Username
	 */
	@AttributeDefinition(name = "MQTT Username", description = "MQTT Username")
	public String username() default "";

	/**
	 * MQTT Broker Password
	 */
	@AttributeDefinition(name = "MQTT Password", description = "MQTT Password")
	public String userPassword() default "";

}
