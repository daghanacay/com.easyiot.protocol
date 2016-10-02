package com.easyiot.auslora_websocket.protocol.api.dto;

import org.osgi.dto.DTO;

/**
 * Auslora data specification for sending the gateway information that received
 * the sensor data.
 * 
 * @author daghan
 *
 */
public class AusloraGatewayDTO extends DTO {
	public int rssi = -56;
	public double snr = 11.8;
	public long ts = 1475295409650L;
	public String gweui = "02-4B-08-FF-FF-0E-02-13.0";
}
