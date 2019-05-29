package com.axway.apim.servicebroker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.stereotype.Service;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.model.APIApplication;
import com.axway.apim.servicebroker.model.APIOrganization;
import com.axway.apim.servicebroker.model.APIOrganizationAccess;
import com.axway.apim.servicebroker.model.APIUser;
import com.axway.apim.servicebroker.model.FrondendAPI;
import com.axway.apim.servicebroker.model.Type;
import com.jayway.jsonpath.JsonPath;

@Service
public class AxwayServiceBrokerImpl implements AxwayServiceBroker, Constants {

	private static final Logger logger = LoggerFactory.getLogger(AxwayServiceBrokerImpl.class.getName());

	@Autowired
	private AxwayOrganzationClient axwayOrganzationClient;

	@Autowired
	private AxwayUserClient axwayUserClient;

	@Autowired
	private AxwayApplicationClient axwayApplicationClient;

	@Autowired
	private AxwayAPIClient axwayAPIClient;

	@Override
	public void importAPI(Map<String, Object> parameters, String appRouteURL, String bindingId,
						  String serviceInstanceId, String email)
			throws ServiceBrokerInvalidParametersException, ServiceBrokerException {
		logger.debug("Creating API Proxy on API manager");
		logger.debug("Parameters {}", parameters);

		if (parameters == null) {
			throw new ServiceBrokerInvalidParametersException(
					"Custom parameters are required to add API on API Manager");
		}

		String type = (String) parameters.get("type");
		String apiName = (String) parameters.get("apiname");
		// String swaggerURL = "https://" + appRouteURL +"/v2/api-docs";
		String apiURI = (String) parameters.get("uri");

		// logger.debug("Organziation name {}", orgName);
		logger.debug("API Name{}", apiName);
		logger.debug("Swagger URI {}", apiURI);

		if (type == null) {
			throw new ServiceBrokerInvalidParametersException("Custom parameter type is required");
		}
		AtomicReference<Type> enumType = new AtomicReference<>();

		try{
			enumType.set(Type.valueOf(type.toUpperCase()));
		}catch (IllegalArgumentException e) {
			throw new ServiceBrokerInvalidParametersException("Custom parameter type value can only be swagger or wsdl");
		}

		if (enumType.get().compareTo(Type.SWAGGER) == 0) {
			type = "swagger";
		} else if (enumType.get().compareTo(Type.WSDL) == 0) {
			type = "wsdl";
		}

		if (apiURI == null) {
			throw new ServiceBrokerInvalidParametersException("Custom parameter uri is required");
		}

		if (!apiURI.startsWith("http")) {
			apiURI = "https://" + appRouteURL + apiURI;
		}
		APIUser apiUser  = getOrgId(email, serviceInstanceId);
		String orgId = apiUser.getOrganizationId();
		String userId = apiUser.getId();
		logger.info("Org id from API Manager : {}" , orgId);
		String response = axwayAPIClient.createBackend(apiName, orgId, type, apiURI);
		String backendAPIId = JsonPath.parse(response).read("$.id", String.class);
		response = axwayAPIClient.createFrontend(backendAPIId, orgId, userId);
		axwayAPIClient.applySecurity(response, bindingId, appRouteURL, userId);

	}

