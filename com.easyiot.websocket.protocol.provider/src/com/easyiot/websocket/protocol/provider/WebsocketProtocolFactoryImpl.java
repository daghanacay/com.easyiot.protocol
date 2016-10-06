package com.easyiot.websocket.protocol.provider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import com.easyiot.websocket.protocol.api.WebsocketProtocol;
import com.easyiot.websocket.protocol.api.WebsocketProtocolFactory;
import com.easyiot.websocket.protocol.provider.configuration.ProtocolEnum;
import com.easyiot.websocket.protocol.provider.configuration.WebsocketConfiguration;

@Component
public class WebsocketProtocolFactoryImpl implements WebsocketProtocolFactory {

	@Override
	public WebsocketProtocol getWSSInstance(String host, int port) {
		WebsocketProtocolImpl returnVal = new WebsocketProtocolImpl();
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
				return port;
			}

			@Override
			public String id() {
				return String.valueOf(new Random().nextLong());
			}

			@Override
			public String host() {
				return host;
			}
		};
		try {
			returnVal.activate(wsConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

}
