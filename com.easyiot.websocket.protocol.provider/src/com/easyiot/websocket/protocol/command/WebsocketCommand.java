package com.easyiot.websocket.protocol.command;

import org.osgi.service.component.annotations.Component;

import osgi.enroute.debug.api.Debug;

/**
 * creates a websocke server on ws://localhost:8025/websockets/echo
 * @author daghan
 *
 */
@Component(service = WebsocketCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=ws",
		Debug.COMMAND_FUNCTION + "=startServer" })
public class WebsocketCommand {

//	public void startServer() {
//		Server server = new Server("localhost", 8025, "/websockets", null, WsServer.class);
//		try {
//			server.start();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//			System.out.print("Please press a key to stop the server.");
//			reader.readLine();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			server.stop();
//		}
//	}
//
//	@ServerEndpoint(value = "/echo")
//	public class WsServer {
//
//		@OnOpen
//		public void onOpen(Session session) {
//			System.out.println("Connected ... " + session.getId());
//		}
//
//		@OnMessage
//		public String onMessage(String message, Session session) {
//			System.out.println(message);
//			return "Echo from server, your message: " + message;
//		}
//
//		@OnClose
//		public void onClose(Session session, CloseReason closeReason) {
//			System.out.println(String.format("Session %s closed because of %s", session.getId(), closeReason));
//		}
//	}
}
