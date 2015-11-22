package com.mat.model;

import com.mat.interfaces.*;
import com.mat.json.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//import com.google.api.client.auth.oauth2.Credential;

public class EScontroller implements IExternalServices {

	/**
	 * Letting Spring to make an instance of ServicesAuthorization class
	 * (settings in external_services-servlet.xml)
	 */
	@Autowired
	ServicesAuthorization serAuth;

	@Override
	public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable {		
		List<ExternalCalendar> calendars=new ArrayList<ExternalCalendar>();
		for (Scheduler sch : schedulers) {
			IService iService = null;
			switch (sch.getShedulerName()) {
			case ServicesConstants.GOOGLE_SERVICE_NAME:
				iService = new GoogleExternalServices();
				break;
			case ServicesConstants.OUTLOOK_SERVICE_NAME:
				iService = new OutlookExternalServices();
				break;
			default:
				break;
			}
			MatCredential credential = serAuth.getCredential(userId, sch);
			calendars.addAll(iService.getCalendars(credential));
		}		
		return calendars;
	}

	@Override
	public List<Person> getContacts(int userId, List<Scheduler> schedulers) throws Throwable {
		List<Person> persons=new ArrayList<Person>();
		for(Scheduler scheduler: schedulers){
			MatCredential credential=serAuth.getCredential(userId, scheduler);
			String className="com.mat.model."+scheduler.getShedulerName()+"ExternalServices";
			List<Person> personsFromOneService = new ArrayList<Person>();
			
			IService iService = (IService) Class.forName(className).newInstance();
			//System.out.println("created successfuly:"+className);
			personsFromOneService=iService.getContacts(credential);
			persons.addAll(personsFromOneService);
		}
		
		return persons;

	}

	@Override
	public void setCredential(int userId, Scheduler scheduler, MatCredential credential) {
		serAuth.setCredential(userId, scheduler, credential);
	}
	
	@Override
	public List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable {
		List<Scheduler> resultSchedulers=new ArrayList<Scheduler>();
		for (Scheduler scheduler : schedulers) {
			MatCredential credential = serAuth.getCredential(userId, scheduler);
			if(credential!=null)
				resultSchedulers.add(scheduler);
		}
		return resultSchedulers;
	}

	// TODO: need to be revised
	@Override
	public boolean upload(int userId, UploadRequest request) throws Throwable {
		List<Scheduler> schedulers = getSchedulers();
		boolean res = true;
		for (Scheduler sch : schedulers) {
			IService iService = null;
			switch (sch.getShedulerName()) {
			case ServicesConstants.GOOGLE_SERVICE_NAME:
				iService = new GoogleExternalServices();
				break;
			case ServicesConstants.OUTLOOK_SERVICE_NAME:
				iService = new OutlookExternalServices();
				break;
			default:
				break;
			}
			MatCredential credential = serAuth.getCredential(userId, sch);
			res = res && iService.upload(credential, request);
		}
		//TODO: bad because if cycle was empty returns true. need to improve
		return res;
	}

	private List<Scheduler> getSchedulers() {
		List<Scheduler> schedulers = new ArrayList<Scheduler>();
		Scheduler scheduler = new Scheduler();
		scheduler.setShedulerName(ServicesConstants.GOOGLE_SERVICE_NAME);
		schedulers.add(scheduler);
		scheduler = new Scheduler();
		scheduler.setShedulerName(ServicesConstants.OUTLOOK_SERVICE_NAME);
		schedulers.add(scheduler);
		return schedulers;
	}

	@Override
	public DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable {
		List<Scheduler> schedulers = getSchedulers();
		List<DownloadEvent> events=new ArrayList<DownloadEvent>();		
		for (Scheduler sch : schedulers) {
			IService iService = null;
			switch (sch.getShedulerName()) {
			case ServicesConstants.GOOGLE_SERVICE_NAME:
				iService = new GoogleExternalServices();
				break;
			case ServicesConstants.OUTLOOK_SERVICE_NAME:
				iService = new OutlookExternalServices();
				break;
			default:
				break;
			}
			MatCredential credential = serAuth.getCredential(userId, sch);
			events.addAll(iService.download(credential, request).getEvents());
		}
		DownloadEventsResponse response=new DownloadEventsResponse();
		response.setEvents(events);
		return response;
	}

	/**
	 * temporary method for testing Hessian SOAP service initial capabilities
	 * 
	 * @return text. If this text can receive Hessian-client, server works well
	 *         even without other methods ready
	 */
	@Override
	public String testMethod() {
		return "If you see this hessian service works";
	}

	

}
