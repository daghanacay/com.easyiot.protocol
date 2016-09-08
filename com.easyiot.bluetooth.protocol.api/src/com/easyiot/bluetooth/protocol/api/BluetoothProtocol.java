package com.easyiot.bluetooth.protocol.api;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

/**
 * Create Bluetooth protocol
 */
@ProviderType
public interface BluetoothProtocol extends Protocol {

	/**
	 * Sends data to SPP service number on this server.
	 * 
	 * @param sppServiceNumber
	 * @param data
	 */
	void sendDataThroughSPP(String sppServiceNumber, String data);

	/**
	 * Returns all the Bluetooth devices that are accepting to be discovered in
	 * the vicinity.
	 * 
	 * @return
	 */
	List<BluetoothDevice> searchDevices();

	/**
	 * return services that supports required authentication and authorization
	 * 
	 * @param bluetoothDevice
	 * @param serviceAuthEncrypt
	 * @return
	 */
	List<BluetoothService> searchServices(BluetoothDevice bluetoothDevice, AuthEncryptEnum serviceAuthEncrypt);
}
