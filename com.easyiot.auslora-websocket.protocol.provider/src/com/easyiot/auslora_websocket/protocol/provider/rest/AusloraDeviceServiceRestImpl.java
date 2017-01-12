package com.easyiot.auslora_websocket.protocol.provider.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.easyiot.auslora_websocket.protocol.api.AusloraDeviceService;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceListDto;

import osgi.enroute.rest.api.REST;
import osgi.enroute.rest.api.RESTRequest;

@Component(immediate = true, property = {
		HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN + "=/auslora/application/*" })
public class AusloraDeviceServiceRestImpl implements REST {
	@Reference
	AusloraDeviceService ausloraDeviceService;

	public AusloraDeviceListDto getDevices(RESTRequest rr, String applicationId) {
		return ausloraDeviceService.getDevices(applicationId);
	}

}
