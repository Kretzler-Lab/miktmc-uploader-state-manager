package org.kpmp.stateManager;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "state")
public class State {

	@Id
	private String id;
	private String packageId;
	private String state;
	private String codicil;
	private Date stateChangeDate;

	public Date getStateChangeDate() {
		return stateChangeDate;
	}

	public void setStateChangeDate(Date stateChangeDate) {
		this.stateChangeDate = stateChangeDate;
	}

	public String getCodicil() {
		return codicil;
	}

	public void setCodicil(String codicil) {
		this.codicil = codicil;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
