package com.mat.model;

import java.util.*;

import com.mat.interfaces.IExternalServices;
import com.mat.json.*;
import com.google.api.client.auth.oauth2.Credential;


public class EScontroller implements IExternalServices {
	
		public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) {
			return null;
			
	}

	public List<Contact> getContacts(int userId, List<Scheduler> schedulers) {
		return null;
			
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


