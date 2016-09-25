package com.easyiot.websocket.protocol.api;

public interface WsListener {
	/**
	 * Processes Received Message
	 * 
	 * @param message
	 *            From the WS server.
	 */
	public void processMessage(final String message);
}
