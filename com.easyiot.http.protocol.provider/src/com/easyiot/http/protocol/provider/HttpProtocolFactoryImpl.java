package com.easyiot.http.protocol.provider;

import java.lang.annotation.Annotation;
import java.util.Random;

import org.osgi.service.component.annotations.Component;

import com.easyiot.http.protocol.api.HttpProtocol;
import com.easyiot.http.protocol.api.HttpProtocolFactory;
import com.easyiot.http.protocol.provider.configuration.HttpProtocolConfiguration;

@Component
public class HttpProtocolFactoryImpl implements HttpProtocolFactory {

	@Override
	public HttpProtocol getInstance(String url) {
		HttpProtocolConfiguration conf = new HttpProtocolConfiguration() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return HttpProtocolConfiguration.class;
			}

			@Override
			public String url() {
				return url;
			}

			@Override
			public String id() {
				return String.valueOf(new Random().nextInt());
			}
		};
		HttpProtocolImpl returnVal = new HttpProtocolImpl();
		returnVal.activate(conf);
		return returnVal;
	}

}
