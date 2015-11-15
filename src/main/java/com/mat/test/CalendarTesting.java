package com.mat.test;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.Calendar;
import com.mat.interfaces.*;
import com.mat.json.*;
import com.mat.model.GoogleExternalServices;

import java.io.*;
import java.text.*;
import java.util.*;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CalendarTesting {
    /** Application name. */
    private static final String APPLICATION_NAME =
        "Google Calendar API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
        System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart. */
    private static final List<String> SCOPES =
        Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
            CalendarTesting.class.getResourceAsStream("resources/client_secret.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
            flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    public static Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws Throwable {           	
    	// Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
            getCalendarService();        
        IExternalServices gService=new GoogleExternalServices(service);
        List<ExternalCalendar> GoogleCalendars= gService.getCalendars();
        for (ExternalCalendar externalCalendar : GoogleCalendars) {
			System.out.println(externalCalendar.toString());
		}
        System.out.println("Adding event");
        //AddEvent(service);
        UploadRequest request= new UploadRequest();
        request.setMyCalendarName("swimming");
        request.setDuration(60);
        List<Date> slots=new LinkedList<Date>();
        Date date1=new Date();
        slots.add(date1);
        Date date2=new Date(date1.getTime() + request.getDuration()*60*1000*4);
        slots.add(date2);
        Date date3=new Date(date1.getTime() + request.getDuration()*60*1000*24);
        slots.add(date3);
        Date date4=new Date(date3.getTime() + request.getDuration()*60*1000*24);
        slots.add(date4);
        request.setSlots(slots);
        ExternalCalendar cal1=new ExternalCalendar();
        cal1.setCalendarService(ServicesConstants.GOOGLE_SERVICE_NAME);
        //cal1.setCalendarName("spring.in.b7@gmail.com");
        cal1.setCalendarName("Tennis");
        List<ExternalCalendar> calendars=new LinkedList<ExternalCalendar>();
        calendars.add(cal1);
        request.setCalendars(calendars);
        Boolean isAdded=gService.upload(request);
        if (isAdded) 
        	System.out.println("added successfull");
        else System.out.println("problem detected");
        
        
        System.out.println("Downloading event");        
        DownloadEventsRequest requestD= new DownloadEventsRequest();
       
		DateFormat df= new SimpleDateFormat("dd/MM/yyyy");
		Date dateStart = df.parse("01/11/2015");
		Date dateEnd = df.parse("30/11/2015");
		requestD.setStartDate(dateStart);
		requestD.setEndDate(dateEnd);
		requestD.setCalendars(calendars);
		DownloadEventsResponse response=gService.download(requestD);
		System.out.println("response="+response); 
		
        // List the next 10 events from the primary calendar.
        //DateTime now = new DateTime(System.currentTimeMillis());
        
        //com.google.api.services.calendar.Calendar.Events calendarEvents=service.events();               
        //com.google.api.services.calendar.Calendar.Events.List events1 = service.events().list("primary","Tennis");
       /* com.google.api.services.calendar.Calendar.CalendarList calendars=service.calendarList();
        System.out.println(calendars);*/
        /*String pageToken = null;
        do {
          CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
          List<CalendarListEntry> items = calendarList.getItems();

          for (CalendarListEntry calendarListEntry : items) {
            System.out.println(calendarListEntry.getSummary());
            System.out.println(calendarListEntry.getId());
          }
          pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);*/
        
        /*List<String> names=getAllCalendars(service);
        for (String name : names) {
			System.out.println(name);
		}*/
        
       /* Events events = service.events().list("imjp6912urksjarvmftc726n1s@group.calendar.google.com")
            .setMaxResults(10)
            .setTimeMin(now)
            .setOrderBy("startTime")
            .setSingleEvents(true)
            .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }*/
    }
    
    /**
     * 
     * @param service - Google calendar service (we should get it each time at the beginning of work with google calendars)
     * @return list of all calendar names
     * @throws IOException
     */    
    public static List<String> getAllCalendars(Calendar service) /*throws IOException*/{
    	String pageToken = null;
   	 	List<String> calendarNames = new LinkedList<String>();
        do {
          CalendarList calendarList;
          try {
				calendarList = service.calendarList().list().setPageToken(pageToken).execute();
			} catch (IOException e) {
				return null; //?? "service is not available"; or throw this exception?
			}
          List<CalendarListEntry> items = calendarList.getItems();           
          for (CalendarListEntry calendarListEntry : items) {           
            calendarNames.add(calendarListEntry.getSummary());
          }
          pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
        return calendarNames;
    }
    
    
    public static void AddEvent(Calendar service/*, UploadRequest request*/){
    	 /* String myCalendarName;
    	    int duration;// time period in minutes;
    	    List<Date> slots; //list of dates from which the selected slots begin
    	    List<ExternalCalendar> calendars; //calendars to where we uploading
    	    int userId;*/
    	    
    	 //Add event
        Event event = new Event()
			    .setSummary("our event");
			   /* .setLocation("800 Howard St., San Francisco, CA 94103")
			    .setDescription("A chance to hear more about Google's developer products.");*/

			DateTime startDateTime = new DateTime("2015-11-05T09:00:00-07:00");
			EventDateTime start = new EventDateTime()
			    .setDateTime(startDateTime);
			    //.setTimeZone("America/Los_Angeles");
			event.setStart(start);

			DateTime endDateTime = new DateTime("2015-11-05T17:00:00-07:00");
			EventDateTime end = new EventDateTime()
			    .setDateTime(endDateTime);
			    //.setTimeZone("America/Los_Angeles");
			event.setEnd(end);

			/*String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
			event.setRecurrence(Arrays.asList(recurrence));

			EventAttendee[] attendees = new EventAttendee[] {
			    new EventAttendee().setEmail("lpage@example.com"),
			    new EventAttendee().setEmail("sbrin@example.com"),
			};
			event.setAttendees(Arrays.asList(attendees));

			EventReminder[] reminderOverrides = new EventReminder[] {
			    new EventReminder().setMethod("email").setMinutes(24 * 60),
			    new EventReminder().setMethod("popup").setMinutes(10),
			};
			Event.Reminders reminders = new Event.Reminders()
			    .setUseDefault(false)
			    .setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);
*/
			String calendarId = "primary";
			try {
				event = service.events().insert(calendarId, event).execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.printf("Event created: %s\n", event.getHtmlLink());

    }
    /*String myCalendarName; //eventName
    int duration; //for getting EndDate of event=start+duration (in minutes)
    List<Date> slots; //slots.size() - count of events to add, startDate of event
    List<ExternalCalendar> calendars; //calendars to where we uploading
    int userId;*/
    
    public static boolean upload(String uploadRequestStr) throws JsonGenerationException, JsonMappingException, IOException{
    	UploadRequest request = convertFromJson(uploadRequestStr);
    	String eventName=request.getMyCalendarName();
    	
    	return false;
    }
    
    private static String getJson(UploadRequest request) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(request);
	}
    
    private static UploadRequest convertFromJson(String json) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, UploadRequest.class);
	}
    
}