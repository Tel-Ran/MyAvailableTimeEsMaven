package com.mat.interfaces;

import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.mat.json.*;


public interface IService {
	  boolean upload(Credential credential, UploadRequest request) throws Throwable;
	    DownloadEventsResponse download(Credential credential, DownloadEventsRequest request) throws Throwable;	   
	    List<Contact> getContacts(Credential credential) throws Throwable;	   
	    List<ExternalCalendar> getCalendars(Credential credential) throws Throwable; //was added at 03.11.2015
	    List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable;	   
}
