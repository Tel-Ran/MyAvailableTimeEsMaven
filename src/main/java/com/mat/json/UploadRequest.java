package com.mat.json;

import java.util.Date;
import java.util.List;

public class UploadRequest {
    String myCalendarName;
    int duration;// time period in minutes;
    List<Date> slots; //list of dates from which the selected slots begin
    List<ExternalCalendar> calendars; //calendars to where we uploading
    int userId;
    
	public String getMyCalendarName() {
		return myCalendarName;
	}
	public int getDuration() {
		return duration;
	}
	public List<Date> getSlots() {
		return slots;
	}
	public List<ExternalCalendar> getCalendars() {
		return calendars;
	}
	public int getUserId() {
		return userId;
	}
	
	public void setMyCalendarName(String myCalendarName) {
		this.myCalendarName = myCalendarName;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public void setSlots(List<Date> slots) {
		this.slots = slots;
	}
	public void setCalendars(List<ExternalCalendar> calendars) {
		this.calendars = calendars;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

    
}
