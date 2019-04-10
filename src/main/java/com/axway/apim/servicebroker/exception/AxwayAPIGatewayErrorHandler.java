package com.axway.apim.servicebroker.exception;

import java.io.IOException;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

public class AxwayAPIGatewayErrorHandler implements ResponseErrorHandler {


	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();
	private static final Logger logger = LoggerFactory.getLogger(AxwayAPIGatewayErrorHandler.class.getName());


	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		String responseBody = IOUtils.toString(response.getBody(), "UTF-8");
		logger.error("Error message from Gateway : {}", responseBody);
		MediaType contentType = response.getHeaders().getContentType();
		if(contentType.includes(MediaType.APPLICATION_JSON)){
			DocumentContext documentContext = JsonPath.parse(responseBody);
			String errorDesc = documentContext.read("$.errors[0].message", String.class);
			throw new AxwayException(errorDesc);
		}else{
			throw new AxwayException("Unknown Error From API Manager");
		}
	}

	@Override
	public boolean hasError(ClientHttpResponse arg0) throws IOException {
		return errorHandler.hasError(arg0);
	}

}