package com.mat.model;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.mat.interfaces.IExternalServices;
import com.mat.interfaces.ServicesConstants;
import com.mat.json.*;

public class GoogleExternalServices implements IExternalServices {

	Calendar service;

	public GoogleExternalServices(Calendar service) {
		super();
		this.service = service;
	}

	public GoogleExternalServices() {
	}

	/**
	 * ? do we need uploading timeZone to google calendar?
	 */
	@Override
	public boolean upload(UploadRequest request) throws Throwable {
		/*
		 * UploadRequest = String myCalendarName; int duration;// time period in
		 * minutes; List<Date> slots; //list of dates from which the selected
		 * slots begin List<ExternalCalendar> calendars; //calendars to where we
		 * uploading int userId;
		 */
		Boolean isUploadCorrect = true;
		String eventName = request.getMyCalendarName();

		for (ExternalCalendar calendar : request.getCalendars()) {
			if (calendar.getCalendarService().equalsIgnoreCase(
					ServicesConstants.GOOGLE_SERVICE_NAME)) {
				String calendarId = getCalendarId(calendar.getCalendarName());// calendarListEntry.getSummary()
				clearPreviousEvents(calendarId, eventName);
				for (Date startDate : request.getSlots()) {
					// getting endDate:
					Date endDate = new Date(startDate.getTime()
							+ request.getDuration() * ServicesConstants.MINUTE);
					// inserting event:
					Boolean isAdded = addEvent(startDate, endDate, calendarId,
							eventName);
					// getting final result:
					isUploadCorrect = (isUploadCorrect && isAdded);
				}
			}
		} // end "for calendars..."

		return isUploadCorrect;
	}

