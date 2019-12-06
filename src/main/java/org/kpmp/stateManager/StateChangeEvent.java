package org.kpmp.stateManager;

class StateChangeEvent {
	private String origin;
	private String packageId;
	private String state;
	private String codicil;

	public StateChangeEvent(String origin, String packageId, String state, String codicil) {
		this.origin = origin;
		this.packageId = packageId;
		this.state = state;
		this.codicil = codicil;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCodicil() {
		return codicil;
	}

	public void setCodicil(String codicil) {
		this.codicil = codicil;
	}
}
