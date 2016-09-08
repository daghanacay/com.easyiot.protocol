package com.easyiot.bluetooth.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Thrown when an error happens during searching bluetooth devices.
 * 
 * @author daghan
 *
 */
public class BluetoothException extends FrameworkException {

	public BluetoothException(Throwable e1) {
		super(e1.getMessage());
	}

	public BluetoothException(String msg) {
		super(msg);
	}

}
