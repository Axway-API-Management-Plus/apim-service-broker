package com.axway.apim.servicebroker.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.axway.apim.servicebroker.exception.AxwayAPIGatewayErrorHandler;
import com.axway.apim.servicebroker.service.AxwayAPIClient;
import com.axway.apim.servicebroker.service.AxwayApplicationClient;
import com.axway.apim.servicebroker.service.AxwayOrganzationClient;
import com.axway.apim.servicebroker.service.AxwayUserClient;
import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;


@Configuration
public class AxwayConfig {

	@Value("${axway_apimanager_url:https://localhost:8075}")
	protected String url;

	@Value("${axway_apimanager_username:apiadmin}")
	private String username;

	@Value("${axway_apimanager_password:changeme}")
	protected char[] password;

	@Value("${axway.apim.connect.timeout}")
	protected int connectTimeout;

	@Value("${axway.apim.read.timeout}")
	protected int readTimeout;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		//RestTemplate restClient = new RestTemplate();
		RestTemplate restClient = restTemplateBuilder
				.setConnectTimeout(Duration.ofMillis(connectTimeout))
				.setReadTimeout(Duration.ofMillis(readTimeout))
				.basicAuthentication(username, new String(password))
				.detectRequestFactory(false)
				.build();
		restClient.setErrorHandler(new AxwayAPIGatewayErrorHandler());
		return restClient;
	}

//	@Bean
//	public BrokerApiVersion brokerApiVersion() {
//		return new BrokerApiVersion();
//	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		return mapper;
	}

	@Bean
	public AxwayOrganzationClient axwayOrganzationClient() {
		AxwayOrganzationClient axwayOrganzationClient = new AxwayOrganzationClient();
		return axwayOrganzationClient;
	}

	@Bean
	public AxwayUserClient axwayUserClient() {
		AxwayUserClient axwayUserClient = new AxwayUserClient();
		return axwayUserClient;
	}

	@Bean
	public AxwayApplicationClient axwayApplicationClient() {
		AxwayApplicationClient axwayApplicationClient = new AxwayApplicationClient();
		return axwayApplicationClient;
	}

	@Bean
	public AxwayAPIClient axwayAPIClient() {
		AxwayAPIClient axwayAPIClient = new AxwayAPIClient();
		return axwayAPIClient;
	}

	@Bean
	public Util util() {
		return new Util();
	}

	@Bean
	public String url() {
		return url;
	}
}
