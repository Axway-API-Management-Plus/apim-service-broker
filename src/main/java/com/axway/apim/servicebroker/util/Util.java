package com.axway.apim.servicebroker.util;

import java.util.Base64;

import org.springframework.http.HttpHeaders;

public final class Util {

	public static HttpHeaders createAuthHeaders(String username, String password) {

		return new HttpHeaders() {

			private static final long serialVersionUID = 1L;

			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

	
}
