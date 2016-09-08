package com.easyiot.bluetooth.protocol.api;

public enum AuthEncryptEnum {
	NOAUTHENTICATE_NOENCRYPT(0), AUTHENTICATE_NOENCRYPT(1), AUTHENTICATE_ENCRYPT(2);
	int val;

	AuthEncryptEnum(int val) {
		this.val = val;
	}

	int getVal() {
		return this.val;
	}
}
