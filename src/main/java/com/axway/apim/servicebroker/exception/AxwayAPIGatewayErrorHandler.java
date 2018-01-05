package com.axway.apim.servicebroker.exception;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import com.axway.apim.servicebroker.service.AxwayClient;

public class AxwayAPIGatewayErrorHandler implements ResponseErrorHandler {

	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
	static final Logger logger = LoggerFactory.getLogger(AxwayClient.class.getName());

	public AxwayAPIGatewayErrorHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		
		
		String responseBody = IOUtils.toString(response.getBody(),"UTF-8");
		int statusCode = response.getStatusCode().value();
		logger.error("Http Status Code : {}",statusCode);
		logger.error("Error message from Gateway : {}",responseBody);
		throw new AxwayException("Internal Server Error");

	}

	@Override
	public boolean hasError(ClientHttpResponse arg0) throws IOException {
		return errorHandler.hasError(arg0);
	}

}
