package com.axway.apim.servicebroker.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.axway.apim.servicebroker.exception.AxwayAPIGatewayErrorHandler;

@Configuration
public class AxwayConfig {

	@Bean
	public RestTemplate getRestTemplate() {

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		RestTemplate restClient = new RestTemplate();
		restClient.setErrorHandler(new AxwayAPIGatewayErrorHandler());
		return restClient;
	}

	@Bean
	public BrokerApiVersion brokerApiVersion() {
		return new BrokerApiVersion();
	}

}
