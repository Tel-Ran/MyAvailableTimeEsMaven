package com.mat.interfaces;

import java.util.Arrays;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class Constants {

	public static final String ACCESS_TOKEN = "ya29.MQJSIklAPf4CmQ0Rlt_iGsLKPXqJ-azA9ZD0o4O9UgVc5sPBM34-MLu1AKAvLiCeLlqA";
	public static final String REFRESH_TOKEN = "1/g02StQeVA8giO5w-L3W3J2pAuHsLNlawkEVDeR89hRVIgOrJDtdun6zK6XiATCKT";
	public static final String CLIENT_ID = "1044767536523-51m2bc0leb0lncl3e3cpkgblb5h5htun.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "7ScqCNN8TvcxIhv169Lus_Vt";
	public static final String CALLBACK_URI = "http://localhost:8080/SampleWebApplication/index.jsp";
	public static final Iterable<String> SCOPE = Arrays.asList("profile;email".split(";"));
	public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	public static final String TYPE_OF_SERVICE = "google";
	 
	 
}


