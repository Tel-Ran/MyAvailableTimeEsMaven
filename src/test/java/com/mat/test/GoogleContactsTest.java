package com.mat.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
//Spring test library
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.interfaces.Constants;
import com.mat.json.MatCredential;
import com.mat.json.Scheduler;
import com.mat.model.EScontroller;
import com.mat.model.ServicesAuthorization;

//spring-Test annotations for adding Spring context to text
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"file:target/ExternalServices/WEB-INF/external_services-servlet.xml"})
public class GoogleContactsTest {
	
	private static final String ACCOUNT_NAME = "google";

	private static final int USER_ID = 1;

	private static final String SCHEDULER_NAME = null;

	@SuppressWarnings("deprecation")
	private static final Date EXP_TIME = new Date();
	private static GoogleCredential gCredential;
	private static EScontroller eScontroller;
	private static MatCredential credential;
	private static Scheduler scheduler;

	//Adding bean of class from xml Spring configuration
	@Autowired
	private ServicesAuthorization serAuth;
	
	/**
	 * Preparing object for tests
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Making MatCredential class instance
		credential = new MatCredential();
		credential.setAccessToken(Constants.ACCESS_TOKEN);
		credential.setRefreshToken(Constants.REFRESH_TOKEN);
		credential.setExpirationTime(EXP_TIME);

		//Making Schedulet class instance
		scheduler = new Scheduler();
		scheduler.setAccountName(ACCOUNT_NAME);
		scheduler.setShedulerName(SCHEDULER_NAME);
		
		eScontroller = new EScontroller();
			
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetCredential() {
		serAuth.setCredential(USER_ID, scheduler, credential);
		MatCredential credRes = serAuth.getCredential(USER_ID, scheduler);
		System.out.println("test1");
		//eScontroller.setCredential(USER_ID, scheduler, credential);
		//eScontroller.getContacts(USER_ID, schedulers);
		assertEquals(credential, credRes);
	}

}
