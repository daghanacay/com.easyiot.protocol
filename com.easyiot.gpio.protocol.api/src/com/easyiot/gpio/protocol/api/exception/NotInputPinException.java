package com.easyiot.gpio.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Exception thrown when one tries to read from a pin that is not set up or set
 * up as output pin
 * 
 * @author daghan
 *
 */
public class NotInputPinException extends FrameworkException {

	private static final long serialVersionUID = 1L;

}
