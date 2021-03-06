package com.easyiot.auslora_websocket.protocol.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.easyiot.auslora_websocket.protocol.api.AusloraDeviceService;
import com.easyiot.auslora_websocket.protocol.api.dto.ABPDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceDto;
import com.easyiot.auslora_websocket.protocol.api.dto.AusloraDeviceListDto;
import com.easyiot.auslora_websocket.protocol.api.dto.OTAADeviceDto;
import com.easyiot.base.test.util.IntegrationTestBase;

public class AusloraWebsocketProtocolTest extends IntegrationTestBase {
	private static AusloraDeviceService unitUnderTest;

	@BeforeClass
	public static void setUpContext() throws InterruptedException {
		try {

			Map<String, String> properties = new HashMap<>();
			// See com.easyiot.redis.protocol HttpProtocolConfiguration
			properties.put("adminUrl", "adm.auslora.com.au");
			properties.put("apiKey", "AAAAEwz95eTEvbdkuxYEvxyLZBQQSTpeqwn3so8HT9os0XJDc");
			pushFactoryConfig(properties, "com.easyiot.auslora-websocket.device.service");
		} catch (IOException e) {
			e.printStackTrace();
		}
		unitUnderTest = getService(AusloraDeviceService.class);
	}

	@Test
	public void testProtocol() {
		assertNotNull(context);
	}

	@Test
	@Ignore
	public void testGetDevice() throws InterruptedException {
		try {
			testRegisterOTAADevice();
		} catch (Exception e) {// Do nothing
		}

		AusloraDeviceDto result = unitUnderTest.getDevice("BE010010", "6717E4C27ED1DB06");
		assertTrue(result._id.equals("6717E4C27ED1DB06"));
	}

	@Test
	@Ignore
	public void testGetDevices() throws InterruptedException {
		try {
			testRegisterOTAADevice();
		} catch (Exception e) {// Do nothing
		}
		AusloraDeviceListDto result = unitUnderTest.getDevices("BE010010");
		assertTrue(result.devices.size() == 1);
	}

	@Test
	@Ignore
	public void testDeleteDevice() throws InterruptedException {
		try {
			registerOTAADevice();
		} catch (Exception e) {// Do nothing
		}
		String result = unitUnderTest.deleteDevice("BE010010", "6717E4C27ED1DB06");
		assertTrue(result.isEmpty());
	}

	private AusloraDeviceDto registerOTAADevice() {
		OTAADeviceDto device = new OTAADeviceDto();
		device.deveui = "6717e4c27ed1db06";
		device.appkey = "6717e4c27ed1db066717e4c27ed1db06";

		return unitUnderTest.registerOTAADevice("BE010010", device);

	}

	@Test
	@Ignore
	public void testRegisterOTAADevice() throws InterruptedException {
		try {
			testDeleteDevice();
		} catch (Exception e) {// Do nothing
		}
		AusloraDeviceDto result = registerOTAADevice();
		assertTrue(result.appkey.equalsIgnoreCase("6717e4c27ed1db066717e4c27ed1db06"));
	}

	@Test
	@Ignore
	public void testRegisterABPDevice() throws InterruptedException {
		try {
			testDeleteDevice();
		} catch (Exception e) {// Do nothing
		}
		ABPDeviceDto device = new ABPDeviceDto();
		device.deveui = "6717e4c27ed1db06";
		device.devaddr = "6717e4c2";
		device.seqno = "-1";
		device.seqdn = "0";
		device.appskey = "6717e4c27ed1db066717e4c27ed1db06";
		device.nwkskey = "6717e4c27ed1db066717e4c27ed1db01";

		AusloraDeviceDto result = unitUnderTest.registerABPDevice("BE010010", device);
		assertTrue(result.appskey.equals("6717E4C27ED1DB066717E4C27ED1DB06"));
	}

}
