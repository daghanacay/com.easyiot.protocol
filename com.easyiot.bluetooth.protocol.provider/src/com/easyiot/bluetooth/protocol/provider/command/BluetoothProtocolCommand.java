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
		Debug.COMMAND_FUNCTION + "=search", Debug.COMMAND_FUNCTION + "=write" })
public class BluetoothProtocolCommand {
	@Reference
	BluetoothProtocol btProtocol;

	/**
	 * provides a command for searching bluetooth enabled devices
	 * 
	 * usage
	 * 
	 * bt:search NOAUTHENTICATE_NOENCRYPT bt:search AUTHENTICATE_NOENCRYPT
	 * bt:search AUTHENTICATE_ENCRYPT
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void search(String encryptAuth) {
		btProtocol.searchDevices().stream().peek(device -> {
			System.out.println("Found device:");
			System.out.println(device.getBluetoothAddress() + device.getFriendlyName());
		}).map(device -> btProtocol.searchServices(device, AuthEncryptEnum.valueOf(encryptAuth)))
				.forEach(serviceList -> {
					System.out.println("Found services");
					serviceList.forEach(service -> System.out.println(service.getConnectionUrl()));
				});
	}

	/**
	 * Sends data through SPP protocol. Check the host configuration in
	 * BluetoothConfiguration to find out which device it will be sent to.
	 * 
	 * usage
	 * 
	 * bt:write 2 hello
	 * 
	 * @param sppServiceNumber
	 * @param data
	 */
	public void write(String sppServiceNumber, String data) {
		btProtocol.sendDataThroughSPP(sppServiceNumber, data);
	}

}