	/*
	 * DownloadEvent { ExternalCalendar calendar; String eventName; //example:
	 * meeting with team / football Date beginning; Date ending;
	 */
	@Override
	public DownloadEventsResponse download(DownloadEventsRequest request)
			throws Throwable {
		/*
		 * Date startInterval; Date endInterval; List<ExternalCalendar> calendars;
		 */
		DownloadEventsResponse response = new DownloadEventsResponse();
		DateTime startInterval = new DateTime(request.getStartDate());
		DateTime endInterval = new DateTime(request.getEndDate());

		List<DownloadEvent> eventsResult = new ArrayList<DownloadEvent>();		
		for (ExternalCalendar calendar : request.getCalendars()) {
			if (calendar.getCalendarService().equalsIgnoreCase(
					ServicesConstants.GOOGLE_SERVICE_NAME)) {
				String calendarId = getCalendarId(calendar.getCalendarName());
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
						// getRecurringEventId() - ������ �� id �������� �������
						if (event.getRecurrence() != null) {							
							System.out.println("Id recurring event="+ event.getId());
							List<String> rules= event.getRecurrence(); // RRULE, EXRULE, RDATE and EXDATE 
							String pageToken1 = null;
							do {
								// getting recEvents from the given interval							
								Events recEvents = service.events().instances(calendarId, event.getId())
										.setTimeMin(startInterval)
										.setTimeMax(endInterval)
										.setPageToken(pageToken1).execute();
								List<Event> recItems = recEvents.getItems();								
								///								
								int i=0;
								for (Event recEvent : recItems) {
									DownloadEvent recEventResult = new DownloadEvent();
									i++;
									recEventResult.setCalendar(calendar);
									recEventResult.setEventName(event.getSummary());
									// using rules to get dates and time of rec events!!!									
									recEventResult.setBeginning(getDateFromDT(recEvent.getStart()));
									recEventResult.setEnding(getDateFromDT(recEvent.getEnd()));	
									if(!recEvent.getId().equals(event.getId())) {
										eventsResult.add(recEventResult);
										System.out.println("recEventResult="+ recEventResult.getBeginning() + " "+
												recEventResult.getEnding());
										System.out.println("events="+eventsResult.toString());
									}
								}
								pageToken1 = recEvents.getNextPageToken();
							} while (pageToken1 != null);

						}
						else{ //adding event without recurrence
							// calendar
							eventResult.setCalendar(calendar);
							// DateStart						
							eventResult.setBeginning(getDateFromDT(event.getStart()));
							// DateEnd						
							eventResult.setEnding(getDateFromDT(event.getEnd()));
							// name
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

	private Date getDateFromDT(EventDateTime dt) throws ParseException {
		DateFormat formatDateTime = new SimpleDateFormat(ServicesConstants.DATETIME_FORMAT);
		DateFormat formatDate = new SimpleDateFormat(ServicesConstants.DATE_FORMAT);
		return (dt.getDateTime() == null) ? 
				formatDate.parse(dt.getDate().toString()) : 
				formatDateTime.parse(dt.getDateTime().toStringRfc3339());
	}

	@Override
	public boolean authorize(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Contact> getContacts(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * if not authorized we should throw exception in this method (in class that implements IExternalServices)
	 */
	@Override
	public List<ExternalCalendar> getCalendars() throws IOException {
		String pageToken = null;
		List<ExternalCalendar> calendars = new ArrayList<ExternalCalendar>();
		do {
			CalendarList calendarList = service.calendarList().list()
					.setPageToken(pageToken).execute(); // throws exception if needs														
			List<CalendarListEntry> items = calendarList.getItems();

			for (CalendarListEntry calendarListEntry : items) {
				ExternalCalendar newCalendar = new ExternalCalendar();
				newCalendar.setCalendarName(calendarListEntry.getSummary());
				newCalendar
						.setCalendarService(ServicesConstants.GOOGLE_SERVICE_NAME);
				calendars.add(newCalendar);
			}
			pageToken = calendarList.getNextPageToken();
		} while (pageToken != null);
		return calendars;
	}

	private boolean addEvent(Date startDate, Date endDate, String calendarId,
			String eventName) throws IOException {
		Event event = new Event().setSummary(eventName);

		// DateTime startDateTime = new DateTime("2015-11-05T09:00:00-07:00");
		// //startDate
		DateTime startDateTime = new DateTime(startDate);
		EventDateTime start = new EventDateTime().setDateTime(startDateTime);
		// .setTimeZone("America/Los_Angeles");
		event.setStart(start);

		// DateTime endDateTime = new DateTime("2015-11-05T17:00:00-07:00");
		// //endDate
		DateTime endDateTime = new DateTime(endDate);
		EventDateTime end = new EventDateTime().setDateTime(endDateTime);
		// .setTimeZone("America/Los_Angeles");
		event.setEnd(end);

		event = service.events().insert(calendarId, event).execute();

		// System.out.printf("Event created: %s\n", event.getHtmlLink());
		return true;
	}

	// if it is possible to get id by name without getting calendarList?..
	private String getCalendarId(String calendarName) throws IOException {
		String calendarId = null; // "primary" by default? or null?

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

	// get all events with name=eventName in calendar=calendarId
	private void clearPreviousEvents(String calendarId, String eventName)
			throws IOException {

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

	public Credential getCredential(int userId, Scheduler scheduler) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCredential(int userId, Scheduler scheduler, Credential credential) {
		// TODO Auto-generated method stub
		
	}

	public List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Contact> getContacts(int UserId, List<Scheduler> schedulers) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * public boolean isEndTimeUnspecified() Convenience method that returns
	 * only Boolean.TRUE or Boolean.FALSE. Boolean properties can have four
	 * possible values: null, Data.NULL_BOOLEAN, Boolean.TRUE or Boolean.FALSE.
	 * This method returns Boolean.TRUE if the default of the property is
	 * Boolean.TRUE and it is null or Data.NULL_BOOLEAN. Boolean.FALSE is
	 * returned if the default of the property is Boolean.FALSE and it is null
	 * or Data.NULL_BOOLEAN. Whether the end time is actually unspecified. An
	 * end time is still provided for compatibility reasons, even if this
	 * attribute is set to True. The default is False.
	 */

	/*
	 * public java.util.List<java.lang.String> getRecurrence() List of RRULE,
	 * EXRULE, RDATE and EXDATE lines for a recurring event, as specified in
	 * RFC5545. Note that DTSTART and DTEND lines are not allowed in this field;
	 * event start and end times are specified in the start and end fields. This
	 * field is omitted for single events or instances of recurring events.
	 */
}
