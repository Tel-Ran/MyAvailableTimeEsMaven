package com.mat.json;

public class Status {

	String statusName; // status identification (collaborated/shared/available)
	int confirmation; // status progress designation

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public int getConfirmation() {
		return confirmation;
	}

	public void setConfirmation(int confirmation) {
		this.confirmation = confirmation;
	}

	@Override
	public String toString() {
		return "Status [statusName=" + statusName + ", confirmation=" + confirmation + "]";
	}

}