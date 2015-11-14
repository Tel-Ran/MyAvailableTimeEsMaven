package com.mat.interfaces;
import com.google.api.client.auth.oauth2.Credential;
import com.mat.json.*;

import java.util.List;

public interface IExternalServices {
    Credential getCredential(int userId, Scheduler scheduler);
    void setCredential(int userId, Scheduler scheduler, Credential credential);
    List<ExternalCalendar> getCalendars(int userId, List<Scheduler> schedulers);
    List<Contact> getContacts(int UserId, List<Scheduler> schedulers);
     
}
