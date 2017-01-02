package com.easyiot.redis.protocol.provider;

import java.util.List;

import org.osgi.dto.DTO;

/**
 * This is testing DTOs object conversion for non-DTO objects
 * 
 * @author daghan
 *
 */
public class TestDataDtoLike {
	public String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
