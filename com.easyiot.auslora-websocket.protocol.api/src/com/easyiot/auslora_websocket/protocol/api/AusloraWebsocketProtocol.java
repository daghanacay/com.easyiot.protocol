package com.easyiot.auslora_websocket.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

/**
 * Provides a Websocket protocol
 * 
 * @author daghan
 *
 */
@ProviderType
public interface AusloraWebsocketProtocol extends Protocol {

	/**
	 * Connects to channel on an auslora server.
	 * 
	 * @param applicationID
	 *            if there is auslora server endpoint is
	 *            wss://nwk.auslora.com.au/app?id=BE01000C&token=
	 *            qCbvxPgM0cg0lKb80vkoug than the channel is
	 *            "app?id=BE01000C&token= qCbvxPgM0cg0lKb80vkoug" with
	 *            application id and access token.
	 * @param deviceEUI
	 *            EUI of the device given by the device manufacturer that you
	 *            wish to connect to.
	 * @param callback
	 *            id the callback to receive any information from the server
	 */
	public void connect(String applicationID, String deviceEUI, final AusloraWebsocketListener callback);

	/**
	 * Disconnects from application.
	 * 
	 * @param applicationID
	 *            See {@link #connect(String, String, AusloraWebsocketListener)}
	 * @param deviceEUI
	 * @param callback
	 *            id the callback to receive any information from the server
	 */
	public void disconnect(String applicationID, String deviceEUI, final AusloraWebsocketListener callback);

	/**
	 * Sends message to a device in an application
	 * 
	 * @param applicationID
	 *            as {@link #connect(String, WsListener)}
	 * @param deviceEUI
	 * @param message
	 *            message to be sent to websocket
	 */
	public void sendMessage(String applicationID, String deviceEUI, String message);

}
