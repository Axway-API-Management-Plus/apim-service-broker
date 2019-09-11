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

import com.axway.apim.servicebroker.model.APIApplication;

public class AxwayApplicationClient implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(AxwayApplicationClient.class.getName());

	@Autowired
	private String url;

	@Autowired
	// @Qualifier("getRestTemplate")
	private RestTemplate restTemplate;

	public List<APIApplication> getApplications(String orgId) {
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/applications")
					.queryParam("field", "orgid").queryParam("op", "eq").queryParam("value", orgId).build().toUri();

		RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.GET, uri);
		logger.info("Calling API : {}", uri.toString());
		ResponseEntity<List<APIApplication>> userEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<List<APIApplication>>() {
				});
		return  userEntity.getBody();
	}

	public void deleteApplications(List<APIApplication> applications) {

		for (APIApplication apiApplication : applications) {
			String appId = apiApplication.getId();
			URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/applications/").path(appId)
					.build().toUri();
			RequestEntity<?> requestEntity = new RequestEntity<>(HttpMethod.DELETE, uri);
			ResponseEntity<String> userEntity = restTemplate.exchange(requestEntity, String.class);
			logger.info("Delete Application Response Code : {} ", userEntity.getStatusCodeValue());

		}
	}

}
