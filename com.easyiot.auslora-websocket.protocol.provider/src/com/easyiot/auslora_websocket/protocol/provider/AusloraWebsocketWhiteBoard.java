package com.easyiot.auslora_websocket.protocol.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketListener;
import com.easyiot.auslora_websocket.protocol.api.AusloraWebsocketProtocol;

@Component(immediate = true)
public class AusloraWebsocketWhiteBoard {
	private Map<String, AusloraWebsocketProtocol> _ausloraWebsocketProtocols = new ConcurrentHashMap<>();

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "removeAusloraProtocol")
	void _addAusloraProtocol(AusloraWebsocketProtocol protocol, Map<String, Object> props) {
		String id = (String) props.get("id");
		if (id != null && !id.isEmpty()) {
			_ausloraWebsocketProtocols.put(id, protocol);
		}
	}

	void removeAusloraProtocol(AusloraWebsocketProtocol protocol, Map<String, Object> props) {
		protocol.disconnectAll();
		String id = (String) props.get("id");
		if (id != null && !id.isEmpty()) {
			_ausloraWebsocketProtocols.remove(id);
		}
	}

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC, unbind = "removeAusloraListener")
	void addAusloraListener(AusloraWebsocketListener l, Map<String, Object> props) {
		String ausloraServerId = (String) props.get("ausloraServerId");
		String applicationId = (String) props.get("applicationId");
		String deviceEUI = (String) props.get("deviceEUI");
		if (applicationId != null && !applicationId.isEmpty()) {
			AusloraWebsocketProtocol registeredProtocol = _ausloraWebsocketProtocols.get(ausloraServerId);
			if (registeredProtocol != null) {
				registeredProtocol.connect(applicationId, deviceEUI, l);
				l.setProtocolHandler(registeredProtocol);
			}

		}
	}

	void removeAusloraListener(AusloraWebsocketListener l, Map<String, Object> props) {
		String ausloraServerId = (String) props.get("ausloraServerId");
		String applicationId = (String) props.get("applicationId");
		String deviceEUI = (String) props.get("deviceEUI");
		if (ausloraServerId != null && !ausloraServerId.isEmpty()) {
			AusloraWebsocketProtocol registeredProtocol = _ausloraWebsocketProtocols.get(ausloraServerId);
			if (registeredProtocol != null) {
				registeredProtocol.disconnect(applicationId, deviceEUI, l);
			}
		}
	}
}