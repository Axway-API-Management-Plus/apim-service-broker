package com.axway.apim.servicebroker.service;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.axway.apim.servicebroker.model.APIOrganizationAccess;
import com.axway.apim.servicebroker.model.APISecurity;
import com.axway.apim.servicebroker.model.FrondendAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AxwayAPIClient implements Constants {

	private static final Logger logger = LoggerFactory.getLogger(AxwayAPIClient.class.getName());

	@Autowired
	// @Qualifier("getRestTemplate")
	private RestTemplate restTemplate;

	@Autowired
	private String url;

	@Autowired
	private ObjectMapper mapper;

	public List<APIOrganizationAccess> listAPIs(String orgId) {
		// https://phx-107.demo.axway.com:8075/api/portal/v1.3/organizations/14df2c8b-c28c-4062-ba8b-31449524a611/apis/
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/organizations/").path(orgId)
				.path("/apis").build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.GET, uri);
		ResponseEntity<List<APIOrganizationAccess>> frondendAPIs = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<List<APIOrganizationAccess>>() {
				});

		return frondendAPIs.getBody();
	}

	public FrondendAPI getAPI(String apiId) {

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/proxies/").path(apiId).build()
				.toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.GET, uri);
		ResponseEntity<FrondendAPI> apiEntity = restTemplate.exchange(requestEntity,
				new ParameterizedTypeReference<FrondendAPI>() {
				});
		return apiEntity.getBody();
	}

	// public String listAPIsByState(String state) {
	//
	// HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);
	//
	// URI uri =
	// UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/proxies/")
	// .queryParam("field", "name").queryParam("op", "eq").queryParam("value",
	// state).build().toUri();
	//
	// ResponseEntity<String> apiEntity = restTemplate.exchange(uri,
	// HttpMethod.GET, httpEntity, String.class);
	// String responseBody = apiEntity.getBody();
	// return responseBody;
	// }

	/**
	 * To read unpublish, publish and pending APIs.
	 * 
	 * @return
	 */
	public String listAPIs() {
		// Get the API based on the name and apply the filters like unpublished
		// and bindingId
		// https://phx-107.demo.axway.com:8075/api/portal/v1.3/proxies?field=name&op=eq&value=pcftest2

		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/proxies").build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.GET, uri);
		// .queryParam("field", "name").queryParam("op",
		// "eq").queryParam("value", apiName);
		ResponseEntity<String> apiEntity = restTemplate.exchange(requestEntity, String.class);
		String responseBody = apiEntity.getBody();
		return responseBody;
	}

	public void unpublishAPI(String frondEndApiId) {
		// https://phx-107.demo.axway.com:8075/api/portal/v1.3/proxies/5a0ce063-06df-485e-8b68-07fb19242422/unpublish
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/proxies/").path(frondEndApiId)
				.path("/unpublish").build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.POST, uri);
		ResponseEntity<String> frondendAPIs = restTemplate.exchange(requestEntity, String.class);
		logger.info("API unpublished Response code {}", frondendAPIs.getStatusCodeValue());

	}

	public void deleteFrondendAPI(String frondEndApiId) {
		// delete Front End
		// https://phx-107.demo.axway.com:8075/api/portal/v1.3/proxies/c7c4a1d1-ce56-4bd3-be0b-dae0f9cf0f80
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/proxies/").path(frondEndApiId)
				.build().toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.DELETE, uri);
		logger.info("Deleting FrontEnd API");
		ResponseEntity<String> apiEntity = restTemplate.exchange(requestEntity, String.class);
		logger.info("Delete Front end API Response Code : {} ", apiEntity.getStatusCodeValue());

	}

	public void deleteBackendAPI(String backendId) {
		// Delete back end
		// https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/apirepo/${BackEndAPIID}
		logger.info("Deleting BackeEnd API");
		URI uri = UriComponentsBuilder.fromUriString(url).path(API_BASEPATH).path("/apirepo/").path(backendId).build()
				.toUri();
		RequestEntity<?> requestEntity = new RequestEntity<Object>(HttpMethod.DELETE, uri);
		ResponseEntity<String> apiEntity = restTemplate.exchange(requestEntity, String.class);
		logger.info("Delete Back end API Response Code : {}", apiEntity.getStatusCodeValue());

	}

	public String createBackend(String apiName, String orgId, String type, String swaggerURI) {

		URI uri = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/apirepo/importFromUrl").build().toUri();
		logger.info("Backend Creation URL : {}", uri.toString());
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		// Add query parameter
		postParameters.add("organizationId", orgId);
		if (apiName != null) {
			postParameters.add("name", apiName);
		}

		postParameters.add("type", type);
		postParameters.add("url", swaggerURI);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				postParameters, headers);
		ResponseEntity<String> strResponse = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
		logger.info("Create Backend API Response code: {}", strResponse.getStatusCodeValue());
		return strResponse.getBody();
	}

	// public String updateBackend(String userId, String response) {
	// JsonNode jsonNode ;
	// try {
	// jsonNode = mapper.readTree(response);
	// } catch (JsonProcessingException e) {
	// throw new ServiceBrokerException("Internal Error");
	// } catch (IOException e) {
	// throw new ServiceBrokerException("Internal Error");
	// }
	// String id = jsonNode.findPath("id").asText();
	// ((ObjectNode) jsonNode).put("createdBy" , userId);
	// URI uri = UriComponentsBuilder.fromUriString(url +
	// "/api/portal/v1.3/apirepo/" + id).build().toUri();
	// HttpEntity<JsonNode> jsonEntity = new HttpEntity<JsonNode>(jsonNode,
	// authHeader);
	// ResponseEntity<String> securityUpdate = restTemplate.exchange(uri,
	// HttpMethod.PUT, jsonEntity, String.class);
	// logger.info("Update Backend Response Code : {}",
	// securityUpdate.getStatusCodeValue());
	// return id;
	// }

	public String createFrontend(String backendAPIId, String orgId, String userId) {
		String json = "{\"apiId\":\"" + backendAPIId + "\",\"organizationId\":\"" + orgId + "\"}";
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(json);
		} catch (IOException e) {
			throw new ServiceBrokerException("Internal Error");
		}
		URI uri = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/").build().toUri();
		RequestEntity<JsonNode> jsonEntity = new RequestEntity<JsonNode>(jsonNode, HttpMethod.POST, uri);
		ResponseEntity<String> virualizedAPIResponse = restTemplate.exchange(jsonEntity, String.class);
		String frontEndAPIResponse = virualizedAPIResponse.getBody();
		return frontEndAPIResponse;
	}

	public void applySecurity(String frontEndAPIResponse, String bindingId, String userId)
			throws ServiceBrokerException {
		JsonNode jsonNode;
		try {
			jsonNode = mapper.readTree(frontEndAPIResponse);
		} catch (JsonProcessingException e) {
			throw new ServiceBrokerException("Internal Error");
		} catch (IOException e) {
			throw new ServiceBrokerException("Internal Error");
		}

		ArrayNode devices = (ArrayNode) ((ArrayNode) jsonNode.findPath("securityProfiles")).get(0).get("devices");

		String virtualAPIId = jsonNode.findPath("id").asText();
		((ObjectNode) jsonNode).put("path", "/" + bindingId);
		// ((ObjectNode) jsonNode).put("createdBy" , userId);

		logger.debug("Security Device {}", devices.getClass().getName());
		devices.addPOJO(new APISecurity());
		URI uri = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/" + virtualAPIId).build().toUri();
		RequestEntity<JsonNode> jsonEntity = new RequestEntity<JsonNode>(jsonNode, HttpMethod.PUT, uri);
		ResponseEntity<String> securityUpdate = restTemplate.exchange(jsonEntity, String.class);
		logger.info("Apply Security Response Code : {}", securityUpdate.getStatusCodeValue());
	}

}
