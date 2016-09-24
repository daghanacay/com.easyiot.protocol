package com.easyiot.websocket.protocol.provider;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.websocket.protocol.api.WebsocketProtocol;
import com.easyiot.websocket.protocol.api.WsListener;
import com.easyiot.websocket.protocol.api.exception.ChannelConnectException;
import com.easyiot.websocket.protocol.api.exception.ChannelUnavailableException;
import com.easyiot.websocket.protocol.provider.configuration.WebsocketConfiguration;

@Designate(ocd = WebsocketConfiguration.class, factory = true)
@Component(name = "com.easyiot.websocket.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class WebsocketProtocolImpl implements WebsocketProtocol {

	private static final String PROTOCOL_NAME = "ws";

	private WebsocketConfiguration configuration;
	private CountDownLatch latch;
	private Map<String, WsEndpoint> channelList = new ConcurrentHashMap<>();

	/**
	 * Log Service Reference
	 */
	@Reference(cardinality = OPTIONAL)
	private volatile LogService logService;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(WebsocketConfiguration configuration) throws IOException {
		this.configuration = configuration;
	}

	@Override
	public String getId() {
		return configuration.id();
	}

	//
	@Override
	public void connect(String channel, WsListener callback) {
		latch = new CountDownLatch(1);
		WsEndpoint endpoint;
		if ((endpoint = channelList.get(channel)) != null) {
			// If we already created the channel just add the callback to the
			// existing channel
			endpoint.addMessageListener(callback);
		} else {
			ClientManager client = ClientManager.createClient();
			try {
				WsEndpoint newEndPoint = new WsEndpoint(channel, callback);
				String connectionUri = String.format("%s://%s:%s/%s", PROTOCOL_NAME, configuration.host(),
						configuration.port(), channel);
				client.connectToServer(newEndPoint, new URI(connectionUri));
				// Wait for 100 sec to see if it connects. Released in
				// WsEndpoint.onOpen
				latch.await(100, TimeUnit.SECONDS);
			} catch (DeploymentException | URISyntaxException | InterruptedException | IOException e) {
				throw new ChannelConnectException(e);
			}
		}
	}

	@Override
	public void disconnect(String channel, WsListener callback) {
		WsEndpoint endpoint;
		if ((endpoint = channelList.get(channel)) != null) {
			// If we already created the channel just add the callback to the
			// existing channel
			endpoint.removeMessageListener(callback);
		}
	}

	@Override
	public void sendMessage(String channel, String message) {
		WsEndpoint endpoint;
		if ((endpoint = channelList.get(channel)) != null) {
			endpoint.sendMessage(message);
		} else {
			throw new ChannelUnavailableException();
		}
	}

	@ClientEndpoint
	public class WsEndpoint {
		private String channel;
		private Session session;
		private List<WsListener> messageListeners = new ArrayList<>();

		//
		public WsEndpoint(String channel, WsListener callback) {
			this.channel = channel;
			addMessageListener(callback);
		}

		//
		@OnOpen
		public void onOpen(Session session) {
			this.session = session;
			channelList.put(this.channel, this);
			latch.countDown();
		}

		//
		@OnMessage
		public void onMessage(String message, Session session) {
			for (WsListener listener : messageListeners) {
				listener.processMessage(message);
			}
		}

		@OnClose
		public void onClose(Session session, CloseReason closeReason) {
			messageListeners = null;
		}

		//
		private void sendMessage(String message) {
			session.getAsyncRemote().sendText(message);
		}

		//
		private synchronized void addMessageListener(WsListener listener) {
			messageListeners.add(listener);
		}

		//
		private synchronized void removeMessageListener(WsListener listener) {
			messageListeners.add(listener);
		}
	}

}
