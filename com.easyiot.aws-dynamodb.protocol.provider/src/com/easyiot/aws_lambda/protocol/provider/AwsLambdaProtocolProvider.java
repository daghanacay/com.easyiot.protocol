package com.easyiot.aws_lambda.protocol.provider;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

public class AwsLambdaProtocolProvider {
	private void setUpLambda() {
		final CatService catService = LambdaInvokerFactory.builder()
				.lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(CatService.class);
		
		CountCatsInput input = new CountCatsInput();
		input.setBucketName("pictures-of-cats");
		input.setKey("three-cute-cats");

		int cats = catService.countCats(input).getCount();
	}
}
