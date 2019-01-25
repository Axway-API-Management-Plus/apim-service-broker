package com.axway.apim.servicebroker.service;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.model.APIUser;
import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

public class AxwayUserClient implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(AxwayUserClient.class.getName());

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private HttpHeaders authHeader;

	@Autowired
	private String url;

	@Autowired
	//@Qualifier("getRestTemplate")
	private RestTemplate restTemplate;

	public APIUser getUser(String username) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users")
				.queryParam("field", "loginName").queryParam("op", "eq").queryParam("value", username).build().toUri();

		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);
		logger.info("Calling API : {}", uri.toString());
		ResponseEntity<List<APIUser>> userEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<APIUser>>() {
				});

		List<APIUser> apiUsers = userEntity.getBody();
		logger.info("Response Code for get User : {}",userEntity.getStatusCodeValue());
		if (apiUsers.isEmpty()) {
			return null;
		}
		return apiUsers.iterator().next();
	}
	
	public APIUser getUserByOrgId(String orgId) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users")
				.queryParam("field", "orgid").queryParam("op", "eq").queryParam("value", orgId).build().toUri();

		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);
		logger.info("Calling API : {}", uri.toString());
		ResponseEntity<List<APIUser>> userEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity,
				new ParameterizedTypeReference<List<APIUser>>() {
				});

		List<APIUser> apiUsers = userEntity.getBody();
		if (apiUsers.isEmpty()) {
			return null;
		}
		return apiUsers.iterator().next();
	}

	public String createUser(String organizationId, String email) throws AxwayException {
		APIUser apiUser = new APIUser();
		apiUser.setLoginName(email);
		String name = Util.getNameFromEmail(email);
		apiUser.setName(name);
		apiUser.setEmail(email);
		apiUser.setOrganizationId(organizationId);
		JsonNode jsonNode = mapper.convertValue(apiUser, JsonNode.class);
		HttpEntity<JsonNode> jsonEntity = new HttpEntity<JsonNode>(jsonNode, authHeader);
		// https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/organizations
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users").build().toUri();
		ResponseEntity<String> user = restTemplate.exchange(uri, HttpMethod.POST, jsonEntity, String.class);
		int httpStatusCode = user.getStatusCodeValue();
		logger.info("Create User Response code : {}", httpStatusCode);
		if (httpStatusCode != HttpStatus.CREATED.value()) {
			throw new AxwayException("A user with the supplied login name already exists");
		}
		String userId = JsonPath.parse(user.getBody()).read("$.id", String.class);
		return userId;
		
	}
	
	public void resetPassword(String userId){
		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users/").path(userId)
				.path("/resetpassword").build().toUri();

		restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, String.class);

	}

	public void deleteUser(String id) throws AxwayException {

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users/").path(id).build().toUri();
		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);
		ResponseEntity<String> userEntity = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, String.class);
		logger.info("Delete User Response Code : {} ", userEntity.getStatusCodeValue());
	}

}
