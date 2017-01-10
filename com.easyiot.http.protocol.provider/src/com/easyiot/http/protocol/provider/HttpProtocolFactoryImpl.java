package com.easyiot.http.protocol.provider;

import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.util.tracker.ServiceTracker;

import com.easyiot.http.protocol.api.HttpProtocol;
import com.easyiot.http.protocol.api.HttpProtocolFactory;

@Component
public class HttpProtocolFactoryImpl implements HttpProtocolFactory {

	protected final static BundleContext context = FrameworkUtil.getBundle(HttpProtocolFactoryImpl.class)
			.getBundleContext();

	@Override
	public HttpProtocol getInstance(String url) {
		HttpProtocolImpl returnVal = null;
		Map<String, String> properties = new HashMap<>();
		String randomId = String.valueOf(new Random().nextInt());
		properties.put("id", randomId);
		properties.put("url", url);
		properties.put("factoryId", "factoryId-" + randomId);

		try {
			pushFactoryConfig(properties, "com.easyiot.http.protocol");
			returnVal = getService("(factoryId=factoryId-" + randomId + ")");
		} catch (IOException | InterruptedException | InvalidSyntaxException e) {
			e.printStackTrace();
		}

		return returnVal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static void pushFactoryConfig(Map<String, String> propertiesMap, String factoryPid) throws IOException {

		ServiceReference configurationAdminReference = context.getServiceReference(ConfigurationAdmin.class.getName());
		if (configurationAdminReference != null) {

			ConfigurationAdmin confAdmin = (ConfigurationAdmin) context.getService(configurationAdminReference);

			Configuration configuration = confAdmin.createFactoryConfiguration(factoryPid, null);
			Dictionary properties = new Hashtable<>();
			// See com.easyiot.http.protocol.provider HttpProtocolConfiguration
			for (Entry<String, String> entry : propertiesMap.entrySet()) {
				properties.put(entry.getKey(), entry.getValue());
			}

			configuration.update(properties);

		}
	}

	protected static <T> T getService(String serviceFilter) throws InterruptedException, InvalidSyntaxException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, context.createFilter(serviceFilter), null);
		st.open();
		return st.waitForService(1000);
	}

	protected static <T> T getService(Class<T> clazz) throws InterruptedException {
		ServiceTracker<T, T> st = new ServiceTracker<>(context, clazz, null);
		st.open();
		return st.waitForService(1000);
	}

}
