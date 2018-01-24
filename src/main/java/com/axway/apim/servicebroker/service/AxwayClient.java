package com.axway.apim.servicebroker.service;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.model.APISecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;

@Service
public class AxwayClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${axway_apimanager_url:https://phx-107.demo.axway.com:8075}")
	private String url;

	@Value("${axway_apimanager_username:apiadmin}")
	private String username;

	@Value("${axway_apimanager_password:Space*52}")
	private String password;

	static final Logger logger = LoggerFactory.getLogger(AxwayClient.class.getName());

	private static ObjectMapper mapper = new ObjectMapper();

	public void importAPI(Map<String, Object> parameters, String appRouteURL, String bindingId)
			throws JsonProcessingException, IOException, AxwayException {
		

		logger.debug("Creating API Proxy on API manager");
		logger.debug("Parameters {}", parameters);
		
		if(parameters == null){
			throw new AxwayException("Custom parameters are required to add API on API Manager");
		}
		
		String orgName = (String) parameters.get("orgName");
		String apiName = (String) parameters.get("apiName");
		// String swaggerURL = "https://" + appRouteURL +"/v2/api-docs";
		String swaggerURI = (String) parameters.get("swaggerURI");
		
		logger.debug("Organziation name {}", orgName);
		logger.debug("API Name{}", apiName);
		logger.debug("Swagger URI {}", swaggerURI);
		
		if(orgName == null){
			throw new AxwayException("Custom parameter orgName is required");
		}


		if(apiName == null){
			throw new AxwayException("Custom parameter apiName is required");
		}
		

		if(swaggerURI == null){
			throw new AxwayException("Custom parameter swaggerURI is required");
		}
		
		if(!swaggerURI.startsWith("http")){
			swaggerURI = "https://"+ appRouteURL + swaggerURI;
		}



		HttpHeaders authHeader = createAuthHeaders(username, password);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/organizations/")
				.queryParam("field", "name").queryParam("op", "eq").queryParam("value", orgName);

		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);

		String orgURL = builder.toUriString();

		logger.info("Calling API : {}", orgURL);
		ResponseEntity<String> organizationEntity = callAPIGateway(orgURL, HttpMethod.GET, httpEntity);
	
		String responseBody = organizationEntity.getBody();
		String orgId = JsonPath.parse(responseBody).read("$.[0].id", String.class);
		logger.debug("Org id from API Manager :" + orgId);
		builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/apirepo/importFromUrl");

		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		// Add query parameter
		postParameters.add("organizationId", orgId);
		postParameters.add("name", apiName);
		postParameters.add("type", "swagger");
		postParameters.add("url", swaggerURI);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
				postParameters, authHeader);

		ResponseEntity<String> strResponse = callAPIGateway(builder.toUriString(), HttpMethod.POST, request);

		String virtualizedAPI = strResponse.getBody();

		String backendAPIId = JsonPath.parse(virtualizedAPI).read("$.id", String.class);
		String json = "{\"apiId\":\"" + backendAPIId + "\",\"organizationId\":\"" + orgId + "\"}";
		authHeader.setContentType(MediaType.APPLICATION_JSON);
		httpEntity = new HttpEntity<String>(json, authHeader);
		builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/");
		ResponseEntity<String> virualizedAPIResponse = callAPIGateway(builder.toUriString(), HttpMethod.POST,
				httpEntity);

		String frontEndAPIResponse = virualizedAPIResponse.getBody();

		logger.debug("JsonBody : {}", frontEndAPIResponse);
		JsonNode jsonNode = mapper.readTree(frontEndAPIResponse);

		ArrayNode devices = (ArrayNode) ((ArrayNode) jsonNode.findPath("securityProfiles")).get(0).get("devices");

		String virtualAPIId = jsonNode.findPath("id").asText();
		((ObjectNode) jsonNode).put("path", "/"+bindingId);

		logger.debug("Security Device {}", devices.getClass().getName());
		devices.addPOJO(new APISecurity());

		builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/" + virtualAPIId);
		HttpEntity<JsonNode> jsonEntity = new HttpEntity<JsonNode>(jsonNode, authHeader);
		ResponseEntity<String> securityUpdate = callAPIGateway(builder.toUriString(), HttpMethod.PUT, jsonEntity);
		logger.debug(securityUpdate.getBody());
	}
	
	
	@SuppressWarnings("unchecked")
	public void deleteAPI(String bindingId)
			throws JsonProcessingException, IOException, AxwayException {
		
		logger.info("Deleting API Proxy on API manager");
		
		HttpHeaders authHeader = createAuthHeaders(username, password);
		//Get the API based on the name and apply the filters like unpublished and bindingId
		//https://phx-107.demo.axway.com:8075/api/portal/v1.3/proxies?field=name&op=eq&value=pcftest2
		HttpEntity<?> httpEntity = new HttpEntity<Object>(authHeader);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/");
		//			.queryParam("field", "name").queryParam("op", "eq").queryParam("value", apiName);
		ResponseEntity<String> apiEntity = callAPIGateway(builder.toUriString(), HttpMethod.GET, httpEntity);
		String responseBody = apiEntity.getBody();
		List<Map<String, Object>> apis = JsonPath.parse(responseBody).read("$.*[?(@.state=='unpublished' && @.path =='/"+bindingId+"')]");
		logger.info("Paths :" +apis);
		
		if(!apis.isEmpty()){
			Map<String, Object> apiDefinition =  apis.get(0);
			String frondEndApiId = (String)apiDefinition.get("id");
			
			Object backendAPIDetails = ((Map<String, Object>)apiDefinition.get("serviceProfiles")).get("_default");
			String backendId =  (String) ((Map<String, Object>)backendAPIDetails).get("apiId");
			
			// delete Front End
			//https://phx-107.demo.axway.com:8075/api/portal/v1.3/proxies/c7c4a1d1-ce56-4bd3-be0b-dae0f9cf0f80
			builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/proxies/" + frondEndApiId);
			logger.info("Deleting FrontEnd API");
			apiEntity = callAPIGateway(builder.toUriString(), HttpMethod.DELETE, httpEntity);
			logger.info("Delete Front end API Response Code : {} ",apiEntity.getStatusCodeValue());
			//Delete back end
			//https://${APIManagerHost}:${APIManagerPort}/api/portal/v1.3/apirepo/${BackEndAPIID}
			logger.info("Deleting BackeEnd API");
			builder = UriComponentsBuilder.fromUriString(url + "/api/portal/v1.3/apirepo/" + backendId);
			apiEntity = callAPIGateway(builder.toUriString(), HttpMethod.DELETE, httpEntity);
			logger.info("Delete Back end API Response Code : {}",apiEntity.getStatusCodeValue());
			
		}else{
			apis = JsonPath.parse(responseBody).read("$.*[?(@.state=='published' && @.path =='/"+bindingId+"')]");
			if(!apis.isEmpty())
				throw new AxwayException("Unbind is not allowed as API is in published state");
			else
				throw new AxwayException("The requested API is not available in API manger");
		}
			
	}

	private ResponseEntity<String> callAPIGateway(String url, HttpMethod method, HttpEntity<?> entity) {
		return restTemplate.exchange(url, method, entity, String.class);
	}

	private HttpHeaders createAuthHeaders(String username, String password) {
		return new HttpHeaders() {
			/**
			* 
			*/
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
