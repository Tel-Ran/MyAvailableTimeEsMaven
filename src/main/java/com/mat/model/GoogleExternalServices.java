package com.mat.model;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.util.ServiceException;
import com.mat.interfaces.IService;
import com.mat.interfaces.ServicesConstants;
import com.mat.json.*;


public class GoogleExternalServices implements IService {

	private static HttpTransport HTTP_TRANSPORT;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);	
	
	static 	 
	 {
	        try {
	            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();	           
	        } catch (Throwable t) {
	            t.printStackTrace();
	            System.exit(1);
	        }
	    }
	
	 	
	private com.google.api.client.auth.oauth2.Credential getGoogleCredential(com.mat.json.MatCredential credential) throws Throwable{
		com.google.api.client.auth.oauth2.Credential googleCredential= 
				new GoogleCredential.Builder()
					.setJsonFactory(JSON_FACTORY)
					.setTransport(HTTP_TRANSPORT)
					.setClientSecrets(ServicesConstants.CLIENT_ID, ServicesConstants.CLIENT_SECRET)
					.build();
		googleCredential.setRefreshToken(credential.getRefreshToken()); // you need String refreshToken for this method
		googleCredential.refreshToken();
		return googleCredential;
	}
	
	private Calendar getCalendarService(com.google.api.client.auth.oauth2.Credential googleCredential){
		return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleCredential)
        					.setApplicationName(ServicesConstants.APPLICATION_NAME)
        					.build();		
	}
	
	public boolean upload(com.mat.json.MatCredential credential, UploadRequest request) throws Throwable {
		
		Calendar service = getCalendarService(getGoogleCredential(credential));
		
		String eventName = request.getMyCalendarName();
		for (ExternalCalendar calendar : request.getCalendars()) {
			if (calendar.getCalendarService().equalsIgnoreCase(ServicesConstants.GOOGLE_SERVICE_NAME)) {
				String calendarId = getCalendarId(service, calendar.getCalendarName());
				clearPreviousEvents(service, calendarId, eventName);
				for (Slot slot : request.getSlots()) {		
					Date startDate= slot.getBeginning();
					Date endDate = new Date(startDate.getTime() + request.getDuration() * ServicesConstants.MINUTE);					
					addEvent(service, startDate, endDate, calendarId, eventName);					
				}
			}
		} 
		return true;	
	}

	public DownloadEventsResponse download(com.mat.json.MatCredential credential, DownloadEventsRequest request) throws Throwable {
		Calendar service = getCalendarService(getGoogleCredential(credential));
		
		DownloadEventsResponse response = new DownloadEventsResponse();
		DateTime startInterval = new DateTime(request.getFromDate());
		DateTime endInterval = new DateTime(request.getToDate());

		List<DownloadEvent> eventsResult = new ArrayList<DownloadEvent>();		
		for (ExternalCalendar calendar : request.getCalendars()) {
			if (calendar.getCalendarService().equalsIgnoreCase(
					ServicesConstants.GOOGLE_SERVICE_NAME)) {
				String calendarId = getCalendarId(service, calendar.getCalendarName());
				// getting events from calendarId and add them to eventsResult
				String pageToken = null;
				List<ExternalCalendar> calendars = new ArrayList<ExternalCalendar>();
				do {
					Events events = service.events().list(calendarId)
							.setTimeMin(startInterval)
							.setTimeMax(endInterval)
							.setPageToken(pageToken)
							.execute();
					List<Event> items = events.getItems();
					for (Event event : items) {
						DownloadEvent eventResult = new DownloadEvent();						
						// adding recurrence events:
						// getRecurringEventId() - ссылка на id родителя события
						if (event.getRecurrence() != null) {
							//List<String> rules= event.getRecurrence(); // RRULE, EXRULE, RDATE and EXDATE 
							List<DownloadEvent> recEvents=getRecEvents(service, request, event,calendarId, startInterval, endInterval,calendar);
							for (DownloadEvent recEvent : recEvents) {
								eventsResult.add(recEvent);
							}
						}
						else{ //adding event without recurrence							
							eventResult.setCalendar(calendar);													
							eventResult.setBeginning(getDateFromDT(event.getStart()));												
							eventResult.setEnding(getDateFromDT(event.getEnd()));							
							eventResult.setEventName(event.getSummary());
							eventsResult.add(eventResult);
						}
					}
					pageToken = events.getNextPageToken();
				} while (pageToken != null);

			}
		}
		response.setEvents(eventsResult);
		return response;
	}


	public List<Person> getContacts(com.mat.json.MatCredential credential) throws Throwable {
		com.google.api.client.auth.oauth2.Credential googleCredential = getGoogleCredential(credential);
		ContactsService myService = new ContactsService("contacts");//на что влияет имя??
		myService.setOAuth2Credentials(googleCredential);
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

	public List<ExternalCalendar> getCalendars(com.mat.json.MatCredential credential) throws Throwable {
		Calendar service = getCalendarService(getGoogleCredential(credential));
		
		String pageToken = null;
		List<ExternalCalendar> calendars = new ArrayList<ExternalCalendar>();
		do {
			CalendarList calendarList = service.calendarList().list()
					.setPageToken(pageToken).execute(); // throws exception if needs														
			List<CalendarListEntry> items = calendarList.getItems();
			for (CalendarListEntry calendarListEntry : items) {
				ExternalCalendar newCalendar = new ExternalCalendar();
				newCalendar.setCalendarName(calendarListEntry.getSummary());
				newCalendar.setCalendarService(ServicesConstants.GOOGLE_SERVICE_NAME);
				calendars.add(newCalendar);
			}
			pageToken = calendarList.getNextPageToken();
		} while (pageToken != null);
		return calendars;
	}
	
	// if it is possible to get id by name without getting calendarList?..
	private String getCalendarId(Calendar service, String calendarName) throws IOException {
		String calendarId = null;
		String pageToken = null;
		do {
			CalendarList calendarList = service.calendarList().list()
					.setPageToken(pageToken).execute();
			List<CalendarListEntry> items = calendarList.getItems();
			for (CalendarListEntry calendarListEntry : items) {
				if (calendarListEntry.getSummary().equals(calendarName))
					calendarId = calendarListEntry.getId();
			}
			pageToken = calendarList.getNextPageToken();
		} while (pageToken != null);
		return calendarId;
	}	
		
	// getting all events with (name=eventName in calendar=calendarId) and removing them
	private void clearPreviousEvents(Calendar service, String calendarId, String eventName) throws IOException {
		// Iterate over the events in the specified calendar
		List<String> eventIds = new ArrayList<String>();
		String pageToken = null;
		do {
			Events events = service.events().list(calendarId)
					.setPageToken(pageToken).execute();
			List<Event> items = events.getItems();
			for (Event event : items) {
				if (event.getSummary().equals(eventName))
					eventIds.add(event.getId());
			}
			pageToken = events.getNextPageToken();
		} while (pageToken != null);

		for (String eventId : eventIds) {
			service.events().delete(calendarId, eventId).execute();
		}
	}	
	
	//adding event to calendar=calendarId
	private void addEvent(Calendar service, Date startDate, Date endDate, String calendarId,
			String eventName) throws IOException {
		Event event = new Event().setSummary(eventName);
		DateTime startDateTime = new DateTime(startDate);
		EventDateTime start = new EventDateTime().setDateTime(startDateTime);
		// .setTimeZone("America/Los_Angeles");
		event.setStart(start);
		DateTime endDateTime = new DateTime(endDate);
		EventDateTime end = new EventDateTime().setDateTime(endDateTime);
		// .setTimeZone("America/Los_Angeles");
		event.setEnd(end);
		event = service.events().insert(calendarId, event).execute();		
	}

	//converting Date from EventDateTime (both variants) to Date format
	private Date getDateFromDT(EventDateTime dt) throws ParseException {
		DateFormat formatDateTime = new SimpleDateFormat(ServicesConstants.DATETIME_FORMAT);
		DateFormat formatDate = new SimpleDateFormat(ServicesConstants.DATE_FORMAT);
		return (dt.getDateTime() == null) ? 
				formatDate.parse(dt.getDate().toString()) : 
				formatDateTime.parse(dt.getDateTime().toStringRfc3339());
	}

	private List<DownloadEvent> getRecEvents(Calendar service, DownloadEventsRequest request, Event event, String calendarId,
		DateTime startInterval,DateTime endInterval,ExternalCalendar calendar) throws IOException, ParseException {
		List<DownloadEvent> recEventsResult =new ArrayList<DownloadEvent>();
		String pageToken1 = null;
		do {
			// getting recEvents from the given interval							
			Events recEvents = service.events().instances(calendarId, event.getId())
					.setTimeMin(startInterval)
					.setTimeMax(endInterval)
					.setPageToken(pageToken1).execute();
			List<Event> recItems = recEvents.getItems();
			for (Event recEvent : recItems) {
				DownloadEvent recEventResult = new DownloadEvent();									
				recEventResult.setCalendar(calendar);
				recEventResult.setEventName(event.getSummary());													
				recEventResult.setBeginning(getDateFromDT(recEvent.getStart()));
				recEventResult.setEnding(getDateFromDT(recEvent.getEnd()));	
				if(!recEvent.getId().equals(event.getId())) {
					recEventsResult.add(recEventResult);										
				}
			}
			pageToken1 = recEvents.getNextPageToken();
		} while (pageToken1 != null);
		return recEventsResult;
	}

	

}
