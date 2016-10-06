package com.easyiot.mqtt.protocol.provider;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Random;

import com.easyiot.mqtt.protocol.api.MqttProtocol;
import com.easyiot.mqtt.protocol.api.MqttProtocolFactory;
import com.easyiot.mqtt.protocol.provider.configuration.MqttConfiguration;

public class MqttProtocolFactoryImpl implements MqttProtocolFactory{

	@Override
	public MqttProtocol getSecureInstance(String username, String password, String host, int port) {
		MqttProtocolImpl returnVal = new MqttProtocolImpl();
		MqttConfiguration myConfig= new MqttConfiguration() {
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return MqttConfiguration.class;
			}
			
			@Override
			public String username() {
				return username;
			}
			
			@Override
			public String userPassword() {
				return password;
			}
			
			@Override
			public int port() {
				return port;
			}
			
			@Override
			public String id() {
				return String.valueOf(new Random().nextInt());
			}
			
			@Override
			public String host() {
				return host;
			}
		};
		try {
			returnVal.activate(myConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

}
