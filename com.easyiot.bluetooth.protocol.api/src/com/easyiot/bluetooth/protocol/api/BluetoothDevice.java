package com.easyiot.bluetooth.protocol.api;

public interface BluetoothDevice {
	/**
	 * Returns the delegate class. This class can be used in the implementation
	 * classes to reuse through other operations provided by the underlying
	 * library.
	 * 
	 * @return
	 */
	<T> T getDelegateDevice();

	/**
	 * Returns the bluetooth address.
	 * 
	 * @return
	 */
	String getBluetoothAddress();

	/**
	 * Returns the friendly name.
	 * 
	 * @return
	 */
	String getFriendlyName();
}
