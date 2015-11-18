package com.mat.model;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.mat.interfaces.IExternalServices;
import com.mat.interfaces.IService;
import com.mat.interfaces.ServicesConstants;
import com.mat.json.*;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

import com.google.api.client.auth.oauth2.Credential;

public class EScontroller implements IExternalServices {

	/**
	 * Letting Spring to make an instance of ServicesAuthorization class
	 * (settings in external_services-servlet.xml)
	 */
	@Autowired
	ServicesAuthorization serAuth;

	public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) {
		return null;

	}

	public List<Person> getContacts(int userId, List<Scheduler> schedulers) {
		return null;

	}

	public void setCredential(int userId, Scheduler scheduler, Credential credential) {
		serAuth.setCredential(userId, scheduler, credential);

	}

	public List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO: need to be revised
	public boolean upload(int userId, UploadRequest request) throws Throwable {
		List<Scheduler> schedulers = new ArrayList<Scheduler>();
		Scheduler scheduler = new Scheduler();
		scheduler.setShedulerName(ServicesConstants.GOOGLE_SERVICE_NAME);
		schedulers.add(scheduler);
		scheduler = new Scheduler();
		scheduler.setShedulerName(ServicesConstants.OUTLOOK_SERVICE_NAME);
		schedulers.add(scheduler);
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
			Credential credential = serAuth.getCredential(userId, scheduler);
			res = res && iService.upload(credential, request);
		}
		//TODO: bad because if cycle was empty returns true. need to improve
		return res;
	}

	public DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return null;
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
