package com.easyiot.aws_dynamodb.protocol.provider;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.easyiot.aws_dynamodb.protocol.api.AwsDynamodbProtocol;
import com.easyiot.aws_dynamodb.protocol.configuration.AwsDynamoDbConfiguration;

@Designate(ocd = AwsDynamoDbConfiguration.class, factory = true)
@Component(name = "com.easyiot.aws-dynamodb.protocol", configurationPolicy = ConfigurationPolicy.REQUIRE)
public class AwsDynamoDbProtocolImpl implements AwsDynamodbProtocol {

	private static final String ID = "ID";
	private AmazonDynamoDB ddb;
	private AwsDynamoDbConfiguration awsDynamoDbConfiguration;

	@Activate
	public void activate(AwsDynamoDbConfiguration conf) {
		this.awsDynamoDbConfiguration = conf;
		AmazonDynamoDBClientBuilder dbBuilder = AmazonDynamoDBClientBuilder.standard();
		AWSCredentialsProvider credentialProvider = getAwsCredentials(awsDynamoDbConfiguration);
		ddb = dbBuilder.withRegion(awsDynamoDbConfiguration.region()).withCredentials(credentialProvider).build();
		try {
			ddb.describeTable(awsDynamoDbConfiguration.tableName()).getTable();
		} catch (AmazonServiceException e) {
			CreateTableRequest request = new CreateTableRequest()
					.withAttributeDefinitions(new AttributeDefinition(ID, ScalarAttributeType.S))
					.withKeySchema(new KeySchemaElement(ID, KeyType.HASH))
					.withProvisionedThroughput(new ProvisionedThroughput(new Long(10), new Long(10)))
					.withTableName(awsDynamoDbConfiguration.tableName());

			CreateTableResult result = ddb.createTable(request);
		}
	}

	@Deactivate
	public void deActivate() {
		ddb.shutdown();
	}

	private AWSCredentialsProvider getAwsCredentials(AwsDynamoDbConfiguration conf) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(conf.awsAccessKeyId(),conf.awsSecretKey());
		return new AWSStaticCredentialsProvider(awsCreds);
	}

	@Override
	public String getId() {
		return this.awsDynamoDbConfiguration.id();
	}

	@Override
	public void saveData(String key, String data) {
		HashMap<String, AttributeValue> item_values = new HashMap<String, AttributeValue>();

		item_values.put(key, new AttributeValue(data));

		try {
			ddb.putItem(awsDynamoDbConfiguration.tableName(), item_values);
		} catch (ResourceNotFoundException e) {
			System.err.format("Error: The table \"%s\" can't be found.\n", awsDynamoDbConfiguration.tableName());
			System.err.println("Be sure that it exists and that you've typed its name correctly!");
		} catch (AmazonServiceException e) {
			System.err.println(e.getMessage());
		}

	}

	@Override
	public String readData(String key) {
		Map<String, AttributeValue> retrievedValue = new HashMap<>();
		retrievedValue.put(ID, new AttributeValue(key));
		Map<String, AttributeValue> returned_item = ddb.getItem(awsDynamoDbConfiguration.tableName(), retrievedValue)
				.getItem();
		return returned_item.get(key).getS();
	}

	@Override
	public boolean deleteData(String key) {
		Map<String, AttributeValue> deletedValue = new HashMap<>();
		deletedValue.put(ID, new AttributeValue(key));
		ddb.deleteItem(awsDynamoDbConfiguration.tableName(), deletedValue);
		return true;
	}

}
