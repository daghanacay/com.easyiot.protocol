package com.easyiot.aws_lambda.protocol.provider;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

public interface CatService {
	@LambdaFunction(functionName = "CountCats")
	CountCatsOutput countCats(CountCatsInput input);
}
