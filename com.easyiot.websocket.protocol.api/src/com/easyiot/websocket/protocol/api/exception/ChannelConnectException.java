package com.easyiot.websocket.protocol.api.exception;

import com.easyiot.base.api.exception.FrameworkException;

public class ChannelConnectException extends FrameworkException {
	private static final long serialVersionUID = 1L;

	public ChannelConnectException(String msg) {
		super(msg);
	}

	public ChannelConnectException(Throwable t) {
		super(t.getMessage());
	}
}
