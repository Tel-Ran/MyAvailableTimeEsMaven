package com.mat.interfaces;
import com.google.api.client.auth.oauth2.Credential;
import com.mat.json.*;

import java.util.List;

public interface IExternalServices {
    boolean upload(UploadRequest request) throws Throwable;
    DownloadEventsResponse download(DownloadEventsRequest request) throws Throwable;
    boolean authorize(User user);
    List<Contact> getContacts(int userId, List<Scheduler> schedulers) throws Throwable;
    //null if not authorised
    List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable; //was added at 03.11.2015
    List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable;
    void setToken(Credential credential); //
}
