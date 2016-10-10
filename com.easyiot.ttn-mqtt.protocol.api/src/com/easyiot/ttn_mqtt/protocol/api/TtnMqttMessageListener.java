package com.easyiot.ttn_mqtt.protocol.api;

import com.easyiot.ttn_mqtt.protocol.api.dto.TtnMetaDataDTO;

/**
 * Message Processor for subscription messages
 */
@FunctionalInterface
public interface TtnMqttMessageListener {

	/**
	 * Processes Received Message
	 */
	public void processMessage(TtnMetaDataDTO message);
	
}
