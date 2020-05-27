package com.axway.apim.servicebroker.service;


import static org.junit.Assert.fail;

import java.util.List;

import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.axway.apim.servicebroker.model.APIApplication;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(properties = { "axway_apimanager_url=http://localhost:8081/" })

public class AxwayApplicationClientTest {

	@Autowired
	private AxwayApplicationClient axwayApplicationClient;

	@Autowired
	private AxwayOrganzationClient axwayOrganzationClient;

	@MockBean
	private CloudFoundryClient cloudFoundryClient;

	@Test
	public void getApplications() {

		String orgName = "Axway";

		String orgId = axwayOrganzationClient.getOrganizationId(orgName);
		List<APIApplication> apiApplications = axwayApplicationClient.getApplications(orgId);
		if(apiApplications.isEmpty()){
			fail("No application found");
		}

	}

	
}
