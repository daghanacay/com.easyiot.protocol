package com.easyiot.http.protocol.provider;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.easyiot.http.protocol.api.HttpProtocolBuilder;
import com.easyiot.http.protocol.api.exception.ConnectionProblemException;
import com.easyiot.http.protocol.api.exception.JsonConversionException;

import osgi.enroute.dto.api.DTOs;
import osgi.enroute.dto.api.TypeReference;

public class HttpProtocolBuilderImpl implements HttpProtocolBuilder {

	private DTOs dtosService;

	// Internal delegation to http client
	private Request httpClientRequest;

	public HttpProtocolBuilderImpl(Request httpClientRequest, DTOs dtosService) {
		this.httpClientRequest = httpClientRequest;
		this.dtosService = dtosService;
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
	public <T> T returnContent(Class<T> clazz) {
		try {
			return dtosService.decoder(clazz).get(returnContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new JsonConversionException();
		}
	}

	@Override
	public <T> T returnContent(TypeReference<T> ref) {
		try {
			return dtosService.decoder(ref).get(returnContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new JsonConversionException();
		}
	}
}
