package com.mat.model;

import java.util.*;

import com.mat.interfaces.IExternalServices;
import com.mat.json.*;
import com.google.api.client.auth.oauth2.Credential;
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


public abstract class EScontroller implements IExternalServices {
	
	List
	
	static Calendar googleService;
	static ExchangeService outlookService;
	
		public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) {
		return null;
	}

	public List<Contact> getContacts(int UserId, List<Scheduler> schedulers) {
		// TODO Auto-generated method stub
		return null;
	}

}
