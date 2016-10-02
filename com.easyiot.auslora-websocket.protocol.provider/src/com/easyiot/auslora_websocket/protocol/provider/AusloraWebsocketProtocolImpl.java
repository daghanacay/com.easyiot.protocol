package com.easyiot.auslora_websocket.protocol.provider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketListener;
import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketProtocol;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraMetadataDTO;
import com.easyiot.websocket.protocol.api.WsListener;
import com.easyiot.websocket.protocol.provider.WebsocketProtocolImpl;
import com.easyiot.websocket.protocol.provider.configuration.AusloraWebsocketConfiguration;
import com.easyiot.websocket.protocol.provider.configuration.ProtocolEnum;
import com.easyiot.websocket.protocol.provider.configuration.WebsocketConfiguration;

import osgi.enroute.dto.api.DTOs;

@SuppressWarnings("restriction")
@Designate(ocd = AusloraWebsocketConfiguration.class, factory = true)
@Component(name = "com.easyiot.auslora-websocket.protocol")
public class AusloraWebsocketProtocolImpl implements AusloraWebsocketProtocol {
	private Map<String, AusloraWebsocketListener> deviceMap = new ConcurrentHashMap<>();
	private Map<AusloraWebsocketListener, WsListener> callbackMap = new ConcurrentHashMap<>();
	private WebsocketProtocolImpl wsClient;
	private AusloraWebsocketConfiguration configuration;
	@Reference
	private DTOs dtoConverter;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(AusloraWebsocketConfiguration configuration) throws IOException {
		this.configuration = configuration;
		createPrivateWebSocketProtocol(configuration);
	}

	@Override
	public String getId() {
		return configuration.id();
	}

	@Override
	public void connect(String applicationID, String deviceEUI, AusloraWebsocketListener callback) {
		deviceMap.put(deviceEUI, callback);

		WsListener wsCallback = (str) -> {
			try {
				// Only use the GW data messages
				if (str.contains("\"cmd\":\"gw\"")) {
					AusloraMetadataDTO newMetaData = dtoConverter.decoder(AusloraMetadataDTO.class).get(str);
					// Decode payload
					deviceMap.get(newMetaData.EUI).processMessage(newMetaData);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
		callbackMap.put(callback, wsCallback);
		wsClient.connect(applicationID, wsCallback);

	}

	@Override
	public void disconnect(String applicationID, String deviceEUI, AusloraWebsocketListener callback) {
		deviceMap.remove(deviceEUI);
		wsClient.disconnect(applicationID, callbackMap.remove(callback));
	}

	@Override
	public void sendMessage(String applicationID, String deviceEUI, String message) {
		wsClient.sendMessage(applicationID, message);
	}

	private void createPrivateWebSocketProtocol(AusloraWebsocketConfiguration configuration) throws IOException {
		wsClient = new WebsocketProtocolImpl();
		WebsocketConfiguration wsConfig = new WebsocketConfiguration() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return WebsocketConfiguration.class;
			}

			@Override
			public ProtocolEnum protocol() {
				return ProtocolEnum.wss;
			}

			@Override
			public int port() {
				return configuration.port();
			}

			@Override
			public String id() {
				return "AUSLORA" + configuration.id();
			}

			@Override
			public String host() {
				return configuration.host();
			}
		};

		wsClient.activate(wsConfig);
	}
}
