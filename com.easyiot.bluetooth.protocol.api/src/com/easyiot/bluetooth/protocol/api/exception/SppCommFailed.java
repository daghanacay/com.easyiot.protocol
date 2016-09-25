package com.easyiot.bluetooth.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Thrown if any of the IO exception occurs during SPP communication.
 * 
 * @author daghan
 *
 */
public class SppCommFailed extends FrameworkException {
	private static final long serialVersionUID = 1L;

	public SppCommFailed(Throwable t) {
		super(t.getMessage());
	}
}
