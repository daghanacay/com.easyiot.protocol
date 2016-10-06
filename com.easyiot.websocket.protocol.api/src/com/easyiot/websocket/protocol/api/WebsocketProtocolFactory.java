package com.easyiot.websocket.protocol.api;

public interface WebsocketProtocolFactory {
	/**
	 * Returns a {@link WebsocketProtocol} implementation. creates a secure web
	 * socket instance on given host and port.
	 * 
	 * @param id
	 *            id of the websocket protocol
	 * @param host
	 * @param port
	 * 
	 * @return
	 */
	public WebsocketProtocol getWSSInstance(String host, int port);
}
