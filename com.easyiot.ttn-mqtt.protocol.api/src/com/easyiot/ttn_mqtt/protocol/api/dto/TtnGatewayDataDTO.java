package com.easyiot.ttn_mqtt.protocol.api.dto;

import org.osgi.dto.DTO;

public class TtnGatewayDataDTO extends DTO {
	public Double frequency;
	public String datarate;
	public String codingrate;
	public int gateway_timestamp;
	public String gateway_time;
	public int channel;
	public String server_time;
	public int rssi;
	public int lsnr;
	public int rfchain;
	public int crc;
	public String modulation;
	public String gateway_eui;
	public double altitude;
	public double longitude = 144.9633200;
	public double latitude = -37.8140000;
}
