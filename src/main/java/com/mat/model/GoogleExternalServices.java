package com.mat.model;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.util.ServiceException;
import com.mat.interfaces.IService;
import com.mat.json.DownloadEventsRequest;
import com.mat.json.DownloadEventsResponse;
import com.mat.json.ExternalCalendar;
import com.mat.json.Person;
import com.mat.json.Scheduler;
import com.mat.json.UploadRequest;

public class GoogleExternalServices implements IService {

	public boolean upload(Credential credential, UploadRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return false;
	}

	public DownloadEventsResponse download(Credential credential, DownloadEventsRequest request) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Person> getContacts(Credential credential) throws Throwable {
		ContactsService myService = new ContactsService("contacts");//на что влияет имя??
		myService.setOAuth2Credentials(credential);
	    URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/default/full");
	    Query myQuery = new Query(feedUrl);
	    myQuery.setMaxResults(3000);
	    List<Person> persons=new ArrayList<Person>();
	    try {
			ContactFeed resultFeed = myService.query(myQuery, ContactFeed.class);
			
		    for(ContactEntry entry : resultFeed.getEntries())
		    {
		    	String currentEmail="";
		      //if(!entry.hasName() || !entry.getName().hasFullName()) continue;
		    	if (!entry.hasEmailAddresses()) continue; //if contact don`t have email - not taking it
		    	for(Email email : entry.getEmailAddresses()){
		    		if(email.getPrimary())
			    		  currentEmail=email.getAddress();
   		  
			    }
		    	//Contact contact=new Contact(entry.getName().getFullName().getValue(), currentEmail);
		    	Person person =new Person();
		    	person.setEmail(currentEmail);
		    	person.setFirstName(entry.getName().getGivenName().getValue());
		    	person.setLastName(entry.getName().getFamilyName().getValue());
		    	//System.out.println(person);
		    	persons.add(person);
		    }
			
		
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return persons;

	}

	public List<ExternalCalendar> getCalendars(Credential credential) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
