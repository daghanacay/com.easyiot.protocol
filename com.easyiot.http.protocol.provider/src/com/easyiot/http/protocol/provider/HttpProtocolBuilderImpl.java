package com.easyiot.http.protocol.provider;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Reference;

import com.easyiot.http.protocol.api.HttpProtocolBuilder;
import com.easyiot.http.protocol.api.exception.ConnectionProblemException;
import com.easyiot.http.protocol.api.exception.JsonConversionException;

import osgi.enroute.dto.api.DTOs;

public class HttpProtocolBuilderImpl implements HttpProtocolBuilder {

	@Reference
	DTOs dtosService;

	// Internal delegation to http client
	Request httpClientRequest;

	public HttpProtocolBuilderImpl(Request httpClientRequest) {
		this.httpClientRequest = httpClientRequest;
	}

	public Request getHttpClientRequest() {
		return httpClientRequest;
	}

	@Override
	public HttpProtocolBuilder setHeaders(Map<String, String> headers) {
		for (Entry<String, String> entry : headers.entrySet()) {
			httpClientRequest.addHeader(entry.getKey(), entry.getValue());
		}
		return this;
	}

	@Override
	public HttpProtocolBuilder addHeader(String key, String value) {
		httpClientRequest.addHeader(key, value);
		return this;
	}

	@Override
	public <T extends DTO> HttpProtocolBuilder setBody(T body) {
		httpClientRequest.bodyString(body.toString(), ContentType.APPLICATION_JSON);
		return this;
	}

	@Override
	public <T> HttpProtocolBuilder setBody(T body) {
		try {
			httpClientRequest.bodyString(dtosService.encoder(body).put(), ContentType.APPLICATION_JSON);
		} catch (Exception e) {
			throw new ConnectionProblemException();
		}
		return this;
	}

	@Override
	public HttpProtocolBuilder setBody(String body) {
		httpClientRequest.bodyString(body, ContentType.APPLICATION_JSON);
		return this;
	}

	@Override
	public String returnContent() {
		try {
			return httpClientRequest.execute().returnContent().asString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionProblemException();
		}
	}

	@Override
	public <T> T returnContentObj(Class<T> clazz) {
		try {
			return dtosService.decoder(clazz).get(returnContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new JsonConversionException();
		}
	}
}
