package com.axway.apim.servicebroker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import com.axway.apim.servicebroker.exception.AxwayAPIGatewayErrorHandler;

@Configuration
public class CloudFoundryConfig {
	
	private OAuth2ClientContext oauth2ClientContext = new DefaultOAuth2ClientContext();
	
	
	@Value("${cf_uaa_username:pcf@axway.com}")
	private String username;

	@Value("${cf_uaa_password:changeme}")
	private String password;
	
	@Value("${cf_uaa_access_token_url:https://login.pcf.axway.com/oauth/token}")
	private String accessTokenURI;

	
	@Bean
	public OAuth2ProtectedResourceDetails cf() {
		ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
		details.setUsername(username);
		details.setPassword(password);
		details.setGrantType("password");
		details.setAccessTokenUri(accessTokenURI);
		return details;
	}

	@Bean
	OAuth2RestTemplate cfOauthRestTemplate(){
		
		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(cf(),oauth2ClientContext);
		oAuth2RestTemplate.setAccessTokenProvider(new CFResourceOwnerPasswordAccessTokenProvider());
		oAuth2RestTemplate.setErrorHandler(new AxwayAPIGatewayErrorHandler());
		return oAuth2RestTemplate;
	}

}
