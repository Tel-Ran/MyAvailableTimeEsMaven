package com.mat.json;

import java.util.Date;
import java.util.List;

public class DownloadEventsRequest {    
    Date fromDate;
    Date toDate;
    //information about requested calendars
    List<ExternalCalendar> calendars;
	
    public Date getFromDate() {
		return fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public List<ExternalCalendar> getCalendars() {
		return calendars;
	}
	
	public void setFromDate(Date startDate) {
		this.fromDate = startDate;
	}
	public void setToDate(Date endDate) {
		this.toDate = endDate;
	}
	public void setCalendars(List<ExternalCalendar> calendars) {
		this.calendars = calendars;
	}
	@Override
	public String toString() {
		return "DownloadEventsRequest [startDate=" + fromDate + ", endDate="
				+ toDate + ", calendars=" + calendars + "]";
	}
	
	
    
    
}
