package com.axway.apim.servicebroker.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import com.axway.apim.servicebroker.model.APIOrganizationAccess;
import com.axway.apim.servicebroker.service.AxwayAPIClient;
import com.axway.apim.servicebroker.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AxwayAPIClientTest {

	
	
	
	@Autowired
	AxwayAPIClient axwayAPIClient;
	
	
	
//	@Test
//	public void testListAPIs(){
//		List<APIOrganizationAccess> axwayFrondendAPIs = axwayAPIClient.listAPIs("14df2c8b-c28c-4062-ba8b-31449524a611");
//	}
	
	@Test
	public void testDeleteAPI(){
		
	}

}
