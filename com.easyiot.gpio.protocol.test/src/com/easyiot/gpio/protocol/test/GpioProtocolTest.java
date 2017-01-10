package com.easyiot.gpio.protocol.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.easyiot.base.test.util.IntegrationTestBase;

public class GpioProtocolTest extends IntegrationTestBase {

	@BeforeClass
	public static void setUpContext() {
		try {

			Map<String, String> properties = new HashMap<>();
			// See com.easyiot.redis.protocol HttpProtocolConfiguration
			properties.put("id", "test.id");
			pushFactoryConfig(properties, "com.easyiot.gpio.protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProtocol() {
		assertNotNull(context);
	}
}
