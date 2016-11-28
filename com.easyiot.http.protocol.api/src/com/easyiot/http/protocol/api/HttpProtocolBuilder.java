package com.easyiot.http.protocol.api;

import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.dto.DTO;

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
	 * Sets body from an DTO object
	 * 
	 * @param body
	 * @return
	 */
	public <T extends DTO> HttpProtocolBuilder setBody(T body);

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
	 * Returns the content of response as a generic object
	 * 
	 * @return
	 */
	public <T> T returnContentObj(Class<T> clazz);

}
