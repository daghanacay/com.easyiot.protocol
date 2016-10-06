package com.easyiot.ttn_mqtt.protocol.api.dto;

import java.util.Arrays;
import java.util.List;

import org.osgi.dto.DTO;

public class TtnMetaDataDTO extends DTO{
	public String payload = "25";
	public int port;
	public int counter;
	public String dev_eui;
	public List<TtnGatewayDataDTO> metadata = Arrays.asList(new TtnGatewayDataDTO());
}
