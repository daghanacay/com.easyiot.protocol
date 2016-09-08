package com.easyiot.bluetooth.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Thrown if any of the IO exception occurs during SPP communication.
 * 
 * @author daghan
 *
 */
public class SppCommFailed extends FrameworkException {
	public SppCommFailed(Throwable t) {
		super(t.getMessage());
	}
}