	@Override
	public boolean deleteAPI(String bindingId, String serviceInstanceId, String email) throws AxwayException {

		logger.info("Deleting API Proxy on API manager");
		String orgId = getOrgId(email, serviceInstanceId).getOrganizationId();

		// Get the API based on the name and apply the filters like unpublished
		String responseBody = axwayAPIClient.listAPIs();
		List<Map<String, Object>> apis = JsonPath.parse(responseBody).read(
				"$.*[?(@.organizationId =='" + orgId + "' && @.cfBindingId =='" + bindingId + "' && @.state =='published')]");
		logger.info("Published APIs {} :" ,apis);

		if (!apis.isEmpty()) {
			throw new AxwayException("Unbind is not allowed as API is in published state");

		} else {
			apis = JsonPath.parse(responseBody).read(
					"$.*[?(@.organizationId =='" + orgId + "' && @.cfBindingId =='" + bindingId + "' && @.state =='unpublished')]");
			logger.info("unpublished APIs {} :" ,apis);
			if (!apis.isEmpty()) {
				Map<String, Object> apiDefinition = apis.get(0);
				String frondEndApiId = (String) apiDefinition.get("id");
				String backendId = (String) apiDefinition.get("apiId");
				axwayAPIClient.deleteFrondendAPI(frondEndApiId);
				axwayAPIClient.deleteBackendAPI(backendId);
			} else {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean createOrgAndUser(String orgName, String email, String serviceInstanceId) throws AxwayException {

		String orgId = axwayOrganzationClient.getOrganizationId(orgName);
		APIUser apiUser = axwayUserClient.getUser(email);
		if (apiUser != null && orgId != null) {
			return false;
		}

		orgId = axwayOrganzationClient.createOrganization(orgName, email, serviceInstanceId);
		String userId = axwayUserClient.createUser(orgId, email);
		axwayUserClient.resetPassword(userId);
		return true;
	}

	@Override
	public boolean deleteOrgAppAndUser(String email, String serviceInstanceId) throws AxwayException {
		logger.info("Deleting Subscription");

		List<FrondendAPI> frondendAPIs = new ArrayList<>();
		String orgId = null;
		String userId = null;
		List<APIOrganization> apiOrganizations = axwayOrganzationClient.listOrganization();
		Optional<APIOrganization> organization = apiOrganizations.stream()
				.filter(apiOrganization -> (apiOrganization.getService_instance_id() != null
						&& apiOrganization.getService_instance_id().equalsIgnoreCase(serviceInstanceId)))
				.findAny();
		if (organization.isPresent()) {
			orgId = organization.get().getId();
			APIUser apiUser = axwayUserClient.getUserByOrgId(orgId);
			userId = apiUser.getId();
		} else {
			return false;
		}
		List<APIOrganizationAccess> apiOrganizationAccesses = axwayAPIClient.listAPIs(orgId);

		for (APIOrganizationAccess apiOrganizationAccess : apiOrganizationAccesses) {
			String apiId = apiOrganizationAccess.getApiId();
			FrondendAPI frondendAPI = axwayAPIClient.getAPI(apiId);
			if (frondendAPI.getState().equals(PUBLISHED)) {
				logger.info("Publised APIs are avaialble under the organization");
				throw new AxwayException(
						"Can't delete Organization as it has published API, Please unpublish the API from API Manager");
			}
			frondendAPIs.add(frondendAPI);
		}

		for (FrondendAPI frondendAPI : frondendAPIs) {
			String frondEndApiId = frondendAPI.getId();
			String backendId = frondendAPI.getApiId();

			axwayAPIClient.deleteFrondendAPI(frondEndApiId);
			axwayAPIClient.deleteBackendAPI(backendId);
		}
		List<APIApplication> applications = axwayApplicationClient.getApplications(orgId);
		axwayApplicationClient.deleteApplications(applications);
		axwayUserClient.deleteUser(userId);
		axwayOrganzationClient.deleteOrganization(orgId);
		return true;
	}

	private APIUser getOrgId(String email, String serviceInstanceId) throws ServiceBrokerException {

		APIUser apiUser = axwayUserClient.getUser(email);
		if (apiUser == null) {
			throw new ServiceBrokerException("Access Denied : User is not exists on API Manager");
		}
		String orgId = apiUser.getOrganizationId();
		logger.info("Org id :{}", orgId);
		APIOrganization apiOrganization = axwayOrganzationClient.getOrganization(orgId);
		if (!serviceInstanceId.equals(apiOrganization.getService_instance_id())) {
			throw new ServiceBrokerException("Internal Error : Service instance id mismatch");
		}
		return apiUser;
	}
}
