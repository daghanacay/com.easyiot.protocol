package com.easyiot.aws_lambda.protocol.api;

public interface AwsLambdaProtocol {

	public <T> T createLamdaProxy(Class<T> clazz);

}
