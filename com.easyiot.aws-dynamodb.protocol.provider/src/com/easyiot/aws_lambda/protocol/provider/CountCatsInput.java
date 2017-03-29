package com.easyiot.aws_lambda.protocol.provider;

public class CountCatsInput {
	private String bucketName;
	private String key;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String value) {
		bucketName = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String value) {
		key = value;
	}
}
