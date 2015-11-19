package com.mat.interfaces;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.json.*;

import java.util.List;

public interface IExternalServices {   
    void setCredential(int userId, Scheduler scheduler, GoogleCredential credential);    
    List<Scheduler> getAuthorizedSchedulers(int userId, List<Scheduler> schedulers) throws Throwable; //null if not authorized? 
    List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers) throws Throwable;
    List<Person> getContacts(int userId, List<Scheduler> schedulers) throws Throwable; 
    boolean upload(int userId, UploadRequest request) throws Throwable;
    DownloadEventsResponse download(int userId, DownloadEventsRequest request) throws Throwable;
    /**
     * temporary method for testing Hessian SOAP service initial capabilities
     * @return any string. If this string can receive Hessian-client, server works well 
     * even without other methods ready
     */
    String testMethod();
}
