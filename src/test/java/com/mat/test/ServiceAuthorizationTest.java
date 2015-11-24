package com.mat.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.mat.interfaces.Constants;
import com.mat.json.MatCredential;
import com.mat.json.Scheduler;
import com.mat.model.ServicesAuthorization;


//@RunWith(SpringJUnit4ClassRunner.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@ContextConfiguration(locations = {"beans.xml"})
public class ServiceAuthorizationTest {
	
	private static final String ACCOUNT_NAME = "google";

	private static final int USER_ID = 1;

	private static final String SCHEDULER_NAME = null;

	@SuppressWarnings("deprecation")
	private static final Date EXP_TIME = new Date();
	private static GoogleCredential gCredential;
	private static MatCredential credential;
	private static Scheduler scheduler;

	String testAnswer = "If you see this hessian service works";

/*public HessianProxyFactoryBean iExternalServices() {
	    HessianProxyFactoryBean factory = new HessianProxyFactoryBean();
	    factory.setServiceUrl("http://localhost:8080/MyAvailableTimeEsMaven/external_services.service");
	    factory.setServiceInterface(IExternalServices.class);
	    return factory;
*/	/*}*/
	@Autowired
	private ServicesAuthorization serAuth;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		gCredential = new GoogleCredential.Builder().setJsonFactory(Constants.JSON_FACTORY)
			     .setTransport(Constants.HTTP_TRANSPORT).setClientSecrets(Constants.CLIENT_ID, Constants.CLIENT_SECRET).build();
			credential = new MatCredential();
			credential.setAccessToken(Constants.ACCESS_TOKEN);
			credential.setRefreshToken(Constants.REFRESH_TOKEN);
			credential.setExpirationTime(EXP_TIME);
			scheduler = new Scheduler();
			scheduler.setAccountName(ACCOUNT_NAME);
			scheduler.setShedulerName(SCHEDULER_NAME);
			
			
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
		assertEquals(credential, credRes);
	}
	

}
