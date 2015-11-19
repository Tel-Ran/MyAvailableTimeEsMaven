package com.mat.interfaces;

import java.util.List;

//import com.google.api.client.auth.oauth2.Credential;
import com.mat.json.*;

public interface IService {
	  boolean upload(MatCredential credential, UploadRequest request) throws Throwable;
	    DownloadEventsResponse download(MatCredential credential, DownloadEventsRequest request) throws Throwable;	   
	    List<Person> getContacts(MatCredential credential) throws Throwable;	   
	    List<ExternalCalendar> getCalendars(MatCredential credential) throws Throwable; 	     
}
