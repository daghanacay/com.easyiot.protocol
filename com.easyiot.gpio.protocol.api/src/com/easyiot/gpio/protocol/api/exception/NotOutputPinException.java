package com.easyiot.gpio.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Exception thrown when one tries to write to pin that is not set up or set up
 * as input pin
 * 
 * @author daghan
 *
 */
public class NotOutputPinException extends FrameworkException {
	private static final long serialVersionUID = 1L;
}
