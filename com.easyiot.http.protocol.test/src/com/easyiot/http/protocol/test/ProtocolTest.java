package com.easyiot.http.protocol.test;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.util.tracker.ServiceTracker;

import com.easyiot.http.protocol.api.HttpProtocol;

import osgi.enroute.configurer.api.RequireConfigurerExtender;

@RequireConfigurerExtender
public class ProtocolTest {

	private final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	@Test
	public void testProtocol() throws Exception {
		Assert.assertNotNull(context);
	}

	@Test
	public void testHttpService() throws Exception {
		// This will create the component through confguration.
		pushConfig();
		HttpProtocol protocol = getService(HttpProtocol.class);
		String result = protocol.GET().returnContent();
		Assert.assertNotNull(result);
	}

	private <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

	private void pushConfig() throws IOException {
		ServiceReference configurationAdminReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (configurationAdminReference != null) {
			ConfigurationAdmin confAdmin = (ConfigurationAdmin) context.getService(configurationAdminReference);

			Configuration configuration = confAdmin.createFactoryConfiguration("com.easyiot.http.protocol", null);
			Dictionary properties = new Hashtable<>();
			// See com.easyiot.http.protocol.provider HttpProtocolConfiguration
			properties.put("id", "test.service");
			properties.put("url", "http://google.com");
			configuration.update(properties);

		}
	}
}
