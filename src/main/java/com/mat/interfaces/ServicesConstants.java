package com.mat.interfaces;

public interface ServicesConstants {
	static final String GOOGLE_SERVICE_NAME = "Google";
	static final long MINUTE = 60*1000; // in milliseconds
	static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	static final String DATE_FORMAT = "yyyy-MM-dd";
	static final String OUTLOOK_SERVICE_NAME = "Outlook";
	static final String APPLICATION_NAME ="MAT";
	static final String CLIENT_ID = "1044767536523-51m2bc0leb0lncl3e3cpkgblb5h5htun.apps.googleusercontent.com"; //change to ours
	static final String CLIENT_SECRET = "7ScqCNN8TvcxIhv169Lus_Vt"; //change to ours
	static final String AUTHORIZATION = "Authorization";
	static final String BEARER = "Bearer ";
	static final String ACCEPT = "Accept";
	static final String APPLICATION = "application/json; odata.metadata=none";
	static final String CONTACTS = "https://outlook.office.com/api/v2.0/me/contacts?$select=EmailAddresses,GivenName,Surname";
	static final String ADDRESS = "Address";
	static final String GIVENNAME = "GivenName";
	static final String SURNAME = "Surname";
	
}
