package com.easyiot.mqtt.protocol.api;

/**
 * Message Processor for subscription messages
 */
@FunctionalInterface
public interface MessageListener {

	/**
	 * Processes Received Message
	 */
	public void processMessage(final String message);
	
}
