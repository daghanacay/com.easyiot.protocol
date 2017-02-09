package com.easyiot.redis.protocol.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.easyiot.base.test.util.IntegrationTestBase;
import com.easyiot.redis.protocol.api.RedisProtocol;

public class RedisProtocolTest extends IntegrationTestBase {

	@BeforeClass
	public static void setUpContext() {
		try {

			Map<String, String> properties = new HashMap<>();
			// See com.easyiot.redis.protocol HttpProtocolConfiguration
			properties.put("host", "localhost");
			properties.put("port", "6379");
			pushConfig(properties, "com.easyiot.redis.protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSaveStringData() throws Exception {
		RedisProtocol unitUnderTest = getService(RedisProtocol.class);
		unitUnderTest.saveData("key1", "value1");
		String result = unitUnderTest.readData("key1");
		assertEquals("value1", result);
		unitUnderTest.saveData("key1", "override");
		result = unitUnderTest.readData("key1");
		assertEquals("override", result);

		// clean up
		unitUnderTest.deleteData("key1");
	}

	@Test
	public void testSaveDataDto() throws Exception {
		RedisProtocol unitUnderTest = getService(RedisProtocol.class);
		TestDTO mockData = new TestDTO();
		mockData.name = "daghan1";
		mockData.address = "address 1";
		unitUnderTest.saveData("key1", mockData);
		TestDTO result = unitUnderTest.readData("key1", TestDTO.class);

		assertEquals("daghan1", result.name);
		assertEquals("address 1", result.address);
		// clean up
		unitUnderTest.deleteData("key1");
	}

	@Test
	public void testReadDataDTO() throws Exception {
		RedisProtocol unitUnderTest = getService(RedisProtocol.class);
		unitUnderTest.saveData("key1", "{\"name\":\"daghan\",\"address\":\"my address\"}");
		TestDTO result = unitUnderTest.readData("key1", TestDTO.class);

		assertEquals("daghan", result.name);
		assertEquals("my address", result.address);
		// clean up
		unitUnderTest.deleteData("key1");
	}

	@Test
	public void testListStringSave() throws Exception {
		RedisProtocol unitUnderTest = getService(RedisProtocol.class);
		unitUnderTest.saveData("key1", Arrays.asList("val1", "val2", "val3"), false);
		List<String> result = unitUnderTest.readListData("key1");

		assertEquals("val1", result.get(0));
		assertEquals("val2", result.get(1));
		assertEquals("val3", result.get(2));

		unitUnderTest.saveData("key1", Arrays.asList("Aval1", "Aval2", "Aval3"), false);
		result = unitUnderTest.readListData("key1");

		assertEquals("Aval1", result.get(0));
		assertEquals("Aval2", result.get(1));
		assertEquals("Aval3", result.get(2));

		unitUnderTest.saveData("key1", Arrays.asList("AAval1", "AAval2", "AAval3"), true);
		result = unitUnderTest.readListData("key1");

		assertEquals("Aval1", result.get(0));
		assertEquals("Aval2", result.get(1));
		assertEquals("Aval3", result.get(2));
		assertEquals("AAval1", result.get(3));
		assertEquals("AAval2", result.get(4));
		assertEquals("AAval3", result.get(5));
		// clean up
		unitUnderTest.deleteData("key1");
	}

	@Test
	public void testReadListDataDTO() throws Exception {
		RedisProtocol unitUnderTest = getService(RedisProtocol.class);
		unitUnderTest.saveData("key1", Arrays.asList("{\"name\":\"daghan\",\"address\":\"my address\"}",
				"{\"name\":\"daghan2\",\"address\":\"my address2\"}"), false);
		List<TestDTO> result = unitUnderTest.readListData("key1", TestDTO.class);

		assertEquals("daghan", result.get(0).name);
		assertEquals("my address", result.get(0).address);
		assertEquals("daghan2", result.get(1).name);
		assertEquals("my address2", result.get(1).address);
		// clean up
		unitUnderTest.deleteData("key1");
	}

}
