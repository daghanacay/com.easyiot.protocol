package com.easyiot.redis.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

public class DataParseError extends FrameworkException {
	private static final long serialVersionUID = 1L;

	public DataParseError(String reason) {
		super(reason);
	}
}
