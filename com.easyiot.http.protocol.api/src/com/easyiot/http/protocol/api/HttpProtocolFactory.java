package com.easyiot.http.protocol.api;

/**
 * Protocol factory to generate protocol bypassing the OSGi registry.
 * 
 * @author daghan
 *
 */
public interface HttpProtocolFactory {

	/**
	 * Returns an instance of protocol factory.
	 * 
	 * @param url
	 * @return
	 */
	public HttpProtocol getInstance(String url);

}
