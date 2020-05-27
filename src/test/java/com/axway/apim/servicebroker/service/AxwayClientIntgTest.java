package com.axway.apim.servicebroker.service;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.axway.apim.servicebroker.service.AxwayServiceBroker;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AxwayClientIntgTest {

	@Autowired
	AxwayServiceBroker axwayServiceBroker;

	@MockBean
	private CloudFoundryClient cloudFoundryClient;


	private String email = "anna@axway.com";
	private String serviceInstanceId = "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";



	@Test
	public void contextLoads() {
	}

	@Test
	public void testImportAPI() {

		Map<String, Object> parameters = new HashMap<>();

		//parameters.put("orgName", "Axway");
		parameters.put("apiName", "pcftest");
		parameters.put("type", "swagger");
		parameters.put("URI","http://petstore.swagger.io/v2/swagger.json");

		//String routeURL =

		axwayServiceBroker.importAPI(parameters,null,"123", serviceInstanceId, email);

	}


	@Test
	public void shouldDeleteAPI() {

		Map<String, Object> parameters = new HashMap<>();

		//parameters.put("orgName", "Axway");
		parameters.put("apiname", "pcftest2");

		//String routeURL =

		try {
			axwayServiceBroker.deleteAPI("4be7e206-0d6c-4ae4-9880-8c28f1c9bf48", null, email);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failed");
		}

	}

//	@Test
//	public void getUser(){
//		HttpHeaders authHeader = Util.createAuthHeaders("apiadmin", "Space*52");
//		APIUser apiUser = axwayClient.getUserId("alex2", authHeader);
//		System.out.println(apiUser);
//		System.out.println(apiUser.getId());
//		System.out.println(apiUser.getOrganizationId());
//	}
//
//	@Test
//	public void shouldReturnOrgId(){
//		HttpHeaders authHeader = Util.createAuthHeaders("apiadmin", "Space*52");
//		String orgId = axwayClient.getOrganizationId("alex2", authHeader);
//			// TODO Auto-generated catch block
//		System.out.println(orgId);
//	}




}