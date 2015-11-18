package com.mat.model;

import java.net.URISyntaxException;

import microsoft.exchange.webservices.data.core.request.HttpWebRequest;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;

public class OAuth2Credentials extends ExchangeCredentials {
	private String accessToken;

	public OAuth2Credentials(String accessToken) {
		if (accessToken != null)
			this.accessToken = accessToken;
	}

	@Override
	public void prepareWebRequest(HttpWebRequest client) throws URISyntaxException {
		super.prepareWebRequest(client);
		if (client.getHeaders() != null)
			client.getHeaders().put("Authorization", "Bearer " + accessToken);
	}
}
