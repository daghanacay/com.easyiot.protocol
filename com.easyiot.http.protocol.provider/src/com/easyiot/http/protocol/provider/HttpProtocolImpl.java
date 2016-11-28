package com.easyiot.http.protocol.provider;

import org.apache.http.client.fluent.Request;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.http.protocol.api.HttpProtocol;
import com.easyiot.http.protocol.api.HttpProtocolBuilder;
import com.easyiot.http.protocol.provider.configuration.HttpProtocolConfiguration;

/**
 * Implementation of the HttpProtocol based on Apache HTTP client
 */

@Component(name = "com.easyiot.http.protocol", configurationPolicy= ConfigurationPolicy.REQUIRE)
@Designate(ocd = HttpProtocolConfiguration.class, factory = true)
public class HttpProtocolImpl implements HttpProtocol {

	HttpProtocolConfiguration configuration;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(HttpProtocolConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public HttpProtocolBuilder GET() {
		return new HttpProtocolBuilderImpl(Request.Get(configuration.url()));
	}

	@Override
	public HttpProtocolBuilder POST() {
		return new HttpProtocolBuilderImpl(Request.Post(configuration.url()));
	}

	@Override
	public HttpProtocolBuilder PUT() {
		return new HttpProtocolBuilderImpl(Request.Put(configuration.url()));
	}

	@Override
	public HttpProtocolBuilder DELETE() {
		return new HttpProtocolBuilderImpl(Request.Delete(configuration.url()));
	}

	@Override
	public String getId() {
		return configuration.id();
	}

}
