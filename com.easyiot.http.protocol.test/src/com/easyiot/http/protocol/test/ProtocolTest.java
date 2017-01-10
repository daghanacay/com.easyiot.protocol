package com.easyiot.http.protocol.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.easyiot.base.test.util.IntegrationTestBase;
import com.easyiot.http.protocol.api.HttpProtocol;

import osgi.enroute.configurer.api.RequireConfigurerExtender;

@RequireConfigurerExtender
public class ProtocolTest extends IntegrationTestBase {

	@Test
	public void testProtocol() throws Exception {
		Assert.assertNotNull(context);
	}

	@Test
	public void testHttpService() throws Exception {
		// This will create the component through confguration.
		Map<String, String> properties = new HashMap<>();
		// See com.easyiot.http.protocol.provider HttpProtocolConfiguration
		properties.put("id", "test.service");
		properties.put("url", "http://google.com");
		pushFactoryConfig(properties, "com.easyiot.http.protocol");
		HttpProtocol protocol = getService(HttpProtocol.class);
		String result = protocol.GET().returnContent();
		Assert.assertNotNull(result);
	}

}
