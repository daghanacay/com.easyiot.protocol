package com.easyiot.auslora_websocket.protocol.api;

import com.easyiot.auslora_websocket.protocol.api.dto.ABPDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceListDto;
import com.easyiot.auslora_websocket.protocol.api.dto.OTAADeviceDto;

public interface AusloraDeviceService {

	AusloraDeviceListDto getDevices(String applicationId);

	AusloraDeviceDto getDevice(String applicationId, String deviceId);

	AusloraDeviceDto registerOTAADevice(String applicationId, OTAADeviceDto device);

	AusloraDeviceDto registerABPDevice(String applicationId, ABPDeviceDto device);
	
	String deleteDevice(String applicationId, String deviceId);

}
