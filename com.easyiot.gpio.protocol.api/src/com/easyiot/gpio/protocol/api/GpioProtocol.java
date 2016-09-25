package com.easyiot.gpio.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;
import com.easyiot.gpio.protocol.api.exception.NotInputPinException;
import com.easyiot.gpio.protocol.api.exception.NotOutputPinException;
import com.easyiot.gpio.protocol.api.exception.PinAlreadyConfiguredException;

@ProviderType
public interface GpioProtocol extends Protocol {

	/**
	 * Configures a pin as input or output if it is not configured.
	 * 
	 * @param pinNumber
	 *            the pin to be configured
	 * @param pinFunction
	 *            input or output pin
	 * @param defaultValue
	 *            default output for output pins and pull resistance for input
	 *            pins
	 */
	public void configurePin(PinNumberEnum pinNumber, InputOutputEnum pinFunction, PinLevelEnum defaultValue)
			throws PinAlreadyConfiguredException;

	/**
	 * Same as
	 * {@link #configurePin(PinNumberEnum, InputOutputEnum, PinLevelEnum)} but
	 * instead of throwing an exception it resets the pin and reconfigures it.
	 * It potentially break the working of some other device.
	 * 
	 * @param pinNumber
	 *            the pin to be configured
	 * @param pinFunction
	 *            input or output pin
	 * @param defaultValue
	 *            default output for output pins and pull resistance for input
	 *            pins
	 */
	public void forceConfigurePin(PinNumberEnum pinNumber, InputOutputEnum pinFunction, PinLevelEnum defaultValue);

	/**
	 * Reads the value of a pin which is already set as an input pin.
	 * 
	 * @param pinNumber
	 * @return
	 * @throws NotInputPinException
	 */
	public boolean readPinValue(PinNumberEnum pinNumber) throws NotInputPinException;

	/**
	 * Writes a value on the pin which is already set as input pin.
	 * 
	 * @param pinNumber
	 * @param value
	 * @throws NotOutputPinException
	 */
	public void writePinValue(PinNumberEnum pinNumber, boolean value) throws NotOutputPinException;

}
