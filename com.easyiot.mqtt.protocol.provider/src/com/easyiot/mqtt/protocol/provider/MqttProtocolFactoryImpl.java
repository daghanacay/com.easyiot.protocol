package com.easyiot.mqtt.protocol.provider;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import com.easyiot.base.util.EasyIotOsgiUtil;
import com.easyiot.mqtt.protocol.api.MqttProtocol;
import com.easyiot.mqtt.protocol.api.MqttProtocolFactory;

@Component
public class MqttProtocolFactoryImpl implements MqttProtocolFactory {

	@Override
	public MqttProtocol getSecureInstance(String username, String password, String host, int port) {
		MqttProtocol returnVal = null;
		Map<String, String> properties = new HashMap<>();
		String randomId = String.valueOf(new Random().nextInt());
		// properties required by the component that needs to be
		properties.put("id", randomId);
		properties.put("username", username);
		properties.put("userPassword", password);
		properties.put("host", host);
		properties.put("port", String.valueOf(port));
		// Additional property to find the created service
		properties.put("factoryId", "factoryId-" + randomId);

		try {
			// push the configuration and let the OSGi create the service
			EasyIotOsgiUtil.pushFactoryConfig(properties, "com.easyiot.mqtt.protocol");
			// find the created service from the additional parameter
			returnVal = EasyIotOsgiUtil.getService("(factoryId=factoryId-" + randomId + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}

}
