package com.easyiot.auslora_websocket.protocol.api;

import com.easyiot.auslora_websocket.protocol.api.dto.AusloraMetadataDTO;

/**
 * Websocket listener that will be called when a response comed from websocket.
 * 
 * @author daghan
 *
 */
public interface AusloraWebsocketListener {
	/**
	 * Message that needs to be processes. Data is passed as a form of Auslora
	 * metadata format
	 * 
	 * @param ausloraData
	 */
	public void processMessage(AusloraMetadataDTO ausloraData);
	
	/**
	 * Sets the protocol instance for the client so they can access it directly
	 * 
	 * @param protocolInstance
	 */
	public void setProtocolHandler(AusloraWebsocketProtocol protocolInstance);
}
