package com.mat.json;

import java.util.*;

public class Slot {

	Date beginning;
	Status status;
	List<Person> participants;
	Person client;
	String messageBar;

	public Date getBeginning() {
		return beginning;
	}

	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Person> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Person> participants) {
		this.participants = participants;
	}

	public Person getClient() {
		return client;
	}

	public void setClient(Person client) {
		this.client = client;
	}

	public String getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(String messageBar) {
		this.messageBar = messageBar;
	}

	@Override
	public String toString() {
		return "Slot [beginning=" + beginning + ", status=" + status + ", participants=" + participants + ", client="
				+ client + ", messageBar=" + messageBar + "]";
	}

}