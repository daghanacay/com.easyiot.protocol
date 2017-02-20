package com.easyiot.aws_dynamodb.protocol.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.easyiot.aws_dynamodb.protocol.api.AwsDynamodbProtocol;
import com.easyiot.base.test.util.IntegrationTestBase;

public class AwsDynamoDbProtocolTest extends IntegrationTestBase {
	private static AwsDynamodbProtocol protocol;

	@BeforeClass
	public static void setUpContext() throws InterruptedException {
		try {

			Map<String, String> properties = new HashMap<>();
			// See com.easyiot.redis.protocol HttpProtocolConfiguration
			properties.put("id", "test.id");
			properties.put("region", "ap-southeast-2");
			properties.put("awsAccessKeyId", "AKIAJQ2TZWQDKIHMYANQ");
			properties.put("awsSecretKey", "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");
			properties.put("tableName", "testTable");
			pushFactoryConfig(properties, "com.easyiot.aws-dynamodb.protocol");
		} catch (IOException e) {
			e.printStackTrace();
		}
		protocol = getService(AwsDynamodbProtocol.class);
	}

	@Test
	public void testProtocol() throws Exception {
		Assert.assertNotNull(context);
	}
	
	@Test
	public void testInsertData(){
		protocol.saveData("ID", "firstData");
		protocol.readData("firstData");
	}
}
