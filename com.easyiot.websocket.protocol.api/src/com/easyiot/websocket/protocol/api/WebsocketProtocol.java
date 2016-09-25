package com.easyiot.websocket.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

/**
 * Provides a Websocket protocol
 * 
 * @author daghan
 *
 */
@ProviderType
public interface WebsocketProtocol extends Protocol{

	/**
	 * Connects to channel. channel here is the WS endpoint. For example
	 * 
	 * @param channel
	 *            if there is web server endpoint on server ws://localhost:8080
	 *            with @ServerEndpoint(value = "/device1") than the channel is
	 *            "device1"
	 * @param callback
	 *            id the callback to receive any information from the server
	 */
	public void connect(String channel, final WsListener callback);
	
	/**
	 * Disconnects from channel.
	 * 
	 * @param channel
	 *            if there is web server endpoint on server ws://localhost:8080
	 *            with @ServerEndpoint(value = "/device1") than the channel is
	 *            "device1"
	 * @param callback
	 *            id the callback to receive any information from the server
	 */
	public void disconnect(String channel, final WsListener callback);

	/**
	 * Writes message to a channel
	 * 
	 * @param channel
	 *            as {@link #connect(String, WsListener)}
	 * @param message
	 *            message to be sent to websocket
	 */
	public void sendMessage(String channel, String message);

}
