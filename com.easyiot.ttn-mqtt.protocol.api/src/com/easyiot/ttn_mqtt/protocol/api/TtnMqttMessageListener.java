package com.easyiot.ttn_mqtt.protocol.api;

/**
 * Message Processor for subscription messages
 */
@FunctionalInterface
public interface TtnMqttMessageListener {

	/**
	 * Processes Received Message
	 */
	public void processMessage(final String message);
	
}
