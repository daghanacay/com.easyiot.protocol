package com.easyiot.bluetooth.protocol.provider;

import static org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL;
import static org.osgi.service.log.LogService.LOG_DEBUG;
import static org.osgi.service.log.LogService.LOG_ERROR;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.bluetooth.protocol.api.AuthEncryptEnum;
import com.easyiot.bluetooth.protocol.api.BluetoothDevice;
import com.easyiot.bluetooth.protocol.api.BluetoothProtocol;
import com.easyiot.bluetooth.protocol.api.BluetoothService;
import com.easyiot.bluetooth.protocol.api.exception.BluetoothException;
import com.easyiot.bluetooth.protocol.api.exception.SppCommFailed;
import com.easyiot.bluetooth.protocol.provider.configuration.BluetoothConfiguration;
import com.intel.bluetooth.BluetoothConsts;

import osgi.enroute.configurer.api.RequireConfigurerExtender;

/**
 * Implementation of bluetooth protocol. Uses bluecove libraries.
 * 
 * @author daghan
 *
 */
@RequireConfigurerExtender
@Designate(ocd = BluetoothConfiguration.class, factory = true)
@Component(name = "com.easyiot.bluetooth.protocol", immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
public final class BluetoothProtocolImpl implements BluetoothProtocol {
	// Timeout in seconds
	private static final long CONNECTION_TIMEOUT = 50;
	/**
	 * Log Service Reference
	 */
	@Reference(cardinality = OPTIONAL)
	private volatile LogService logService;
	private BluetoothConfiguration bluetoothConfiguration;
	private ReentrantLock connectionLock;
	private CountDownLatch l;

	/**
	 * Activation Callback
	 */
	@Activate
	public void activate(BluetoothConfiguration conf) throws IOException {
		this.connectionLock = new ReentrantLock();
		this.bluetoothConfiguration = conf;
	}

	@Override
	public String getId() {
		return bluetoothConfiguration.id();
	}

	@Override
	public void sendDataThroughSPP(String sppServiceNumber, String data) {
		String serverURL = String.format("btspp://%s:%s", bluetoothConfiguration.host(), sppServiceNumber);
		StreamConnection streamConnection;
		try {
			if (this.connectionLock.tryLock(5, TimeUnit.SECONDS)) {
				streamConnection = (StreamConnection) Connector.open(serverURL);
				// send string
				OutputStream outStream = streamConnection.openOutputStream();
				PrintWriter pWriter = new PrintWriter(new OutputStreamWriter(outStream));
				pWriter.write(data);
				pWriter.write("\n");
				pWriter.write("\n");
				pWriter.flush();
				outStream.close();
				streamConnection.close();
			} else {
				throw new BluetoothException("Cannot get the lock. Please wait until device is available.");
			}
		} catch (IOException e) {
			throw new SppCommFailed(e);
		} catch (InterruptedException e) {
			throw new BluetoothException("Cannot get the lock. Please wait until device is available.");
		} finally {
			if (connectionLock.isHeldByCurrentThread()) {
				connectionLock.unlock();
			}
		}

	}

	@Override
	public List<BluetoothDevice> searchDevices() {

		DeviceDiscoveryListener listener = new DeviceDiscoveryListener();

		try {
			if (this.connectionLock.tryLock(5, TimeUnit.SECONDS)) {
				this.l = new CountDownLatch(1);
				// Start search
				LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
				// Throw error if it does not finish in a given time
				try {
					if (!l.await(CONNECTION_TIMEOUT, TimeUnit.SECONDS)) {
						this.logService.log(LOG_ERROR,
								String.format("Device discovery TIMEOUT in %ss. Terminating.", CONNECTION_TIMEOUT));
						throw new BluetoothException("Device discovery Timeout");
					}
				} catch (final InterruptedException e) {
					this.logService.log(LOG_ERROR, "Device discovery is interrupted.");
					throw new BluetoothException("Device discovery Interrupted");
				}
			} else {
				throw new BluetoothException("Cannot get the lock. Please wait until device is available.");
			}
		} catch (BluetoothStateException e1) {
			throw new BluetoothException(e1);
		} catch (InterruptedException e) {
			throw new BluetoothException("Cannot get the lock. Please wait until device is available.");
		} finally {
			if (connectionLock.isHeldByCurrentThread()) {
				connectionLock.unlock();
			}
		}

		return listener.getDevices();
	}

	@Override
	public List<BluetoothService> searchServices(BluetoothDevice bluetoothDevice, AuthEncryptEnum serviceAuthEncrypt) {
		logService.log(LOG_DEBUG, String.format("search services on %s %s", bluetoothDevice.getBluetoothAddress(),
				bluetoothDevice.getFriendlyName()));
		// start searching for services for each discovered device
		// new UUID(0x0003), // RFCOMM,
		// new UUID(0x1000),//ServiceDiscoveryServerServiceClassID
		// new UUID(0x0001) // SDP protocol
		UUID[] searchUuidSet = new UUID[] { BluetoothConsts.SERIAL_PORT_UUID };
		// Service name
		int[] attrIDs = new int[] { 0x0100 };
		ServiceDiscoveryListener listener = new ServiceDiscoveryListener(serviceAuthEncrypt);

		try {
			if (this.connectionLock.tryLock(5, TimeUnit.SECONDS)) {
				this.l = new CountDownLatch(1);
				LocalDevice.getLocalDevice().getDiscoveryAgent().searchServices(attrIDs, searchUuidSet,
						bluetoothDevice.<RemoteDevice> getDelegateDevice(), listener);
				// Throw error if it does not finish in a given time
				try {
					if (!l.await(CONNECTION_TIMEOUT, TimeUnit.SECONDS)) {
						this.logService.log(LOG_ERROR,
								String.format("Service discovery TIMEOUT in %s. Terminating.", CONNECTION_TIMEOUT));
						throw new BluetoothException("Service discovery Timeout");
					}
				} catch (final InterruptedException e) {
					this.logService.log(LOG_ERROR, "Service discovery attempt is interrupted.");
					throw new BluetoothException("Service discovery interrupted");
				}
			} else {
				throw new BluetoothException("Cannot get the lock. Please wait until device is available.");
			}
		} catch (InterruptedException e) {
			throw new BluetoothException("Cannot get the lock. Please wait until device search is finished.");
		} catch (BluetoothStateException e) {
			throw new BluetoothException(e);
		} finally {
			if (connectionLock.isHeldByCurrentThread()) {
				connectionLock.unlock();
			}
		}

		logService.log(LOG_DEBUG,
				listener.getServices().size() + " services found for device on " + bluetoothDevice.getFriendlyName());

		return listener.getServices();

	}

	/**
	 * Listener for device discovery
	 * 
	 * @author daghan
	 *
	 */
	public class DeviceDiscoveryListener implements DiscoveryListener {
		List<BluetoothDevice> devicesDiscovered = new ArrayList<>();

		@Override
		public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
			logService.log(LOG_DEBUG, "Device " + btDevice.getBluetoothAddress() + " found");
			devicesDiscovered.add(createBluetoothDevice(btDevice));
			try {
				System.out.println("     name " + btDevice.getFriendlyName(false));
			} catch (IOException cantGetDeviceName) {
			}
		}

		@Override
		public void inquiryCompleted(int discType) {
			// Releases the countdown latch
			l.countDown();
		}

		// This is for services should not be used here
		@Override
		public void serviceSearchCompleted(int transID, int respCode) {
			throw new UnsupportedOperationException();
		}

		// This is for services should not be used here
		@Override
		public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
			throw new UnsupportedOperationException();
		}

		public List<BluetoothDevice> getDevices() {
			return devicesDiscovered;
		}

		private BluetoothDevice createBluetoothDevice(RemoteDevice btDevice) {
			return new BluetoothDevice() {

				@Override
				public String getFriendlyName() {
					try {
						return btDevice.getFriendlyName(false);
					} catch (IOException e) {
						return "";
					}
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T> T getDelegateDevice() {
					return (T) btDevice;
				}

				@Override
				public String getBluetoothAddress() {
					return btDevice.getBluetoothAddress();
				}
			};
		}

	}

	public class ServiceDiscoveryListener implements DiscoveryListener {
		private List<BluetoothService> servicesFound = new ArrayList<>();
		private AuthEncryptEnum serviceAuthEncrypt;

		public ServiceDiscoveryListener(AuthEncryptEnum serviceAuthEncrypt) {
			this.serviceAuthEncrypt = serviceAuthEncrypt;
		}

		public List<BluetoothService> getServices() {
			return servicesFound;
		}

		// This is for services should not be used here
		@Override
		public void deviceDiscovered(RemoteDevice arg0, DeviceClass arg1) {
			throw new UnsupportedOperationException();

		}

		// This is for services should not be used here
		@Override
		public void inquiryCompleted(int arg0) {
			throw new UnsupportedOperationException();

		}

		@Override
		public void serviceSearchCompleted(int arg0, int arg1) {
			// Releases the countdown latch
			l.countDown();
		}

		@Override
		public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
			for (int i = 0; i < servRecord.length; i++) {
				String url = servRecord[i].getConnectionURL(serviceAuthEncrypt.getVal(), false);
				if (url == null) {
					continue;
				}
				servicesFound.add(createBluetoothService(url));
				DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
				if (serviceName != null) {
					logService.log(LOG_DEBUG, "service " + serviceName.getValue() + " found " + url);
				} else {
					logService.log(LOG_DEBUG, "service found " + url);
				}
			}

		}

		private BluetoothService createBluetoothService(String url) {
			return new BluetoothService() {

				@Override
				public String getConnectionUrl() {
					return url;
				}
			};
		}

	}

}
