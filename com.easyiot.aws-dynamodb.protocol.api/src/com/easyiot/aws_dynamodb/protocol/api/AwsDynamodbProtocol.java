package com.easyiot.aws_dynamodb.protocol.api;

import org.osgi.annotation.versioning.ProviderType;

import com.easyiot.base.api.Protocol;

@ProviderType
public interface AwsDynamodbProtocol extends Protocol {
	
	/**
	 * Saves a string against a key value. Overrides if the value exists.
	 * 
	 * @param key
	 * @param data
	 */
	public void saveData(String key, String data);
	
	/**
	 * Reads string data stored against a key.
	 * 
	 * @param key
	 * @return null if no data is found.
	 */
	public String readData(String key);
	
	/**
	 * Deletes data from server.
	 * 
	 * @param key
	 * @return
	 */
	public boolean deleteData(String key);

}
