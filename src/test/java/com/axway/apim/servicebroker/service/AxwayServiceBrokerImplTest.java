package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.exception.AxwayException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(properties = {"axway_apimanager_url=http://localhost:8081"})
public class AxwayServiceBrokerImplTest {

    private String email = "anna@axway.com";
    private String serviceInstanceId = "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";

    @Autowired
    private AxwayServiceBroker axwayServiceBroker;

    @Test
    public void shouldImportAPI() {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("type", "swagger");
        parameters.put("apiname", "pcftest");
        parameters.put("uri", "http://petstore.swagger.io/v2/swagger.json");


        axwayServiceBroker.importAPI(parameters, null, "123", serviceInstanceId, email);

    }

    @Test
    public void importAPIWithoutParamaters() {


        try {
            axwayServiceBroker.importAPI(null, null, "123", serviceInstanceId, email);
            fail("importAPIWithoutParamaters failed");
        } catch (ServiceBrokerInvalidParametersException e) {
            assertThat(e).isInstanceOf(ServiceBrokerInvalidParametersException.class).hasMessage("Custom parameters are required to add API on API Manager");
        }

    }

    @Test
    public void importAPIWithoutType() {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("apiname", "pcftest");
        parameters.put("uri", "http://petstore.swagger.io/v2/swagger.json");


        try {
            axwayServiceBroker.importAPI(parameters, null, "123", serviceInstanceId, email);
            fail("importAPIWithoutType failed");
        } catch (ServiceBrokerInvalidParametersException e) {
            assertThat(e).isInstanceOf(ServiceBrokerInvalidParametersException.class).hasMessage("Custom parameter type is required");
        }

    }
	
	/*@Test
	public void importAPIWithoutAPIName() {
	
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("type", "swagger");
		parameters.put("swaggerURI","http://petstore.swagger.io/v2/swagger.json");
		
	
		try {
			axwayServiceBroker.importAPI(parameters,null,"123", serviceInstanceId, email);
			fail("importAPIWithoutAPIName failed");
		} catch (IOException e) {
			assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Custom parameter apiName is required");
		}
		
	}*/


    @Test
    public void importAPIWithoutSwaggerURL() {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("apiname", "pcftest");
        parameters.put("type", "swagger");


        try {
            axwayServiceBroker.importAPI(parameters, null, "123", serviceInstanceId, email);
            fail("importAPIWithoutSwaggerURL failed");
        } catch (ServiceBrokerInvalidParametersException e) {
            assertThat(e).isInstanceOf(ServiceBrokerInvalidParametersException.class).hasMessage("Custom parameter uri is required");
        }

    }


    @Test
    public void apiNotAvailableToDelete() {
        try {
            boolean status = axwayServiceBroker.deleteAPI("000000-0d6c-4ae4-9880-8c28f1c9bf48", serviceInstanceId, email);
            assertThat(status);
        } catch (IOException e) {
            fail("apiNotAvailableToDelete failed");
        }

    }

    @Test
    public void doNotDeletePublishedAPI() {
        try {
            axwayServiceBroker.deleteAPI("78a38296-bded-44a9-9329-f2cd0a92e962", serviceInstanceId, email);
            fail("apiNotAvailableToDelete failed");
        } catch (IOException e) {
            assertThat(e).isInstanceOf(AxwayException.class).hasMessage("Unbind is not allowed as API is in published state");
        }

    }


    @Test
    public void shouldDeleteAPI() {


        try {
            axwayServiceBroker.deleteAPI("4be7e206-0d6c-4ae4-9880-8c28f1c9bf48", serviceInstanceId, email);
        } catch (IOException e) {
            fail("Test failed");
        }

    }

    @Test
    public void createOrgAppAndUser(){
        try {
            axwayServiceBroker.createOrgAndUser("Axway", email, serviceInstanceId );
        } catch (AxwayException e) {
            fail("Test failed");
        }
    }

    @Test
    public void deleteOrgAppAndUser(){
        try {
            axwayServiceBroker.deleteOrgAppAndUser(email, serviceInstanceId );
        } catch (AxwayException e) {
            fail("Test failed");
        }
    }

}
