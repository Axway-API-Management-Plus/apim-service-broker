package com.axway.apim.servicebroker.service;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.model.APIOrganization;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

public class AxwayOrganzationClient implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(AxwayOrganzationClient.class.getName());

	@Autowired
	private String url;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	// @Qualifier("getRestTemplate")
	private RestTemplate restTemplate;

	public String createOrganization(String orgName, String email, String serviceInstanceId) {

		APIOrganization apiOrganization = new APIOrganization();
		apiOrganization.setName(orgName);
		apiOrganization.setEmail(email);
		apiOrganization.setService_instance_id(serviceInstanceId);
		JsonNode jsonNode = mapper.convertValue(apiOrganization, JsonNode.class);
		// https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/organizations
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations").build().toUri();
		RequestEntity<JsonNode> jsonEntity = new RequestEntity<JsonNode>(jsonNode, HttpMethod.POST,uri);
		ResponseEntity<String> organization = restTemplate.exchange(jsonEntity, String.class);
		logger.info("Create Organization Response code : {}", organization.getStatusCodeValue());

		try {
			return JsonPath.parse(organization.getBody()).read("$.id", String.class);
		} catch (PathNotFoundException e) {
			logger.error("Unable to retrieve Organization detail", e);
			return null;
		}

	}

	public void deleteOrganization(String orgId){

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations/").path(orgId).build()
				.toUri();
		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE,uri);
		ResponseEntity<String> userEntity = restTemplate.exchange(requestEntity, String.class);
		logger.info("Delete Organization Response Code : {} ", userEntity.getStatusCodeValue());
	}

	public String getOrganizationId(String orgName) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations")
				.queryParam("field", "name").queryParam("op", "eq").queryParam("value", orgName).build().toUri();

		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
		logger.info("Calling API : {}", uri.toString());
		ResponseEntity<String> organizationEntity = restTemplate.exchange(requestEntity, String.class);
		String responseBody = organizationEntity.getBody();
		try {
			return JsonPath.parse(responseBody).read("$.[0].id", String.class);
		} catch (PathNotFoundException e) {
			logger.error("Unable to retrieve Organization detail", e);
			return null;
		}

	}

	public void updateOrganization(APIOrganization apiOrganization) {
		JsonNode jsonNode = mapper.convertValue(apiOrganization, JsonNode.class);
		
		// https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/organizations
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations").build().toUri();
		RequestEntity<JsonNode> jsonEntity = new RequestEntity<>(jsonNode, HttpMethod.PUT, uri);
		ResponseEntity<String> organization = restTemplate.exchange(jsonEntity, String.class);
		logger.info(" Organziation Response code : {}", organization.getStatusCodeValue());
	}

	public APIOrganization getOrganization(String orgId) {
		
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations/").path(orgId).build()
				.toUri();
		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
		logger.info("Calling Get Organization API : {}", uri.toString());
		ResponseEntity<APIOrganization> orgEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<APIOrganization>() {
				});
		return orgEntity.getBody();
	}

	public List<APIOrganization> listOrganization() {

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations").build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
		logger.info("Calling List Organization API : {}", uri.toString());
		ResponseEntity<List<APIOrganization>> orgEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<List<APIOrganization>>() {
				});
		return orgEntity.getBody();
	}

}
