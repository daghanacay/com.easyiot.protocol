package com.easyiot.websocket.protocol.provider;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import java.io.IOException;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
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

	@Override
	public void connect(String channel, WsListener callback) {
		latch = new CountDownLatch(1);
		WsEndpoint endpoint;
		if ((endpoint = channelList.get(channel)) != null) {
			// If we already created the channel just add the callback to the
			// existing channel
			endpoint.addMessageListener(callback);
		} else {
			try {
				String connectionUri;
				if (80 == configuration.port()) {
					connectionUri = String.format("%s://%s/%s", configuration.protocol(), configuration.host(),
							channel);
				} else {
					connectionUri = String.format("%s://%s:%s/%s", configuration.protocol(), configuration.host(),
							configuration.port(), channel);
				}
				WsEndpoint newEndPoint = new WsEndpoint(new URI(connectionUri), callback);
				trustAllHosts(newEndPoint);
				newEndPoint.connect();
				// Wait for 5 sec to see if it connects. Released in
				// WsEndpoint.onOpen
				if (latch.await(5, TimeUnit.SECONDS)) {
					channelList.put(channel, newEndPoint);
				} else {
					throw new ChannelConnectException("Connection timeout.");
				}
			} catch (Throwable e) {
				throw new ChannelConnectException(e);
			}
		}
	}

	/**
	 * Sets the websocket client to trust all the server certificates
	 * 
	 * @param endpoint
	 */
	private void trustAllHosts(WsEndpoint endpoint) {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				// getAcceptedIssuers
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// checking client certificate
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// checking server certificate
			}

		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			endpoint.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnectAll() {

		for (Entry<String, WsEndpoint> endpoint : channelList.entrySet()) {
			endpoint.getValue().removeAllMessageListeners();
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
			endpoint.send(message);
		} else {
			throw new ChannelUnavailableException();
		}
	}

	public class WsEndpoint extends WebSocketClient {
		private static final int CONNECTION_SUCCESSFUL = 101;
		private List<WsListener> messageListeners = new ArrayList<>();

		public WsEndpoint(URI serverURI, WsListener callback) {
			super(serverURI);
			addMessageListener(callback);
		}

		public void removeAllMessageListeners() {
			messageListeners.clear();
		}

		@Override
		public void onOpen(ServerHandshake handShake) {
			if (handShake.getHttpStatus() != CONNECTION_SUCCESSFUL) {
				throw new ChannelConnectException(handShake.getContent().toString());
			}
			latch.countDown();
		}

		@Override
		public void onMessage(String message) {
			for (WsListener listener : messageListeners) {
				listener.processMessage(message);
			}
		}

		@Override
		public void onClose(int arg0, String arg1, boolean arg2) {
			messageListeners = null;
		}

		@Override
		public void onError(Exception msg) {
			logService.log(LogService.LOG_ERROR, msg.getMessage());
		}

		private synchronized void addMessageListener(WsListener listener) {
			messageListeners.add(listener);
		}

		private synchronized void removeMessageListener(WsListener listener) {
			messageListeners.add(listener);
		}

	}
}
