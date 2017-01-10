package com.easyiot.http.protocol.provider;

import org.apache.http.client.fluent.Request;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.http.protocol.api.HttpProtocol;
import com.easyiot.http.protocol.api.HttpProtocolBuilder;
import com.easyiot.http.protocol.provider.configuration.HttpProtocolConfiguration;

import osgi.enroute.dto.api.DTOs;

/**
 * Implementation of the HttpProtocol based on Apache HTTP client
 */

@Component(name = "com.easyiot.http.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = HttpProtocolConfiguration.class, factory = true)
public class HttpProtocolImpl implements HttpProtocol {

	@Reference
	private DTOs dtosService;

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
		return new HttpProtocolBuilderImpl(Request.Get(configuration.url()), dtosService);
	}

	@Override
	public HttpProtocolBuilder POST() {
		return new HttpProtocolBuilderImpl(Request.Post(configuration.url()), dtosService);
	}

	@Override
	public HttpProtocolBuilder PUT() {
		return new HttpProtocolBuilderImpl(Request.Put(configuration.url()), dtosService);
	}

	@Override
	public HttpProtocolBuilder DELETE() {
		return new HttpProtocolBuilderImpl(Request.Delete(configuration.url()), dtosService);
	}

	@Override
	public String getId() {
		return configuration.id();
	}

}
