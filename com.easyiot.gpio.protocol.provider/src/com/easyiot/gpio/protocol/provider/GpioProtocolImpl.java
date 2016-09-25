package com.easyiot.gpio.protocol.provider;

import java.io.IOException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.gpio.protocol.api.GpioProtocol;
import com.easyiot.gpio.protocol.api.InputOutputEnum;
import com.easyiot.gpio.protocol.api.PinLevelEnum;
import com.easyiot.gpio.protocol.api.PinNumberEnum;
import com.easyiot.gpio.protocol.api.exception.NotInputPinException;
import com.easyiot.gpio.protocol.api.exception.NotOutputPinException;
import com.easyiot.gpio.protocol.api.exception.PinAlreadyConfiguredException;
import com.easyiot.gpio.protocol.provider.configuration.GpioProtocolConfiguration;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@Designate(ocd = GpioProtocolConfiguration.class, factory = true)
@Component(name = "com.easyiot.gpio.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class GpioProtocolImpl implements GpioProtocol {
	private GpioProtocolConfiguration configuration;

	@Reference(target = "(|(board.type=Model2B_Rev1)(board.type=ModelB_Plus_Rev1))")
	private GpioController gpioContoller;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(GpioProtocolConfiguration configuration) throws IOException {
		this.configuration = configuration;
	}

	@Override
	public String getId() {
		return configuration.id();
	}

	@Override
	public void configurePin(PinNumberEnum pinNumber, InputOutputEnum pinFunction, PinLevelEnum defaultValue)
			throws PinAlreadyConfiguredException {
		Pin pin = (Pin) findPin(pinNumber.getVal());
		if (isProvisioned(pin)) {
			throw new PinAlreadyConfiguredException();
		}
		provisionPin(pinFunction, defaultValue, pin);
	}

	@Override
	public void forceConfigurePin(PinNumberEnum pinNumber, InputOutputEnum pinFunction, PinLevelEnum defaultValue) {
		Pin pin = (Pin) findPin(pinNumber.getVal());
		unprovision(pin);
		provisionPin(pinFunction, defaultValue, pin);
	}

	@Override
	public boolean readPinValue(PinNumberEnum pinNumber) throws NotInputPinException {
		boolean returnVal = false;
		Pin pin = findPin(pinNumber.getVal());
		if ((pin == null) || !(pin instanceof GpioPinDigitalInput)) {
			throw new NotInputPinException();
		}
		GpioPinDigitalInput pinDigital = (GpioPinDigitalInput) pin;
		if (gpioContoller.getState(pinDigital).isHigh()) {
			returnVal = true;
		} else {
			returnVal = false;
		}

		return returnVal;
	}

	@Override
	public void writePinValue(PinNumberEnum pinNumber, boolean value) throws NotOutputPinException {
		Pin pin = findPin(pinNumber.getVal());
		if ((pin == null) || !(pin instanceof GpioPinDigitalOutput)) {
			throw new NotOutputPinException();
		}
		GpioPinDigitalOutput pinDigital = (GpioPinDigitalOutput) pin;
		if (value) {
			pinDigital.setState(PinState.HIGH);
		} else {
			pinDigital.setState(PinState.LOW);
		}
	}

	private void provisionPin(InputOutputEnum pinFunction, PinLevelEnum defaultValue, Pin pin) {
		switch (pinFunction) { // Get request sets the input pin
		case input:
			GpioPinDigitalInput digitalIn = this.gpioContoller.provisionDigitalInputPin(pin);
			switch (defaultValue) {
			case high:
				digitalIn.setPullResistance(PinPullResistance.PULL_UP);
				break;

			case low:
				digitalIn.setPullResistance(PinPullResistance.PULL_DOWN);
				break;
			}
			break;
		// Post request sets the output pin
		case output:
			GpioPinDigitalOutput digitalOut = this.gpioContoller.provisionDigitalOutputPin(pin);

			switch (defaultValue) {
			case high:
				digitalOut.setState(true);
				break;
			case low:
				digitalOut.setState(false);
				break;
			}
			break;
		}
	}

	private Pin findPin(int pinNumber) {
		return RaspiPin.getPinByName("GPIO " + pinNumber);
	}

	private void unprovision(Pin pin) {
		gpioContoller.getProvisionedPins().stream().filter(tempPin -> pin.equals(tempPin)).findAny()
				.ifPresent(tempPin -> gpioContoller.unprovisionPin(tempPin));
	}

	private boolean isProvisioned(Pin pin) {
		return gpioContoller.getProvisionedPins().stream().filter(tempPin -> pin.equals(tempPin)).findAny().isPresent();
	}

}
