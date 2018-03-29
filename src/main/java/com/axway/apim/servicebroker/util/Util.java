package com.axway.apim.servicebroker.util;

import java.util.Base64;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
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
	
	public static void isValidEmail(String email) throws ServiceBrokerInvalidParametersException{
		
		if(!EmailValidator.getInstance().isValid(email)){
			throw new ServiceBrokerInvalidParametersException("Invalid user name");
		}
	}
	
	public static String getNameFromEmail(String email){
		
		if(EmailValidator.getInstance().isValid(email)){
			return email.split("@")[0];
		}
		
		return null;
		
	}

	
}
