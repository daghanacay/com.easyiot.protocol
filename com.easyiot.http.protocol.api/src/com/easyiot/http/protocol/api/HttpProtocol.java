package com.easyiot.http.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

/**
 * Defines API for HttpProtocol. Uses builder pattern for maximum flexibility.
 */
@ProviderType
public interface HttpProtocol extends Protocol {
	/**
	 * Returns a builder to make a GET call. It should create a specific
	 * instance to prevent concurrency problems.
	 * 
	 * @return
	 */
	public HttpProtocolBuilder GET();

	/**
	 * Returns a builder to make a POST call. It should create a specific
	 * instance to prevent concurrency problems.
	 * 
	 * @return
	 */
	public HttpProtocolBuilder POST();

	/**
	 * Returns a builder to make a PUT call. It should create a specific
	 * instance to prevent concurrency problems.
	 * 
	 * @return
	 */
	public HttpProtocolBuilder PUT();

	/**
	 * Returns a builder to make a DELETE call. It should create a specific
	 * instance to prevent concurrency problems.
	 * 
	 * @return
	 */
	public HttpProtocolBuilder DELETE();

}
