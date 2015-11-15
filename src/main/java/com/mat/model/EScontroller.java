package com.mat.model;

import java.util.*;

import com.mat.interfaces.IExternalServices;
import com.mat.json.*;
import com.google.api.client.auth.oauth2.Credential;


public class EScontroller implements IExternalServices {
	
	HashMap<Integer, HashMap<Scheduler, List<ExternalCalendar>>> calendars = new HashMap<Integer, HashMap<Scheduler,List<ExternalCalendar>>>();
	HashMap<Integer, HashMap<Scheduler, List<Contact>>> contacts = new HashMap<Integer, HashMap<Scheduler,List<Contact>>>(); 
	
		public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) {
			List<ExternalCalendar> exCalendars = new ArrayList<ExternalCalendar>();
			for (int i = 0; i < schedulers.size(); i++) {
				List<ExternalCalendar> tempCal = calendars.get(userId).get(schedulers.get(i));
				if (tempCal != null)
					exCalendars.addAll(tempCal);
			}
			return exCalendars;
				
			
	}

	public List<Contact> getContacts(int userId, List<Scheduler> schedulers) {
		List<Contact> tmpContacts = new ArrayList<Contact>();
		for (int i = 0; i < schedulers.size(); i++) {
			List<Contact> tempList = contacts.get(userId).get(contacts.get(i));
			if (tempList != null)
				tmpContacts.addAll(tempList);
		}
		return tmpContacts;
			
		}

	public void setCredential(int userId, Scheduler scheduler, Credential credential) {
		// TODO Auto-generated method stub
		
	}

	public List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean upload(int userId, UploadRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	public DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	}


