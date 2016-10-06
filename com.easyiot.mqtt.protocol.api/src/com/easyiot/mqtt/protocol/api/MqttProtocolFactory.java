package com.easyiot.mqtt.protocol.api;

public interface MqttProtocolFactory {
	public MqttProtocol getSecureInstance(String username, String password, String host, int port);
}
