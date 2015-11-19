package com.mat.json;

public class ExternalCalendar {
    String calendarService; //google/outlook
    String calendarName; //birthdays/tennis
	
    //temporarily for testing
    @Override
	public String toString() {
		return "ExternalCalendar [calendarService=" + calendarService
				+ ", calendarName=" + calendarName + "]";
	}

	public String getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(String calendarService) {
		this.calendarService = calendarService;
	}

	public String getCalendarName() {
		return calendarName;
	}

	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
    
    

    
}