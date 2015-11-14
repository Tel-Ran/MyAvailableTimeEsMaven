package com.mat.json;

import java.util.List;

public class DownloadEventsResponse {
    List<DownloadEvent> events;

	public List<DownloadEvent> getEvents() {
		return events;
	}

	public void setEvents(List<DownloadEvent> events) {
		this.events = events;
	}

	@Override
	public String toString() {
		return "DownloadEventsResponse [events=\n" + events + "]";
	}
    
    
}
