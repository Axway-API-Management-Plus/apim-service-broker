package com.axway.apim.servicebroker.exception;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

public class AxwayAPIGatewayErrorHandler implements ResponseErrorHandler {

	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
	static final Logger logger = LoggerFactory.getLogger(AxwayAPIGatewayErrorHandler.class.getName());


	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		String responseBody = IOUtils.toString(response.getBody(), "UTF-8");
		logger.error("Error message from Gateway : {}", responseBody);

	}

	@Override
	public boolean hasError(ClientHttpResponse arg0) throws IOException {
		return errorHandler.hasError(arg0);
	}

}