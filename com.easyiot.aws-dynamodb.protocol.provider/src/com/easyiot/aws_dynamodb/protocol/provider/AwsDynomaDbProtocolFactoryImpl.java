package com.easyiot.aws_dynamodb.protocol.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;

import com.easyiot.aws_dynamodb.protocol.api.AwsDynamodbProtocol;
import com.easyiot.aws_dynamodb.protocol.api.AwsDynamodbProtocolFactory;
import com.easyiot.base.util.EasyIotOsgiUtil;

@Component
public class AwsDynomaDbProtocolFactoryImpl implements AwsDynamodbProtocolFactory{

	@Override
	public AwsDynamodbProtocol getAwsDynamodbProtocolInstance() {
		AwsDynamodbProtocol returnVal = null;
		Map<String, String> properties = new HashMap<>();
		String randomId = String.valueOf(new Random().nextInt());
		// properties required by the component that needs to be
		properties.put("id", randomId);
		// Additional property to find the created service
		properties.put("factoryId", "factoryId-" + randomId);

		try {
			// push the configuration and let the OSGi create the service
			EasyIotOsgiUtil.pushFactoryConfig(properties, "com.easyiot.aws-dynamodb.protocol");
			// find the created service from the additional parameter
			returnVal = EasyIotOsgiUtil.getService("(factoryId=factoryId-" + randomId + ")");
		} catch (IOException | InterruptedException | InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return returnVal;
	}

}

