package com.easyiot.bluetooth.protocol.provider.command;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.easyiot.bluetooth.protocol.api.AuthEncryptEnum;
import com.easyiot.bluetooth.protocol.api.BluetoothProtocol;

import osgi.enroute.debug.api.Debug;

/**
 * Provides commands for bluetooth protocol
 * 
 * @author daghan
 *
 */
@Component(service = BluetoothProtocolCommand.class, immediate = true, property = { Debug.COMMAND_SCOPE + "=bt",
		Debug.COMMAND_FUNCTION + "=search" })
public class BluetoothProtocolCommand {
	@Reference
	BluetoothProtocol btProtocol;

	/**
	 * provides a command for searching bluetooth enabled devices
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void search() {
		btProtocol.searchDevices().stream().peek(device -> {
			System.out.println("Found device:");
			System.out.println(device.getBluetoothAddress() + device.getFriendlyName());
		}).map(device -> btProtocol.searchServices(device, AuthEncryptEnum.AUTHENTICATE_NOENCRYPT))
				.forEach(serviceList -> {
					System.out.println("Found services");
					serviceList.forEach(service -> System.out.println(service.getConnectionUrl()));
				});
	}

}
