package com.easyiot.websocket.protocol.command;

import org.osgi.service.component.annotations.Component;

import osgi.enroute.debug.api.Debug;

/**
 * creates a websocke server on ws://localhost:8025/websockets/echo
 * 
 * @author daghan
 *
 */
@Component(service = WebsocketCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=ws",
		Debug.COMMAND_FUNCTION + "=startServer" })
public class WebsocketCommand {

	public void startServer() {
		System.out.println("start server called. does nothing at the moment");
	}

}
