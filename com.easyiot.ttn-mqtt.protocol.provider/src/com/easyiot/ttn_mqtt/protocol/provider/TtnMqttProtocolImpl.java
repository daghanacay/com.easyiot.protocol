package com.easyiot.ttn_mqtt.protocol.provider;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import java.io.IOException;
import java.util.Base64;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.mqtt.protocol.api.MessageListener;
import com.easyiot.mqtt.protocol.api.MqttProtocol;
import com.easyiot.mqtt.protocol.api.MqttProtocolFactory;
import com.easyiot.ttn_mqtt.protocol.api.TtnMqttMessageListener;
import com.easyiot.ttn_mqtt.protocol.api.TtnMqttProtocol;
import com.easyiot.ttn_mqtt.protocol.api.dto.TtnMetaDataDTO;
import com.easyiot.ttn_mqtt.protocol.provider.configuration.TtnMqttConfiguration;

import osgi.enroute.dto.api.DTOs;

/**
 * Implementation of {@link IMqttClient}
 */

@Designate(ocd = TtnMqttConfiguration.class, factory = true)
@Component(name = "com.easyiot.ttn-mqtt.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
public final class TtnMqttProtocolImpl implements TtnMqttProtocol {
	/**
	 * MQTT Configuration
	 */
	private TtnMqttConfiguration ttnMqttConfiguration;

	private MqttProtocol myMqqttProtocol;

	/**
	 * Log Service Reference
	 */
	@Reference(cardinality = OPTIONAL)
	private volatile LogService logService;

	@Reference
	MqttProtocolFactory mqttProtocolFactory;

	@Reference
	private DTOs dtoConverter;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(TtnMqttConfiguration conf) throws IOException {
		this.ttnMqttConfiguration = conf;
		setupMqtt();
	}

	private void setupMqtt() {
		myMqqttProtocol = mqttProtocolFactory.getSecureInstance(ttnMqttConfiguration.username(),
				ttnMqttConfiguration.userPassword(), ttnMqttConfiguration.host(), ttnMqttConfiguration.port());

	}

	@Override
	public String getId() {
		return ttnMqttConfiguration.id();
	}

	@Override
	public boolean connect() {
		return myMqqttProtocol.connect();
	}

	@Override
	public void disconnect() {
		myMqqttProtocol.disconnect();
	}

	@Override
	public void publish(String channel, String payload) {
		myMqqttProtocol.publish(channel, payload);
	}

	@Override
	public void subscribe(String channel, TtnMqttMessageListener callback) {
		MessageListener myMessageListener = new MessageListener() {
			@Override
			public void processMessage(String message) {
				TtnMetaDataDTO newMetaData;
				try {
					newMetaData = dtoConverter.decoder(TtnMetaDataDTO.class).get(message);
					// Decode sensor data
					newMetaData.payload = new String(Base64.getDecoder().decode(newMetaData.payload));
					callback.processMessage(newMetaData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		myMqqttProtocol.subscribe(channel, myMessageListener);
	}

	@Override
	public void unsubscribe(String channel) {
		myMqqttProtocol.unsubscribe(channel);
	}

}
