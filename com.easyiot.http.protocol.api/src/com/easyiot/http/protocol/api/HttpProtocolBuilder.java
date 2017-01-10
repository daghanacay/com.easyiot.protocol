package com.easyiot.http.protocol.api;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.dto.DTO;

import osgi.enroute.dto.api.TypeReference;

/**
 * Builder for the HttpProtocol
 * 
 * @author daghan
 *
 */
@ProviderType
public interface HttpProtocolBuilder {
	/**
	 * Creates the headers for the protocol.
	 * 
	 * @param headers
	 * @return
	 */
	public HttpProtocolBuilder setHeaders(Map<String, String> headers);

	/**
	 * Add headers to the existing protocol.
	 * 
	 * @param headers
	 * @return
	 */
	public HttpProtocolBuilder addHeader(String key, String value);

	/**
	 * Sets body from a String
	 * 
	 * @param body
	 * @return
	 */
	public HttpProtocolBuilder setBody(String body);

	/**
	 * Sets body from an random object
	 * 
	 * @param body
	 * @return
	 */
	public <T> HttpProtocolBuilder setBody(T body);

	/**
	 * Returns the content of response in String form
	 * 
	 * @return
	 */
	public String returnContent();

	/**
	 * Returns the content of response as an object
	 * 
	 * @return
	 */
	public <T> T returnContent(Class<T> clazz);

	/**
	 * Returns based on type reference for generic classes
	 * 
	 * new TypeReference<List<AusloraDeviceDto>>() {}
	 * 
	 * @param ref
	 * @return
	 */
	public <T> T returnContent(TypeReference<T> ref);

}
