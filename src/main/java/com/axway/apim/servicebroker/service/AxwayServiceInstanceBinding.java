package com.axway.apim.servicebroker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceBindingDoesNotExistException;
import org.springframework.cloud.servicebroker.model.Context;
//import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
//import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
//import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRouteBindingResponse;
//import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.*;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

@Service
public class AxwayServiceInstanceBinding implements ServiceInstanceBindingService {

	@Value("${axway_apimanager_traffic_url:https://phx-107.demo.axway.com:8065}")
	private String url;

	@Autowired
	private AxwayServiceBroker axwayServiceBroker;

	@Autowired
	private CFClient cfClient;
	
	@Autowired
	private Util util;

	@Autowired
	private ObjectMapper mapper;

	static final Logger logger = LoggerFactory.getLogger(AxwayServiceInstanceBinding.class.getName());

//	@Override
//	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
//			CreateServiceInstanceBindingRequest request) {
//		String bindingId = request.getBindingId();
//		String serviceInstanceId = request.getServiceInstanceId();
//		logger.info("Bind Request Binding id : {}", bindingId);
//		log(request);
//		String routeURL = request.getBindResource().getRoute();
//		logger.info("Route URL : {}", routeURL);
//		if (routeURL == null) {
//			throw new ServiceBrokerInvalidParametersException("Application binding is not allowed");
//		}
//		Context userContext = request.getOriginatingIdentity();
//		logger.info("User Identity: {}: ", userContext);
//
//		if (userContext == null) {
//			logger.error("OriginatingIdentity is not present");
//			throw new ServiceBrokerException("Invalid Request");
//		}
//		String userGuid = (String) userContext.getProperty("user_id");
//		String userName = cfClient.getUserName(userGuid);
//		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
//		util.isValidEmail(userName);
//
//		axwayServiceBroker.importAPI(request.getParameters(), routeURL, bindingId, serviceInstanceId, userName);
//
//		String trafficURL = url + "/" + bindingId;
//		logger.info("Traffic URL for the API : {}", trafficURL);
//		CreateServiceInstanceRouteBindingResponse createServiceInstanceBindingResponse = new CreateServiceInstanceRouteBindingResponse()
//				.withRouteServiceUrl(trafficURL);
//		return createServiceInstanceBindingResponse;
//
//		// app1->/guid/greeting
//	}
//
//	@Override
//	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
//
//		String bindingId = request.getBindingId();
//		String serviceInstanceId = request.getServiceInstanceId();
//		logger.info("UnBind Request Binding id : {}", bindingId);
//		Context userContext = request.getOriginatingIdentity();
//		logger.info("User Identity: {}: ", userContext);
//
//		if (userContext == null) {
//			logger.error("OriginatingIdentity is not present");
//			throw new ServiceBrokerException("Invalid Request");
//		}
//
//		String userGuid = (String) userContext.getProperty("user_id");
//		String userName = cfClient.getUserName(userGuid);
//		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
//		util.isValidEmail(userName);
//		try {
//
//			boolean status = axwayServiceBroker.deleteAPI(bindingId, serviceInstanceId, userName);
//			if (!status)
//				throw new ServiceInstanceBindingDoesNotExistException(bindingId);
//		} catch (AxwayException e) {
//			throw new ServiceBrokerException(e.getMessage());
//		}
//
//	}

	@Override
	public Mono<CreateServiceInstanceBindingResponse> createServiceInstanceBinding(
			CreateServiceInstanceBindingRequest request) {

		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		logger.info("Bind Request Binding id : {}", bindingId);
		log(request);
		String routeURL = request.getBindResource().getRoute();
		logger.info("Route URL : {}", routeURL);
		if (routeURL == null) {
			throw new ServiceBrokerInvalidParametersException("Application binding is not allowed");
		}
		Context userContext = request.getOriginatingIdentity();
		logger.info("User Identity: {}: ", userContext);

		if (userContext == null) {
			logger.error("OriginatingIdentity is not present");
			throw new ServiceBrokerException("Invalid Request");
		}
		String userGuid = (String) userContext.getProperty("user_id");
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
		util.isValidEmail(userName);

		axwayServiceBroker.importAPI(request.getParameters(), routeURL, bindingId, serviceInstanceId, userName);

		String trafficURL = url + "/" + bindingId;
		logger.info("Traffic URL for the API : {}", trafficURL);


		return Mono.just(CreateServiceInstanceRouteBindingResponse.builder().routeServiceUrl(trafficURL).build());

	}

	@Override
	public Mono<DeleteServiceInstanceBindingResponse> deleteServiceInstanceBinding(
			DeleteServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		String serviceInstanceId = request.getServiceInstanceId();
		logger.info("UnBind Request Binding id : {}", bindingId);
		Context userContext = request.getOriginatingIdentity();
		logger.info("User Identity: {}: ", userContext);

		if (userContext == null) {
			logger.error("OriginatingIdentity is not present");
			throw new ServiceBrokerException("Invalid Request");
		}

		String userGuid = (String) userContext.getProperty("user_id");
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
		util.isValidEmail(userName);
		try {

			boolean status = axwayServiceBroker.deleteAPI(bindingId, serviceInstanceId, userName);

			if (!status)
				return Mono.error(new ServiceInstanceBindingDoesNotExistException(bindingId));
		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}
		return Mono.just(DeleteServiceInstanceBindingResponse.builder().build());
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
