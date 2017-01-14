package com.easyiot.auslora_websocket.protocol.provider.rest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.easyiot.auslora_websocket.protocol.api.AusloraDeviceService;
import com.easyiot.auslora_websocket.protocol.api.dto.ABPDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceListDto;
import com.easyiot.auslora_websocket.protocol.api.dto.OTAADeviceDto;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.rest.api.REST;
import osgi.enroute.rest.api.RESTRequest;

@Component(immediate = true, property = { REST.ENDPOINT + "=/auslora/*" })
public class AusloraDeviceServiceRestImpl implements REST {
	@Reference
	AusloraDeviceService ausloraDeviceService;

	@Reference
	DTOs dtoService;

	// GET application/{ApplicationId}/device/{deviceId}
	public AusloraDeviceDto getApplication(RESTRequest rr, String applicationId, String deviceString, String deviceId) {
		return ausloraDeviceService.getDevice(applicationId, deviceId);
	}

	// GET application/{ApplicationId}/devices
	public AusloraDeviceListDto getApplication(RESTRequest rr, String applicationId, String deviceString) {
		return ausloraDeviceService.getDevices(applicationId);
	}

	// DELETE application/{ApplicationId}/device/{deviceId}
	public String deleteApplication(RESTRequest rr, String applicationId, String deviceString, String deviceId) {
		return ausloraDeviceService.deleteDevice(applicationId, deviceId);
	}

	private interface DeviceRegisterRequest extends RESTRequest {
		String _body();
	}

	// POST application/{ApplicationId}/otaa with OTAADeviceDto body
	// OR application/{ApplicationId}/abp with AbpDeviceDto body
	public AusloraDeviceDto postApplication(DeviceRegisterRequest rr, String applicationId, String importType)
			throws Exception {
		switch (importType) {
		case "otaa":
			return ausloraDeviceService.registerOTAADevice(applicationId,
					dtoService.decoder(OTAADeviceDto.class).get(rr._body()));
		case "abp":
			return ausloraDeviceService.registerABPDevice(applicationId,
					dtoService.decoder(ABPDeviceDto.class).get(rr._body()));
		default:
			throw new RuntimeException("Unkwon importType");
		}
	}
}
