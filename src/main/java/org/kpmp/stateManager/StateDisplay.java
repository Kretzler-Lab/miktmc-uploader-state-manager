package org.kpmp.stateManager;

import org.bson.Document;
import org.springframework.data.annotation.Id;

@org.springframework.data.mongodb.core.mapping.Document(collection = "stateDisplay")
public class StateDisplay {

	private Document stateDisplayInfo;
	@Id
	private String id;

	public Document getStateDisplayInfo() {
		return stateDisplayInfo;
	}

	public void setStateDisplayInfo(Document stateDisplayInfo) {
		this.stateDisplayInfo = stateDisplayInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
