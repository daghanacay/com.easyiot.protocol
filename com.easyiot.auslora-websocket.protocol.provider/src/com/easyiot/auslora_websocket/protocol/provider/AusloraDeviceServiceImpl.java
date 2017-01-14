package com.easyiot.auslora_websocket.protocol.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.auslora_websocket.protocol.api.AusloraDeviceService;
import com.easyiot.auslora_websocket.protocol.api.dto.ABPDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceListDto;
import com.easyiot.auslora_websocket.protocol.api.dto.OTAADeviceDto;
import com.easyiot.http.protocol.api.HttpProtocol;
import com.easyiot.http.protocol.api.HttpProtocolFactory;
import com.easyiot.websocket.protocol.provider.configuration.AusloraDeviceServiceConfig;

@Designate(ocd = AusloraDeviceServiceConfig.class)
@Component(name = "com.easyiot.auslora-websocket.device.service", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AusloraDeviceServiceImpl implements AusloraDeviceService {
	AusloraDeviceServiceConfig config;

	@Reference
	private HttpProtocolFactory httpFactory;

	@Activate
	private void activate(AusloraDeviceServiceConfig config) {
		this.config = config;
	}

	@Override
	public AusloraDeviceListDto getDevices(String applicationId) {
		HttpProtocol protocol = httpFactory
				.getInstance(String.format("https://%s/1/nwk/app/%s/devices", config.adminUrl(), applicationId));

		return protocol.GET().addHeader("Authorization", "Bearer " + config.apiKey())
				.returnContent(AusloraDeviceListDto.class);
	}

	@Override
	public AusloraDeviceDto getDevice(String applicationId, String deviceId) {
		HttpProtocol protocol = httpFactory.getInstance(
				String.format("https://%s/1/nwk/app/%s/device/%s", config.adminUrl(), applicationId, deviceId));

		return protocol.GET().addHeader("Authorization", "Bearer " + config.apiKey())
				.returnContent(AusloraDeviceDto.class);
	}

	@Override
	public AusloraDeviceDto registerOTAADevice(String applicationId, OTAADeviceDto device) {
		HttpProtocol protocol = httpFactory
				.getInstance(String.format("https://%s/1/nwk/app/%s/devices/otaa", config.adminUrl(), applicationId));
		return protocol.POST().addHeader("Authorization", "Bearer " + config.apiKey()).setBody(device)
				.returnContent(AusloraDeviceDto.class);
	}

	@Override
	public AusloraDeviceDto registerABPDevice(String applicationId, ABPDeviceDto device) {
		HttpProtocol protocol = httpFactory
				.getInstance(String.format("https://%s/1/nwk/app/%s/devices/abp", config.adminUrl(), applicationId));
		return protocol.POST().addHeader("Authorization", "Bearer " + config.apiKey()).setBody(device)
				.returnContent(AusloraDeviceDto.class);
	}

	@Override
	public String deleteDevice(String applicationId, String deviceId) {
		HttpProtocol protocol = httpFactory.getInstance(
				String.format("https://%s/1/nwk/app/%s/device/%s", config.adminUrl(), applicationId, deviceId));
		return protocol.DELETE().addHeader("Authorization", "Bearer " + config.apiKey()).returnContent().toString();
	}

}
