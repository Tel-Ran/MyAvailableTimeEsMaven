package com.mat.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mat.interfaces.ServicesConstants;
import com.mat.json.DownloadEvent;
import com.mat.json.DownloadEventsRequest;
import com.mat.json.DownloadEventsResponse;
import com.mat.json.ExternalCalendar;
import com.mat.json.UploadRequest;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.FolderTraversal;
import microsoft.exchange.webservices.data.core.enumeration.search.OffsetBasePoint;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.FolderSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

public class OutlookExternalServices {
	ExchangeService service;
	List<Folder> folders;

	public List<Folder> getFolders() {
		return folders;
	}

	public OutlookExternalServices(ExchangeService service) {
		this.service = service;
		try {
			folders = getFolderList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean upload(UploadRequest request) throws Throwable {
		String eventName = request.getMyCalendarName();
		for (ExternalCalendar calendar : request.getCalendars()) {
			if (calendar.getCalendarService().equalsIgnoreCase(ServicesConstants.OUTLOOK_SERVICE_NAME)) {
				Folder folder = getFolderByName(calendar.getCalendarName());
				clearPreviousEvents(folder.getId(), eventName);
				for (Date startDate : request.getSlots()) {
					Date endDate = dateAdd(startDate, request.getDuration());
					createAppointment(folder, eventName, "", startDate, endDate);
				}
			}
		}
		return true;
	}

	public DownloadEventsResponse download(DownloadEventsRequest request) throws Throwable {
		DownloadEventsResponse result = new DownloadEventsResponse();
		List<ExternalCalendar> calendars = request.getCalendars();
		List<DownloadEvent> events = new ArrayList<DownloadEvent>();
		for (ExternalCalendar calendar : calendars) {
			events.addAll(findAppointments(getFolder(calendar), request.getFromDate(), request.getToDate()));
		}
		result.setEvents(events);
		return result;
	}

	private List<DownloadEvent> findAppointments(Folder folder, Date startDate, Date endDate) throws Exception {
		List<DownloadEvent> result = new ArrayList<DownloadEvent>();
		FolderId folderid = folder.getId();
		CalendarFolder cf = CalendarFolder.bind(service, folderid);
		FindItemsResults<Appointment> findResults = cf.findAppointments(new CalendarView(startDate, endDate));
		for (Appointment appt : findResults.getItems()) {
			DownloadEvent event = getEventFromAppt(folder, appt);
			result.add(event);
		}
		return result;
	}

	private DownloadEvent getEventFromAppt(Folder folder, Appointment appt) throws ServiceLocalException {
		DownloadEvent event = new DownloadEvent();
		event.setBeginning(appt.getStart());
		event.setEnding(appt.getEnd());
		event.setCalendar(getExteranlCalendarFromAppt(folder));
		event.setEventName(appt.getSubject());
		return event;
	}

	private ExternalCalendar getExteranlCalendarFromAppt(Folder folder) throws ServiceLocalException {
		ExternalCalendar calendar = new ExternalCalendar();
		calendar.setCalendarService(ServicesConstants.OUTLOOK_SERVICE_NAME);
		calendar.setCalendarName(folder.getDisplayName());
		return calendar;
	}

	private Folder getFolder(ExternalCalendar calendar) {
		Folder folder = null;
		try {
			folder = getFolderByName(calendar.getCalendarName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return folder;
	}

	private void clearPreviousEvents(FolderId folderid, String eventName) throws Throwable {
		ItemView view = new ItemView(1000);
		view.setPropertySet(new PropertySet(BasePropertySet.IdOnly));
		SearchFilter sfSearchFilter = new SearchFilter.ContainsSubstring(ItemSchema.Subject, eventName);
		FindItemsResults<Item> findResults = service.findItems(folderid, sfSearchFilter, view);
		if (findResults.getTotalCount() != 0) {
			List<Item> appointments = findResults.getItems();
			for (Item appointment : appointments) {
				appointment.delete(DeleteMode.MoveToDeletedItems);
			}
		}
	}

	private Folder getFolderByName(String calendarName) throws Exception {
		for (Folder folder : folders) {
			if (folder.getDisplayName().equals(calendarName)) {
				return folder;
			}
		}
		return null;
	}

	private void createAppointment(Folder folder, String subject, String textBody, Date startDate, Date endDate)
			throws Exception {
		Appointment appointment = new Appointment(service);
		appointment.setSubject(subject);
		appointment.setBody(MessageBody.getMessageBodyFromText(textBody));
		appointment.setStart(startDate);
		appointment.setEnd(endDate);
		appointment.save(folder.getId());
	}

	private List<Folder> getFolderList() throws Exception {
		List<Folder> result = new ArrayList<Folder>();
		int mPageSize = 100;
		FolderView view = viewInit(0, mPageSize);
		FindFoldersResults findFolderResults = service.findFolders(WellKnownFolderName.MsgFolderRoot, view);
		for (Folder myFolder : findFolderResults.getFolders()) {
			myFolder.load();
			if (myFolder.getFolderClass().equals("IPF.Appointment")) {
				FindFoldersResults calendarFoldersResults = myFolder.findFolders(view);
				result.add(myFolder);
				for (Folder calFolder : calendarFoldersResults.getFolders()) { //
					result.add(calFolder);
				}
			}
		}
		return result;
	}

	private FolderView viewInit(int moffset, int mPageSize) throws Exception {
		FolderView view = new FolderView(mPageSize, moffset, OffsetBasePoint.Beginning);
		PropertySet propertySet = new PropertySet(BasePropertySet.IdOnly);
		propertySet.add(FolderSchema.DisplayName);
		propertySet.add(FolderSchema.ChildFolderCount);
		view.setPropertySet(propertySet);
		view.setTraversal(FolderTraversal.Shallow);
		return view;
	}

	public Date dateAdd(Date startDateAppointment, int duration) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDateAppointment);
		cal.add(Calendar.MINUTE, duration);
		return cal.getTime();
	}
}

/*
 * private List<Folder> getFolderList(int moffset, int moffsetCal) throws
 * Exception { List<Folder> result = new ArrayList<Folder>(); int mPageSize =
 * 30; FolderView viewCal = viewInit(moffsetCal, mPageSize); FolderView view =
 * viewInit(moffset, mPageSize); FindFoldersResults findFolderResults =
 * service.findFolders(WellKnownFolderName.CalendarMsgFolderRoot, view); for
 * (Folder myFolder : findFolderResults.getFolders()) { // myFolder.load(); //
 * if (myFolder.getFolderClass().equals("IPF.Appointment")) { FindFoldersResults
 * calendarFoldersResults = myFolder.findFolders(viewCal); result.add(myFolder);
 * for (Folder calFolder : calendarFoldersResults.getFolders()) { //
 * calFolder.load(); result.add(calFolder); } if
 * (calendarFoldersResults.isMoreAvailable()) { moffsetCal += mPageSize;
 * result.addAll(getFolderList(moffset, moffsetCal)); } } // } if
 * (findFolderResults.isMoreAvailable()) { moffset += mPageSize;
 * result.addAll(getFolderList(moffset, moffsetCal)); } return result; }
 */

/*
 * private List<Folder> getFolderList(int moffset, int moffsetCal) throws
 * Exception { List<Folder> result = new ArrayList<Folder>(); int mPageSize =
 * 100; FolderView viewCal = viewInit(moffsetCal, mPageSize); FolderView view =
 * viewInit(moffset, mPageSize); // FindFoldersResults findFolderResults =
 * service.findFolders(, view); // for (Folder myFolder :
 * findFolderResults.getFolders()) { // FindFoldersResults
 * calendarFoldersResults = // myFolder.findFolders(viewCal); Folder calendar =
 * new Folder(service); calendar.bind(service, WellKnownFolderName.Calendar);
 * result.add(calendar); FindFoldersResults calendarFoldersResults =
 * service.findFolders(WellKnownFolderName.Calendar, viewCal); for (Folder
 * calFolder : calendarFoldersResults.getFolders()) { calFolder.load();
 * result.add(calFolder); } return result; }
 */