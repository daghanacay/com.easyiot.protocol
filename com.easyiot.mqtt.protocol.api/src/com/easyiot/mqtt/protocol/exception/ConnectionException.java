package com.easyiot.mqtt.protocol.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * When there is a problem with connection such as IO exception or authorization exception.
 * 
 * @author daghan
 *
 */
public class ConnectionException extends FrameworkException {
	private static final long serialVersionUID = 1L;
	
	public ConnectionException(final String message) {
		super(message);
	}
}
