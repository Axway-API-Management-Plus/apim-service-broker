package com.axway.apim.servicebroker.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRouteBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.stereotype.Service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AxwayServiceInstanceBinding implements ServiceInstanceBindingService {
	
	@Value("${axway_apimanager_traffic_url:https://10.129.14.86:8065}")
	private String url;
	
	@Autowired 
	private AxwayClient axwayClient;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	static final Logger logger = LoggerFactory.getLogger(AxwayServiceInstanceBinding.class.getName());


	@Override
	public CreateServiceInstanceBindingResponse createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
		String bindingId = request.getBindingId();
		logger.info("Bind Request Binding id : {}",bindingId);
		log(request);
		String routeURL =  request.getBindResource().getRoute();
		logger.info("Route URL : {}",routeURL);
		try {
			axwayClient.importAPI(request.getParameters(), routeURL, bindingId);
		} catch (IOException e) {
			logger.error("Error calling Axway API manager",e);
			throw new ServiceBrokerException("Error Calling Axway API Manager");
		}
		String trafficURL = url+"/"+bindingId;
		logger.info("Traffic URL for the API : {}",trafficURL);
		CreateServiceInstanceRouteBindingResponse createServiceInstanceBindingResponse = new CreateServiceInstanceRouteBindingResponse().withRouteServiceUrl(trafficURL);
		return createServiceInstanceBindingResponse;
		
		//app1->/guid/greeting
	}

	@Override
	public void deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
		
		String bindingId = request.getBindingId();
		logger.info("UnBind Request Binding id : {}",bindingId);
		try {
			axwayClient.deleteAPI( bindingId);
		} catch (JsonProcessingException e) {
			throw new ServiceBrokerException("Error Calling Axway API Manager");
		} catch (AxwayException e) {
			throw new ServiceBrokerException("Error Calling Axway API Manager");
		} catch (IOException e) {
			throw new ServiceBrokerException("Error Calling Axway API Manager");
		}
		
		log(request);
		

	}
	
	
	public void log(Object test){
		
		try {
			String request = mapper.writeValueAsString(test);
			logger.info("Request {}",request);
			
		} catch (JsonProcessingException e) {
			logger.error("Error processing JSON");
		}
	}

}
