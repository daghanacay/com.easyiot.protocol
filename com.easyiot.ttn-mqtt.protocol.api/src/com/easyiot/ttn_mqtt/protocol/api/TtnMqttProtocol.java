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
package com.easyiot.ttn_mqtt.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

/**
 * The client interface used to operate on MQTT Calls
 */
@ProviderType
public interface TtnMqttProtocol extends Protocol {

	
	/**
	 * Connect to Message Broker
	 */
	public boolean connect();

	/**
	 * Disconnect the client from the broker
	 */
	public void disconnect();
	
	/**
	 * Publish a message to a channel
	 *
	 * @param channel
	 *            the channel we are publishing to
	 * @param message
	 *            the message we are publishing
	 */
	public void publish(final String channel, final String payload);

	/**
	 * Subscribes to a channel and registers a callback that is fired every time
	 * a new message is published on the channel.
	 *
	 * @param channel
	 *            the channel we are subscribing to
	 * @param callback
	 *            the callback to be fired whenever a message is received on
	 *            this channel
	 */
	public void subscribe(final String channel, final TtnMqttMessageListener callback);

	/**
	 * Unsubscribes from a channel.
	 *
	 * @param channel
	 *            the channel we are unsubscribing to
	 */
	public void unsubscribe(final String channel);

}
