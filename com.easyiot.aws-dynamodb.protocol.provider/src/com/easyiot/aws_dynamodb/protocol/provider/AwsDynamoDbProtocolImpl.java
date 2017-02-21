package com.easyiot.aws_dynamodb.protocol.provider;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.management.RuntimeErrorException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
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
	Promise<AmazonDynamoDB> ddbPromise;

	@Reference
	Executor executor;

	private static final String ID = "ID";
	private AwsDynamoDbConfiguration awsDynamoDbConfiguration;

	@Activate
	public void activate(AwsDynamoDbConfiguration conf) {
		this.awsDynamoDbConfiguration = conf;
		Deferred<AmazonDynamoDB> deferred = new Deferred<>();
		executor.execute(dbCreateLambda(deferred));
		ddbPromise = deferred.getPromise();
	}

	private Runnable dbCreateLambda(Deferred<AmazonDynamoDB> deferred) {

		return () -> {
			AmazonDynamoDBClientBuilder dbBuilder = AmazonDynamoDBClientBuilder.standard();
			AWSCredentialsProvider credentialProvider = getAwsCredentials(awsDynamoDbConfiguration);
			AmazonDynamoDB ddb = dbBuilder.withRegion(awsDynamoDbConfiguration.region())
					.withCredentials(credentialProvider).build();
			try {
				ddb.describeTable(awsDynamoDbConfiguration.tableName()).getTable();
			} catch (AmazonServiceException e) {
				try {
					CreateTableRequest request = new CreateTableRequest()
							.withAttributeDefinitions(new AttributeDefinition(ID, ScalarAttributeType.S))
							.withKeySchema(new KeySchemaElement(ID, KeyType.HASH))
							.withProvisionedThroughput(new ProvisionedThroughput(new Long(10), new Long(10)))
							.withTableName(awsDynamoDbConfiguration.tableName());
					ddb.createTable(request);
				} catch (Exception e2) {
					deferred.fail(e2);
				}
			}
			;
			deferred.resolve(ddb);
		};
	}

	@Deactivate
	public void stop() {
		ddbPromise.onResolve(this::close);
	}

	private void close() {
		try {
			ddbPromise.getValue().shutdown();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private AWSCredentialsProvider getAwsCredentials(AwsDynamoDbConfiguration conf) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(conf.awsAccessKeyId(), conf.awsSecretKey());
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
			ddbPromise.getValue().putItem(awsDynamoDbConfiguration.tableName(), item_values);
		} catch (ResourceNotFoundException e) {
			System.err.format("Error: The table \"%s\" can't be found.\n", awsDynamoDbConfiguration.tableName());
			System.err.println("Be sure that it exists and that you've typed its name correctly!");
		} catch (AmazonServiceException e) {
			System.err.println(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String readData(String key) {
		Map<String, AttributeValue> retrievedValue = new HashMap<>();
		retrievedValue.put(ID, new AttributeValue(key));
		Map<String, AttributeValue> returned_item;
		try {
			returned_item = ddbPromise.getValue().getItem(awsDynamoDbConfiguration.tableName(), retrievedValue)
					.getItem();
			return returned_item.get(ID).getS();
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeException(new Error(e));
		}

	}

	@Override
	public boolean deleteData(String key) {
		Map<String, AttributeValue> deletedValue = new HashMap<>();
		deletedValue.put(ID, new AttributeValue(key));
		try {
			ddbPromise.getValue().deleteItem(awsDynamoDbConfiguration.tableName(), deletedValue);
			return true;
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeErrorException(new Error(e));
		}

	}

}
