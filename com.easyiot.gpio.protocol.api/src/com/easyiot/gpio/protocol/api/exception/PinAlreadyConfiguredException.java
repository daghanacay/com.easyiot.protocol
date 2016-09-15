package com.easyiot.gpio.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

/**
 * Thrown if an already configured pin is attempted to be reconfigured
 * 
 * @author daghan
 *
 */
public class PinAlreadyConfiguredException extends FrameworkException {
	private static final long serialVersionUID = 1L;

}
