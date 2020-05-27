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
public class ServiceInstanceController implements Constants {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInstanceController.class);

    @Value("${axway.apim.orgname.prefix}")
    protected String orgNamePrefix;

    private String apiManagerURL;
    private CFClient cfClient;
    private AxwayServiceBroker axwayServiceBroker;
    private ServiceBrokerHelper serviceBrokerHelper;
    private Util util;

    @Autowired
    public ServiceInstanceController(CFClient cfClient, AxwayServiceBroker axwayServiceBroker
            , ServiceBrokerHelper serviceBrokerHelper, String apiManagerURL, Util util) {
        this.cfClient = cfClient;
        this.axwayServiceBroker = axwayServiceBroker;
        this.apiManagerURL = apiManagerURL;
        this.serviceBrokerHelper = serviceBrokerHelper;
        this.util = util;
    }

    @PutMapping(value = "/v2/service_instances/{instanceId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateServiceInstanceResponse> createServiceInstance(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity,
            @RequestHeader(value = "X-Broker-API-Version", required = false) String serviceBrokerVersion,
                    @RequestBody Map<String, Object> request) {
        //util.log(request, logger);
        String userGuid = serviceBrokerHelper.parseIdentity(originatingIdentityString);
        String userName = cfClient.getUserName(userGuid);
        logger.info("User Guid: {} User Name: {} ", userGuid, userName);
        util.isValidEmail(userName);
        Map<String, Object> context = ( Map<String, Object>) request.get("context");
        if (context == null) {
            logger.error("Cloud Foundry Context is not present");
            throw new ServiceBrokerException("Invalid Request");
        }

        String spaceName = (String) context.get("space_name");
        String cfOrgName = (String) context.get("organization_name");
        logger.info(" Space Name: {} Organization name : {}", spaceName, cfOrgName);
        String orgName = orgNamePrefix + DOT + cfOrgName + DOT + spaceName + DOT + serviceInstanceId;
        try {
            boolean status = axwayServiceBroker.createOrgAndUser(orgName, userName, serviceInstanceId);
            CreateServiceInstanceResponse createServiceInstanceResponse = new CreateServiceInstanceResponse();
            if (status) {
                createServiceInstanceResponse.setDashboardUrl(apiManagerURL);

            } else {
                createServiceInstanceResponse.setInstanceExisted(true);
            }
            return new ResponseEntity<>(createServiceInstanceResponse, HttpStatus.OK);

        } catch (AxwayException e) {
            throw new ServiceBrokerException(e.getMessage());
        }
       //return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/v2/service_instances/{instanceId}")
    public ResponseEntity<String> deleteServiceInstance(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @RequestParam("service_id") String serviceDefinitionId,
            @RequestParam("plan_id") String planId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity,
            @RequestHeader(value = "X-Broker-API-Version", required = false) String serviceBrokerVersion) {

        String userGuid = serviceBrokerHelper.parseIdentity(originatingIdentityString);
        String userName = cfClient.getUserName(userGuid);
        logger.info("User Guid: {} User Name: {} ", userGuid, userName);
        util.isValidEmail(userName);
        try {
            boolean status = axwayServiceBroker.deleteOrgAppAndUser(userName, serviceInstanceId);
            if (!status) {
                logger.error("API Manager Organization does not exist" );
                return new ResponseEntity<>("{}",HttpStatus.OK);

            }
        } catch (AxwayException e) {
            throw new ServiceBrokerException(e.getMessage());
        }
        return new ResponseEntity<>("{}",HttpStatus.OK);
    }
}
