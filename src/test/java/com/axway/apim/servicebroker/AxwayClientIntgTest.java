package com.axway.apim.servicebroker;

import static org.assertj.core.api.Assertions.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.axway.apim.servicebroker.service.AxwayClient;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AxwayClientIntgTest {

	@Autowired
	AxwayClient axwayClient;
	
	
	
	

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void testImportAPI() {
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("orgName", "Axway");
		parameters.put("apiName", "pcftest");
		parameters.put("swaggerURL","http://petstore.swagger.io/v2/swagger.json");
		
		//String routeURL = 
		
		try {
			axwayClient.importAPI(parameters,null,"123");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Test failed");
		}
		
	}
	
	
	@Test
	public void shouldDeleteAPI() {
		
		Map<String, Object> parameters = new HashMap<>();
		
		parameters.put("orgName", "Axway");
		parameters.put("apiName", "pcftest2");
		
		//String routeURL = 
		
		try {
			axwayClient.deleteAPI("4be7e206-0d6c-4ae4-9880-8c28f1c9bf48");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
			fail("Test failed");
		}
		
	}
	
	
	
	
	

}
