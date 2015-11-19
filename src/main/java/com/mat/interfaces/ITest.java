package com.mat.interfaces;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.json.Scheduler;

public interface ITest {
GoogleCredential getCredential(int userId, Scheduler scheduler);
}
