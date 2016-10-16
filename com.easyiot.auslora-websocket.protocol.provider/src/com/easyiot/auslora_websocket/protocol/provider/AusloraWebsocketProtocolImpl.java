package com.easyiot.auslora_websocket.protocol.provider;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketListener;
import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketProtocol;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraMetadataDTO;
import com.easyiot.websocket.protocol.api.WebsocketProtocol;
import com.easyiot.websocket.protocol.api.WebsocketProtocolFactory;
import com.easyiot.websocket.protocol.api.WsListener;
import com.easyiot.websocket.protocol.provider.configuration.AusloraWebsocketConfiguration;

import osgi.enroute.dto.api.DTOs;

@Designate(ocd = AusloraWebsocketConfiguration.class, factory = true)
@Component(name = "com.easyiot.auslora-websocket.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
public class AusloraWebsocketProtocolImpl implements AusloraWebsocketProtocol {
	private Map<String, AusloraWebsocketListener> deviceMap = new ConcurrentHashMap<>();
	private Map<AusloraWebsocketListener, WsListener> callbackMap = new ConcurrentHashMap<>();
	private WebsocketProtocol wsClient;
	private AusloraWebsocketConfiguration configuration;
	@Reference
	private DTOs dtoConverter;

	@Reference
	private WebsocketProtocolFactory websocketProtocolFactory;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(AusloraWebsocketConfiguration configuration) throws IOException {
		this.configuration = configuration;
		wsClient = websocketProtocolFactory.getWSSInstance(configuration.host(), configuration.port());
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
					AusloraWebsocketListener existingDevice = deviceMap.get(newMetaData.EUI);
					if (existingDevice != null) {
						deviceMap.get(newMetaData.EUI).processMessage(newMetaData);
					}
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

	@Override
	public void disconnectAll() {
		deviceMap.clear();
		callbackMap.clear();
		wsClient.disconnectAll();
	}
}
