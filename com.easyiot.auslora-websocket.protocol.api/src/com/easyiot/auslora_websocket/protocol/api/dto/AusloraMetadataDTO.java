package com.easyiot.auslora_websocket.protocol.api.dto;

import java.util.Arrays;
import java.util.List;

/**
 * Meta data information sent by the Auslora server. specs can be found in
 * Auslora documentation page. We are only interested in "gw" type messages.
 * 
 * @author daghan
 *
 */
public class AusloraMetadataDTO {
	public String cmd = "gw";
	public String EUI = "000DB531176F3557";
	public long ts = 1474695794926L;
	public long fcnt = 44;
	public int port = 2;
	public int freq = 916400000;
	public String dr = "SF10 BW125 4/5";
	public boolean ack = false;
	public String data = "2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e2e";
	public List<AusloraGatewayDTO> gwts = Arrays.asList(new AusloraGatewayDTO());
}
