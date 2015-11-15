package com.mat.interfaces;
import com.google.api.client.auth.oauth2.Credential;
import com.mat.json.*;

import java.util.List;

public interface IExternalServices {   
    void setCredential(int userId, Scheduler scheduler, Credential credential);    
    List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable; //null if not authorized? 
    List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable;
    List<Contact> getContacts(int userId, List<Scheduler> schedulers) throws Throwable; 
    boolean upload(int userId, UploadRequest request) throws Throwable;
    DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable;          
}
