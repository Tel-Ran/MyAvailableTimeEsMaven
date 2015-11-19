package com.mat.model;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.interfaces.ITest;
import com.mat.json.MatCredential;
import com.mat.json.Scheduler;

public class TestController implements ITest {

	@Autowired
	ServicesAuthorization serAuth;
	
	@Override
	public MatCredential getCredential(int userId, Scheduler scheduler) {
		return serAuth.
				getCredential(userId, scheduler);
		
		
	}

}
