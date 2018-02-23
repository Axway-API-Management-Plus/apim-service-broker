package com.axway.apim.servicebroker.service;

import java.util.Map;

import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

import com.axway.apim.servicebroker.exception.AxwayException;

public interface AxwayServiceBroker {

	public void importAPI(Map<String, Object> parameters, String appRouteURL, String bindingId,
			String serviceInstanceId, String email) throws ServiceBrokerInvalidParametersException, ServiceBrokerException;

	public boolean deleteAPI(String bindingId, String serviceInstanceId, String email) throws AxwayException;

	public boolean createOrgAndUser(String orgName, String email, String serviceInstanceId) throws AxwayException;

	public boolean deleteOrgAppAndUser(String email, String serviceInstanceId) throws AxwayException;
}
