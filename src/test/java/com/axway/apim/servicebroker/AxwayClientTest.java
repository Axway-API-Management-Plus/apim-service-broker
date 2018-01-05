package com.axway.apim.servicebroker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.service.AxwayClient;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(properties = { "axway_apimanager_url=http://localhost:8081" })
public class AxwayClientTest {

	@Autowired
	private AxwayClient axwayClient;

	@Test
	public void shouldImportAPI() {
	
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("orgName", "Axway");
		parameters.put("apiName", "pcftest");
		parameters.put("swaggerURI","http://petstore.swagger.io/v2/swagger.json");
		
	
		try {
			axwayClient.importAPI(parameters,null,"123");
		} catch (IOException e) {
			fail("Test failed");
		}
		
	}
	
	@Test
	public void importAPIWithoutParamaters() {
	
		
		try {
			axwayClient.importAPI(null,null,"123");
			fail("importAPIWithoutParamaters failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Custom parameters are required to add API on API Manager");
		}
		
	}
	
	@Test
	public void importAPIWithoutOrgName() {
	
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("apiName", "pcftest");
		parameters.put("swaggerURL","http://petstore.swagger.io/v2/swagger.json");
		
	
		try {
			axwayClient.importAPI(parameters,null,"123");
			fail("importAPIWithoutOrgName failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Custom parameter orgName is required");
		}
		
	}
	
	@Test
	public void importAPIWithoutAPIName() {
	
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("orgName", "Axway");
		parameters.put("swaggerURI","http://petstore.swagger.io/v2/swagger.json");
		
	
		try {
			axwayClient.importAPI(parameters,null,"123");
			fail("importAPIWithoutAPIName failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Custom parameter apiName is required");
		}
		
	}
	
	
	@Test
	public void importAPIWithoutSwaggerURL() {
	
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("apiName", "pcftest");
		parameters.put("orgName", "Axway");
		
	
		try {
			axwayClient.importAPI(parameters,null,"123");
			fail("importAPIWithoutSwaggerURL failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Custom parameter swaggerURI is required");
		}
		
	}
	
	
	@Test
	public void apiNotAvailableToDelete() {
		
		
		try {
			axwayClient.deleteAPI("000000-0d6c-4ae4-9880-8c28f1c9bf48");
			fail("apiNotAvailableToDelete failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("The requested API is not available in API manger");
		}
		
	}
	
	@Test
	public void doNotDeletePubllishedAPI() {
		
	
		
		try {
			axwayClient.deleteAPI("78a38296-bded-44a9-9329-f2cd0a92e962");
			fail("apiNotAvailableToDelete failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Unbind is not allowed as API is in published state");
		}
		
	}
	

	@Test
	public void shouldDeleteAPI() {

	
		try {
			axwayClient.deleteAPI("4be7e206-0d6c-4ae4-9880-8c28f1c9bf48");
		} catch (IOException e) {
			fail("Test failed");
		}

	}

}
