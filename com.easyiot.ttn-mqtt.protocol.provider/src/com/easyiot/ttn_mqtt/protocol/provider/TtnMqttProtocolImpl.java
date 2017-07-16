package com.easyiot.ttn_mqtt.protocol.provider;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

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
@Component(name = "com.easyiot.ttn-mqtt.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
public final class TtnMqttProtocolImpl implements TtnMqttProtocol {
	Promise<MqttProtocol> mqttProtocolPromise;

	@Reference
	Executor executor;

	/**
	 * MQTT Configuration
	 */
	private TtnMqttConfiguration ttnMqttConfiguration;

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
		Deferred<MqttProtocol> deferred = new Deferred<>();
		executor.execute(() -> deferred.resolve(mqttProtocolFactory.getSecureInstance(ttnMqttConfiguration.username(),
				ttnMqttConfiguration.userPassword(), ttnMqttConfiguration.host(), ttnMqttConfiguration.port())));
		mqttProtocolPromise = deferred.getPromise();
	}

	@Override
	public String getId() {
		return ttnMqttConfiguration.id();
	}

	@Override
	public boolean connect() {
		return getMyProtocol().connect();
	}

	private MqttProtocol getMyProtocol() {

		try {
			return mqttProtocolPromise.getValue();
		} catch (InvocationTargetException | InterruptedException e) {
			logService.log(LogService.LOG_ERROR, "protocol factory is not resolved");
			return null;
		}
	}

	@Override
	public void disconnect() {
		getMyProtocol().disconnect();
	}

	@Override
	public void publish(String channel, String payload) {
		getMyProtocol().publish(channel, payload);
	}

	@Override
	public void subscribe(String channel, TtnMqttMessageListener callback) {
		MessageListener myMessageListener = new MessageListener() {
			@Override
			public void processMessage(String message) {
				TtnMetaDataDTO newMetaData;
				try {
					newMetaData = dtoConverter.decoder(TtnMetaDataDTO.class).get(message);
					callback.processMessage(newMetaData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		getMyProtocol().subscribe(channel, myMessageListener);
	}

	@Override
	public void unsubscribe(String channel) {
		getMyProtocol().unsubscribe(channel);
	}

}
