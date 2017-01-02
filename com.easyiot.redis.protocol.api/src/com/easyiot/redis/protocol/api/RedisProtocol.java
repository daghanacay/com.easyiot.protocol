package com.easyiot.redis.protocol.api;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.dto.DTO;

import com.easyiot.redis.protocol.api.exception.DataParseError;

@ProviderType
public interface RedisProtocol extends Protocol {
	/**
	 * Saves a string against a key value. Overrides if the value exists.
	 * 
	 * @param key
	 * @param data
	 */
	public void saveData(String key, String data);

	/**
	 * Saves a list of string against a key value.
	 * 
	 * @param key
	 * @param data
	 * @param append
	 *            if true the data is appended on the existing values otherwise
	 *            existing values are replaced with the input.
	 */
	public void saveData(String key, List<String> data, boolean append);

	/**
	 * Reads string data stored against a key.
	 * 
	 * @param key
	 * @return null if no data is found.
	 */
	public String readData(String key);

	/**
	 * Reads a list of string data stored against a key.
	 * 
	 * @param key
	 * @return null if no data is found.
	 */
	public List<String> readListData(String key);

	/**
	 * Saves a DTO object against a key value. Overrides if the value exists.
	 * 
	 * @param key
	 * @param data
	 */
	public <T extends DTO> void saveData(String key, T data);

	/**
	 * Reads data in redis into an object of type T <T extends DTO>
	 * 
	 * @param key
	 * @param clazz
	 *            must be a DTO class
	 * @return null if data is not found
	 * @throws DataParseError
	 */
	public <T extends DTO> T readData(String key, Class<T> clazz) throws DataParseError;

	/**
	 * Reads data in redis into a list of objects of type T <T extends DTO>
	 * 
	 * @param key
	 * @param clazz
	 *            must be a DTO class
	 * @return Empty if data is not found
	 * @throws DataParseError
	 */
	public <T extends DTO> List<T> readListData(String key, Class<T> clazz) throws DataParseError;

	/**
	 * Deletes data from server.
	 * 
	 * @param key
	 * @return
	 */
	public boolean deleteData(String key);

}
