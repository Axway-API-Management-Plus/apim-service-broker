package com.axway.apim.servicebroker.service;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
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
	private Util util;

	@Autowired
	private String url;

	@Autowired
	// @Qualifier("getRestTemplate")
	private RestTemplate restTemplate;

	public APIUser getUser(String username) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users")
				.queryParam("field", "loginName").queryParam("op", "eq").queryParam("value", username).build().toUri();
		return getUser(uri);

	}


	private APIUser getUser(URI uri){
		logger.info("Calling API : {}", uri.toString());
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.GET, uri);
		ResponseEntity<List<APIUser>> userEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<List<APIUser>>() {
				});

		List<APIUser> apiUsers = userEntity.getBody();
		int statusCode = userEntity.getStatusCodeValue();
		logger.info("Response Code for get User : {}", userEntity.getStatusCodeValue());
		if (apiUsers.isEmpty()) {
			return null;
		}
		return apiUsers.iterator().next();
	}

	public APIUser getUserByOrgId(String orgId) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users").queryParam("field", "orgid")
				.queryParam("op", "eq").queryParam("value", orgId).build().toUri();
		return getUser(uri);
	}

	public String createUser(String organizationId, String email) throws AxwayException {
		APIUser apiUser = new APIUser();
		apiUser.setLoginName(email);
		String name = util.getNameFromEmail(email);
		apiUser.setName(name);
		apiUser.setEmail(email);
		apiUser.setOrganizationId(organizationId);
		JsonNode jsonNode = mapper.convertValue(apiUser, JsonNode.class);
		// https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/organizations
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users").build().toUri();
		RequestEntity<JsonNode> requestEntity = new RequestEntity<JsonNode>(jsonNode, HttpMethod.POST, uri);
		ResponseEntity<String> user = restTemplate.exchange(requestEntity, String.class);
		int httpStatusCode = user.getStatusCodeValue();
		logger.info("Create User Response code : {}", httpStatusCode);
		if (httpStatusCode != HttpStatus.CREATED.value()) {
			throw new AxwayException("A user with the supplied login name already exists");
		}

		String userId = JsonPath.parse(user.getBody()).read("$.id", String.class);
		return userId;

	}

	public void resetPassword(String userId) {

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users/").path(userId)
				.path("/resetpassword").build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.PUT, uri);
		restTemplate.exchange(uri, HttpMethod.PUT, requestEntity, String.class);

	}

	public void deleteUser(String id) throws AxwayException {

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/users/").path(id).build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.DELETE, uri);
		ResponseEntity<String> userEntity = restTemplate.exchange(requestEntity, String.class);
		logger.info("Delete User Response Code : {} ", userEntity.getStatusCodeValue());
	}

}
