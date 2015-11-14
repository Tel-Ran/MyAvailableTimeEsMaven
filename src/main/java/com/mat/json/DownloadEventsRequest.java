package com.mat.json;

import java.util.Date;
import java.util.List;

public class DownloadEventsRequest {    
    Date startDate;
    Date endDate;
    //information about requested calendars
    List<ExternalCalendar> calendars;
	
    public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public List<ExternalCalendar> getCalendars() {
		return calendars;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setCalendars(List<ExternalCalendar> calendars) {
		this.calendars = calendars;
	}
	@Override
	public String toString() {
		return "DownloadEventsRequest [startDate=" + startDate + ", endDate="
				+ endDate + ", calendars=" + calendars + "]";
	}
	
	
    
    
}
