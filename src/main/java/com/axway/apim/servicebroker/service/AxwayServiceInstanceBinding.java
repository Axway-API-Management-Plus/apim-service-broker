package com.axway.apim.servicebroker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.model.Context;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceRouteBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.DeleteServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AxwayServiceInstanceBinding implements ServiceInstanceBindingService {

	@Value("${axway_apimanager_traffic_url:https://phx-107.demo.axway.com:8065}")
	private String url;

	@Autowired
	private AxwayServiceBroker axwayServiceBroker;

	@Autowired
	private CFClient cfClient;

	@Autowired
	private ObjectMapper mapper;

	static final Logger logger = LoggerFactory.getLogger(AxwayServiceInstanceBinding.class.getName());

	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request) {
		String userName = null;
		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		logger.info("Bind Request Binding id : {}", bindingId);
		log(request);
		String routeURL = request.getBindResource().getRoute();
		if (routeURL == null) {
			throw new ServiceBrokerInvalidParametersException("Application binding is not allowed");
		}
		logger.info("Route URL : {}", routeURL);
		Context userContext = request.getOriginatingIdentity();
		logger.info("User Identity: {}: ", userContext);
		if (userContext != null) {
			String userGuid = (String) userContext.getProperty("user_id");
			userName = cfClient.getUserName(userGuid);
			logger.info("User Guid: {} User Name: {} ", userGuid, userName);
			Util.isValidEmail(userName);
		}

		axwayServiceBroker.importAPI(request.getParameters(), routeURL, bindingId, serviceInstanceId, userName);

		String trafficURL = url + "/" + bindingId;
		logger.info("Traffic URL for the API : {}", trafficURL);
		CreateServiceInstanceRouteBindingResponse createServiceInstanceBindingResponse = CreateServiceInstanceRouteBindingResponse
				.builder().routeServiceUrl(trafficURL).build();
		return createServiceInstanceBindingResponse;

		// app1->/guid/greeting
	}

	@Override
	public DeleteServiceInstanceBindingResponse deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {

		String userName = null;
		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		logger.info("UnBind Request Binding id : {}", bindingId);
		Context userContext = request.getOriginatingIdentity();
		logger.info("User Identity: {}: ", userContext);
		if (userContext != null) {
			String userGuid = (String) userContext.getProperty("user_id");
			userName = cfClient.getUserName(userGuid);
			logger.info("User Guid: {} User Name: {} ", userGuid, userName);
			Util.isValidEmail(userName);
		}
		try {

			boolean status = axwayServiceBroker.deleteAPI(bindingId, serviceInstanceId, userName);
			DeleteServiceInstanceBindingResponse deleteServiceInstanceBindingResponse = DeleteServiceInstanceBindingResponse.builder().build();
			if (!status)
				throw new ServiceInstanceBindingDoesNotExistException(bindingId);
			return deleteServiceInstanceBindingResponse;
		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}

	}

	public void log(Object test) {

		try {
			String request = mapper.writeValueAsString(test);
			logger.info("Request {}", request);

		} catch (JsonProcessingException e) {
			logger.error("Error processing JSON");
		}
	}

}
