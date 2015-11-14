package com.mat.json;

import java.util.Date;

public class DownloadEvent {
    ExternalCalendar calendar;
    String eventName; //example: meeting with team / football
    Date beginning;
    Date ending;
    
	public ExternalCalendar getCalendar() {
		return calendar;
	}
	public void setCalendar(ExternalCalendar calendar) {
		this.calendar = calendar;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public Date getBeginning() {
		return beginning;
	}
	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}
	public Date getEnding() {
		return ending;
	}
	public void setEnding(Date ending) {
		this.ending = ending;
	}
	@Override
	public String toString() {
		return "DownloadEvent [calendar=" + calendar + ", eventName="
				+ eventName + ", beginning=" + beginning + ", ending=" + ending
				+ "]\n";
	}
    
    
}

