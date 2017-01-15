package com.easyiot.websocket.protocol.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;

import com.easyiot.base.util.EasyIotOsgiUtil;
import com.easyiot.websocket.protocol.api.WebsocketProtocol;
import com.easyiot.websocket.protocol.api.WebsocketProtocolFactory;
import com.easyiot.websocket.protocol.provider.configuration.ProtocolEnum;

@Component
public class WebsocketProtocolFactoryImpl implements WebsocketProtocolFactory {
	@Override
	public WebsocketProtocol getWSSInstance(String host, int port) {
		WebsocketProtocol returnVal = null;
		Map<String, String> properties = new HashMap<>();
		String randomId = String.valueOf(new Random().nextInt());
		// properties required by the component that needs to be
		properties.put("id", randomId);
		properties.put("protocol", ProtocolEnum.wss.toString());
		properties.put("port", String.valueOf(port));
		properties.put("host", String.valueOf(host));
		// Additional property to find the created service
		properties.put("factoryId", "factoryId-" + randomId);

		try {
			// push the configuration and let the OSGi create the service
			EasyIotOsgiUtil.pushFactoryConfig(properties, "com.easyiot.http.protocol");
			// find the created service from the additional parameter
			returnVal = EasyIotOsgiUtil.getService("(factoryId=factoryId-" + randomId + ")");
		} catch (IOException | InterruptedException | InvalidSyntaxException e) {
			e.printStackTrace();
		}

		return returnVal;
	}

}
