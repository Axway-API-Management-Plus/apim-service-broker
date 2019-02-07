package com.axway.apim.servicebroker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.springframework.cloud.servicebroker.model.CloudFoundryContext;
import org.springframework.cloud.servicebroker.model.Context;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationRequest;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationResponse;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.stereotype.Service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AxwayServiceInstanceService implements ServiceInstanceService, Constants {

	@Autowired
	private String url;

	@Autowired
	private AxwayServiceBroker axwayServiceBroker;;

	@Autowired
	private CFClient cfClient;

	@Value("${axway.apim.orgname.prefix}")
	protected String orgnamePrefix;

	@Autowired
	private ObjectMapper mapper;

	private static final Logger logger = LoggerFactory.getLogger(AxwayServiceInstanceService.class.getName());

	@Override
	public CreateServiceInstanceResponse createServiceInstance(
			CreateServiceInstanceRequest createServiceInstanceRequest) {
		log(createServiceInstanceRequest);

		Context userContext = createServiceInstanceRequest.getOriginatingIdentity();

		logger.info("CreateServiceInstance: User Identity: {}: ", userContext);
		if (userContext == null) {
			logger.error("OriginatingIdentity is not present");
			throw new ServiceBrokerException("Invalid Request");
		}

		String userGuid = (String) userContext.getProperty("user_id");
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);

		Util.isValidEmail(userName);

		String serviceInstanceId = createServiceInstanceRequest.getServiceInstanceId();
		logger.info("Service Instance Id: {}", serviceInstanceId);
		CloudFoundryContext context = (CloudFoundryContext) createServiceInstanceRequest.getContext();
		if (context == null) {
			logger.error("Cloud Foundry Context is not present");
			throw new ServiceBrokerException("Invalid Request");
		}
		String spaceGuid = context.getSpaceGuid();
		String orgGuid = context.getOrganizationGuid();

		logger.info("Space Guid: {}: ", spaceGuid);

		String spaceName = cfClient.getSpaceName(spaceGuid);
		String cfOrgName = cfClient.getOrg(orgGuid);
		String orgName = orgnamePrefix + DOT + cfOrgName + DOT + spaceName + DOT + serviceInstanceId;

		logger.info(" Space Name: {} ", spaceName);
		try {
			boolean status = axwayServiceBroker.createOrgAndUser(orgName, userName, serviceInstanceId);
			CreateServiceInstanceResponse createServiceInstanceResponse = new CreateServiceInstanceResponse();
			if (status) {
				createServiceInstanceResponse.withDashboardUrl(url);

			} else {
				createServiceInstanceResponse.withInstanceExisted(true);
			}
			return createServiceInstanceResponse;

		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}

	}

	@Override
	public DeleteServiceInstanceResponse deleteServiceInstance(
			DeleteServiceInstanceRequest deleteServiceInstanceRequest) {

		log(deleteServiceInstanceRequest);
		Context userContext = deleteServiceInstanceRequest.getOriginatingIdentity();
		logger.info("DeleteServiceInstanceResponse: User identity {} ",
				deleteServiceInstanceRequest.getOriginatingIdentity());

		if (userContext == null) {
			logger.error("OriginatingIdentity is not present");
			throw new ServiceBrokerException("Invalid Request");
		}

		String userGuid = (String) userContext.getProperty("user_id");
		String userName = cfClient.getUserName(userGuid);
		logger.info("User Guid: {} User Name: {} ", userGuid, userName);
		Util.isValidEmail(userName);

		String serviceInstanceId = deleteServiceInstanceRequest.getServiceInstanceId();
		try {
			boolean status = axwayServiceBroker.deleteOrgAppAndUser(userName, serviceInstanceId);
			if (!status) {
				throw new ServiceInstanceDoesNotExistException(serviceInstanceId);
			}
			DeleteServiceInstanceResponse deleteServiceInstanceResponse = new DeleteServiceInstanceResponse();
			return deleteServiceInstanceResponse;
		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}
	}

	@Override
	public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest arg0) {
		UpdateServiceInstanceResponse updateServiceInstanceResponse = new UpdateServiceInstanceResponse();
		return updateServiceInstanceResponse;
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
