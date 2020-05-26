package org.kpmp.stateManager;

import org.bson.Document;
import org.springframework.data.annotation.Id;

@org.springframework.data.mongodb.core.mapping.Document(collection = "stateDisplay")
public class StateDisplay {

	@Id
	private String id;
	private String state;
	private Document apps;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Document getApps() {
		return apps;
	}

	public void setApps(Document apps) {
		this.apps = apps;
	}
}
