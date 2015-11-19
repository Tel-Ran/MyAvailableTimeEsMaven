package com.mat.model;

import java.util.HashMap;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.interfaces.IExternalServices;
import com.mat.json.Contact;
import com.mat.json.ExternalCalendar;
import com.mat.json.Scheduler;


public class ServicesAuthorization{
	
	/**
	 * map of credentials
	 */
	HashMap<Integer, HashMap<Scheduler, GoogleCredential>> credentials = new HashMap<Integer, HashMap<Scheduler, GoogleCredential>>();
	

	/**
	 * get credential from map by userId and scheduler
	 */
	public GoogleCredential getCredential(int userId, Scheduler scheduler) {

		GoogleCredential credential = credentials.get(userId).get(scheduler);
		return credential;
	}

	/**
	 * store credential to map
	 */
	public void setCredential(int userId, Scheduler scheduler, GoogleCredential credential) {
		
		HashMap<Scheduler, GoogleCredential> tempMap = new HashMap<Scheduler, GoogleCredential>();
		tempMap.put(scheduler, credential);
		credentials.put(userId, tempMap);
		
	}


/**
 * the same but in the map we store not whole Scheduler object but just a string from it	
 */
	
	/**
	 * get credential from map by userId and scheduler
	 */
/*	public Credential getCredential(int userId, Scheduler scheduler) {

		Credential credential = credentials.get(userId).get(scheduler.getSchedulerName());
		return credential;
	}*/

	/**
	 * store credential to map
	 */
	/*public void setCredential(int userId, Scheduler scheduler, Credential credential) {
		HashMap<String, Credential> tempMap = new HashMap<String, Credential>();
		tempMap.put(scheduler.getShedulerName(), credential);
		credentials.put(userId, tempMap);
	}
	*/	
	

	
	

}
