package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.exception.ServiceBrokerException;
import com.axway.apim.servicebroker.model.CreateRouteBindingResponse;
import com.axway.apim.servicebroker.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ServiceInstanceBindingController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceBindingController.class);
    @Value("${axway_apimanager_traffic_url:https://phx-107.demo.axway.com:8065}")
	private String apiTrafficURL;

    private String apiManagerURL;
    private CFClient cfClient;
    private AxwayServiceBroker axwayServiceBroker;
    private ServiceBrokerHelper serviceBrokerHelper;
    private Util util;

    @Autowired
    public ServiceInstanceBindingController(CFClient cfClient, AxwayServiceBroker axwayServiceBroker
			, ServiceBrokerHelper serviceBrokerHelper, String apiManagerURL, Util util) {
        this.cfClient = cfClient;
        this.axwayServiceBroker = axwayServiceBroker;
        this.apiManagerURL = apiManagerURL;
        this.serviceBrokerHelper = serviceBrokerHelper;
        this.util = util;
    }

    @PutMapping(value = "/v2/service_instances/{instanceId}/service_bindings/{bindingId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<CreateRouteBindingResponse> createServiceInstanceBinding(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @PathVariable("bindingId") String bindingId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity,
            @RequestBody Map<String, Object> request){

        Map<String, Object> bindResource = (Map<String, Object>) request.get("bind_resource");
		String routeURL = (String) bindResource.get("route");
		logger.info("Route URL : {}", routeURL);
		if (routeURL == null) {
			throw new ServiceBrokerException("Service broker parameters are invalid: Application binding is not allowed");
		}
		String userGuid = serviceBrokerHelper.parseIdentity(originatingIdentityString);
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
		util.isValidEmail(userName);
        Map<String, Object> parameters = (Map<String, Object>) request.get("parameters");
		axwayServiceBroker.importAPI(parameters, routeURL, bindingId, serviceInstanceId, userName);
		String trafficURL = apiTrafficURL + "/" + bindingId;
		logger.info("Traffic URL for the API : {}", trafficURL);
		CreateRouteBindingResponse createRouteBindingResponse = new CreateRouteBindingResponse();
        createRouteBindingResponse.setDashboardUrl(trafficURL);
		return new ResponseEntity<>(createRouteBindingResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/v2/service_instances/{instanceId}/service_bindings/{bindingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> deleteServiceInstanceBinding(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @PathVariable("bindingId") String bindingId,
            @RequestParam("service_id") String serviceDefinitionId,
            @RequestParam("plan_id") String planId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity) {

		logger.info("UnBind Request Binding id : {}", bindingId);
		String userGuid = serviceBrokerHelper.parseIdentity(originatingIdentityString);
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
		util.isValidEmail(userName);
		try {
			boolean status = axwayServiceBroker.deleteAPI(bindingId, serviceInstanceId, userName);
			if (!status) {
				return new ResponseEntity<>(HttpStatus.GONE);
			}
		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
