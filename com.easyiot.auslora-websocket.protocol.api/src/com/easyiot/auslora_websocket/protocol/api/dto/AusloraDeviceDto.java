package com.easyiot.auslora_websocket.protocol.api.dto;

import org.osgi.dto.DTO;

public class AusloraDeviceDto extends DTO {
	public String _id;
	public String devclass;
	public String devaddr;
	public String nwkskey;
	public String appskey;
	public String appkey;
	public int seqno;
	public int seqdn;
	public int seqq;
	public boolean seqrelax;
	public boolean seqdnreset;
	public boolean adr;
	public int adrCnt;
	public int subscription;
	public Long txrate;
	public Long rxrate;
}
