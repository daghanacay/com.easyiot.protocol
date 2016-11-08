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
import com.easyiot.gpio.protocol.api.PinTypeEnum;
import com.easyiot.gpio.protocol.api.exception.NotInputPinException;
import com.easyiot.gpio.protocol.api.exception.NotOutputPinException;
import com.easyiot.gpio.protocol.api.exception.PinAlreadyConfiguredException;
import com.easyiot.gpio.protocol.provider.configuration.GpioProtocolConfiguration;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinAnalogInput;
import com.pi4j.io.gpio.GpioPinAnalogOutput;
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
	public void configurePin(PinNumberEnum pinNumber, PinTypeEnum pinType, InputOutputEnum pinFunction,
			PinLevelEnum defaultValue) throws PinAlreadyConfiguredException {
		Pin pin = getPinByName(pinNumber.getVal());
		if (isProvisioned(pin)) {
			throw new PinAlreadyConfiguredException();
		}
		provisionPin(pinFunction, defaultValue, pin, pinType);
	}

	@Override
	public void forceConfigurePin(PinNumberEnum pinNumber, PinTypeEnum pinType, InputOutputEnum pinFunction,
			PinLevelEnum defaultValue) {
		Pin pin = getPinByName(pinNumber.getVal());
		unprovision(pin);
		provisionPin(pinFunction, defaultValue, pin, pinType);
	}

	@Override
	public boolean readDigitalPinValue(PinNumberEnum pinNumber) throws NotInputPinException {
		boolean returnVal = false;
		GpioPinDigitalInput digitalIn = getProvisionedPin(pinNumber.getVal(), GpioPinDigitalInput.class);
		if (digitalIn == null) {
			throw new NotInputPinException();
		}
		if (gpioContoller.getState(digitalIn).isHigh()) {
			returnVal = true;
		} else {
			returnVal = false;
		}

		return returnVal;
	}

	@Override
	public void writeDigitalPinValue(PinNumberEnum pinNumber, boolean value) throws NotOutputPinException {
		GpioPinDigitalOutput digitalOut = getProvisionedPin(pinNumber.getVal(), GpioPinDigitalOutput.class);
		if (digitalOut == null) {
			throw new NotOutputPinException();
		}
		if (value) {
			digitalOut.setState(PinState.HIGH);
		} else {
			digitalOut.setState(PinState.LOW);
		}
	}

	@Override
	public double readAnalogPinValue(PinNumberEnum pinNumber) throws NotInputPinException {
		GpioPinAnalogInput analogIn = getProvisionedPin(pinNumber.getVal(), GpioPinAnalogInput.class);
		if (analogIn == null) {
			throw new NotOutputPinException();
		}
		return analogIn.getValue();
	}

	@Override
	public void writeAnalogPinValue(PinNumberEnum pinNumber, double value) throws NotOutputPinException {
		GpioPinAnalogOutput analogOut = getProvisionedPin(pinNumber.getVal(), GpioPinAnalogOutput.class);
		if (analogOut == null) {
			throw new NotOutputPinException();
		}
		analogOut.setValue(value);
	}

	private void provisionPin(InputOutputEnum pinFunction, PinLevelEnum defaultValue, Pin pin, PinTypeEnum pinType) {
		switch (pinType) {
		case analog:
			switch (pinFunction) { // Get request sets the input pin
			case input:
				GpioPinAnalogInput analogIn = this.gpioContoller.provisionAnalogInputPin(pin);
				switch (defaultValue) {
				case high:
					analogIn.setPullResistance(PinPullResistance.PULL_UP);
					break;
				case low:
					analogIn.setPullResistance(PinPullResistance.PULL_DOWN);
					break;
				}
				break;
			// Post request sets the output pin
			case output:
				GpioPinAnalogOutput analogOut = this.gpioContoller.provisionAnalogOutputPin(pin);

				switch (defaultValue) {
				case high:
					analogOut.setValue(5.0);
					break;
				case low:
					analogOut.setValue(0.0);
					break;
				}
				break;
			}
			break;
		case digital:
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
			break;
		}
	}

	private Pin getPinByName(int pinNumber) {
		return RaspiPin.getPinByName("GPIO " + pinNumber);
	}

	// generic method for getting any type of GpioPin
	private <T extends GpioPin> T getProvisionedPin(int pinNumber, Class<T> clazz) {
		for (GpioPin tmp : gpioContoller.getProvisionedPins()) {
			if (tmp.getPin().getAddress() == pinNumber) {
				// Cast the pin as it is configured, can be digital or analog
				if (clazz.isInstance(tmp)) {
					return clazz.cast(tmp);
				} else {
					return null;
				}
			}
		}
		return null;
	}

	private void unprovision(Pin pin) {
		gpioContoller.getProvisionedPins().stream().filter(tempPin -> pin.equals(tempPin.getPin())).findFirst()
				.ifPresent(tempPin -> gpioContoller.unprovisionPin(tempPin));

	}

	private boolean isProvisioned(Pin pin) {
		return gpioContoller.getProvisionedPins().stream().filter(tempPin -> pin.equals(tempPin.getPin())).findAny()
				.isPresent();
	}
}
