package com.axway.apim.servicebroker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@Service
public class CFClient {

	@Autowired
	@Qualifier("cfOauthRestTemplate")
	private OAuth2RestTemplate cfOauthRestTemplate;

	@Value("${cc_host:https://api.sys.pie-25.cfplatformeng.com}")
	private String url;

	private static final String USERAPI_BASEPATH = "/v2/users/";
	private static final String SPACEAPI_BASEPATH = "/v2/spaces/";
	private static final String ORGAPI_BASEPATH = "/v2/organizations/";
	
	

	public String getSpaceName(String spaceGuid) {

		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(url + SPACEAPI_BASEPATH + spaceGuid,
				String.class);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String spaceName = documentContext.read("$.entity.name", String.class);
		return spaceName;

	}

	public String getUserName(String userGuid) {

		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(url + USERAPI_BASEPATH + userGuid,
				String.class);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String userName = documentContext.read("$.entity.username", String.class);
		return userName;

	}

	public String getOrg(String orgGuid) {
		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(url + ORGAPI_BASEPATH + orgGuid,
				String.class);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		String orgName = documentContext.read("$.entity.name", String.class);
		return orgName;
	}

}
