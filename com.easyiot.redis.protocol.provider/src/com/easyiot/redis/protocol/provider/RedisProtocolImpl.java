package com.easyiot.redis.protocol.provider;

import java.util.List;
import java.util.stream.Collectors;

import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import com.easyiot.redis.protocol.api.RedisProtocol;
import com.easyiot.redis.protocol.api.exception.DataParseError;
import com.easyiot.redis.protocol.provider.configuration.RedisConfiguration;

import osgi.enroute.dto.api.DTOs;
import redis.clients.jedis.Jedis;

@Designate(ocd = RedisConfiguration.class, factory = true)
@Component(name = "com.easyiot.redis.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class RedisProtocolImpl implements RedisProtocol {
	private RedisConfiguration config;
	private Jedis jedisClient;

	@Reference
	private DTOs dtoService;

	@Activate
	public void activateProtocol(RedisConfiguration config) {
		this.config = config;
		jedisClient = new Jedis(this.config.host(), this.config.port());
	}

	@Override
	public void saveData(String key, String data) {
		jedisClient.set(key, data);
	}

	@Override
	public String readData(String key) {
		return jedisClient.get(key);
	}

	@Override
	public <T extends DTO> T readData(String key, Class<T> clazz) throws DataParseError {
		String value = jedisClient.get(key);
		return decodeData(clazz, value);
	}

	private <T extends DTO> T decodeData(Class<T> clazz, String value) {
		try {
			return dtoService.decoder(clazz).get(value);
		} catch (Exception e) {
			throw new DataParseError(String.format("Cannot parse data: %s\n to class: %s", value, clazz.getName()));
		}
	}

	@Override
	public void saveData(String key, List<String> data, boolean append) {
		if (!append) {
			jedisClient.del(key);
		}
		for (String temp : data) {
			jedisClient.rpush(key, temp);
		}
	}

	@Override
	public List<String> readListData(String key) {
		// get all the data from first (0) and the last (-1) included
		return jedisClient.lrange(key, 0, -1);
	}

	@Override
	public <T extends DTO> List<T> readListData(String key, Class<T> clazz) throws DataParseError {

		return readListData(key).stream().map(val -> decodeData(clazz, val)).collect(Collectors.toList());
	}

	@Override
	public boolean deleteData(String key) {
		return jedisClient.del(key) == 1;
	}

	@Override
	public <T extends DTO> void saveData(String key, T data) {
		try {
			saveData(key, dtoService.encoder(data).put());
		} catch (Exception e) {
			throw new DataParseError(String.format("Cannot encode data: %s", data.getClass().getName()));
		}

	}

	@Override
	public String getId() {
		return this.config.id();
	}

}
