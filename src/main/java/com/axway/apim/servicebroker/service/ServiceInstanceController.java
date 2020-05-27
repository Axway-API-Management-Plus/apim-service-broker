package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.exception.ServiceBrokerException;
import com.axway.apim.servicebroker.model.CreateServiceInstanceResponse;
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
public class ServiceInstanceController implements Constants{

    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);

	@Value("${axway.apim.orgname.prefix}")
	protected String orgnamePrefix;

    private CFClient cfClient;
    private AxwayServiceBroker axwayServiceBroker;;

    @Autowired
    public ServiceInstanceController(CFClient cfClient, AxwayServiceBroker axwayServiceBroker){
        this.cfClient = cfClient;
        this.axwayServiceBroker = axwayServiceBroker;
    }



    @PutMapping(value = "/v2/service_instances/{instanceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateServiceInstanceResponse> createServiceInstance(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity,
            @RequestBody String request) {
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
				createServiceInstanceResponse.setDashboardUrl(url);

			} else {
				createServiceInstanceResponse.setInstanceExisted(true);
			}
			return new ResponseEntity<>(createServiceInstanceResponse,  HttpStatus.OK);

		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/v2/service_instances/{instanceId}")
    public ResponseEntity deleteServiceInstance(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @RequestParam("service_id") String serviceDefinitionId,
            @RequestParam("plan_id") String planId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity) {

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


		try {
			boolean status = axwayServiceBroker.deleteOrgAppAndUser(userName, serviceInstanceId);
			if (!status) {
				throw new ServiceBrokerException( "Service instance does not exist: id=" + serviceInstanceId);
			}

		} catch (AxwayException e) {
			throw new ServiceBrokerException(e.getMessage());
		}
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
