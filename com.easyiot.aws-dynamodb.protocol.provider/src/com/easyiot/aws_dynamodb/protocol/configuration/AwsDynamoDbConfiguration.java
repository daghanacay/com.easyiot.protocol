package com.easyiot.aws_dynamodb.protocol.configuration;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "AWS DynamoDB Configuration")
public @interface AwsDynamoDbConfiguration {
	@AttributeDefinition(name = "Instance ID", description = "AWS DynamoDB instance ID", required = true)
	public String id() default "aws.dynamo.db";
	
	@AttributeDefinition(name = "AWS Region", description = "AWS region for DynamoDB instance", required = true)
	public String region() default "ap-southeast-2";

	@AttributeDefinition(name = "AWS Access Key Id", description = "AWS key Id, see http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html", required = true)
	public String awsAccessKeyId();
	
	@AttributeDefinition(name = "AWS Secret Key", description = "AWS key, see http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/setup-credentials.html", required = true)
	public String awsSecretKey();

	@AttributeDefinition(name = "Table name", description = "DynamoDb table name", required = true)
	public String tableName() default "myTable";
	
}
