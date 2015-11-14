package com.mat.model;

import java.util.*;

import com.mat.interfaces.IExternalServices;
import com.mat.json.*;
//google:
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.mat.interfaces.IExternalServices;
import com.mat.interfaces.ServicesConstants;
import com.mat.json.*;


public class EScontroller implements IExternalServices {
	
	
	static Calendar googleService;
	static ExchangeService outlookService;
	
	
	@Override
	public boolean upload(UploadRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DownloadEventsResponse download(DownloadEventsRequest request)
			throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean authorize(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Contact> getContacts(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ExternalCalendar> getCalendars() throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setToken() {
		// TODO Auto-generated method stub
		
	}

}
