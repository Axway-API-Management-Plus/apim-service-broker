package com.axway.apim.servicebroker.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.catalog.Catalog;
import org.springframework.cloud.servicebroker.model.catalog.DashboardClient;
import org.springframework.cloud.servicebroker.model.catalog.Plan;
import org.springframework.cloud.servicebroker.model.catalog.ServiceDefinition;
import org.springframework.cloud.servicebroker.service.CatalogService;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AxwayCatalogService implements CatalogService {

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public Catalog getCatalog() {
		
		return Catalog.builder()
				  .serviceDefinitions(getServiceDefinition("ED01F448-40C7-4A9D-93D0-51E7D4E93CA1"))
				  .build();
	}

	@Override
	public ServiceDefinition getServiceDefinition(String serviceId) {
		ServiceDefinition serviceDefinition = ServiceDefinition.builder()
				.id(serviceId)
				.name("Axway-APIM")
				.description("Axway Service Broker Implementation")
				.bindable(true)
				.tags(getTags())
				.plans(getPlans())
				.metadata(getServiceDefinitionMetadata())
				.requires("route_forwarding")
				.dashboardClient(getDashboardClient())
				.build();
				return serviceDefinition;
	}

	
	/* Used by Pivotal CF console */

	private Map<String, Object> getServiceDefinitionMetadata() {
		Map<String, Object> sdMetadata = new HashMap<>();
		sdMetadata.put("displayName", "Axway Amplify APIM");
		sdMetadata.put("imageUrl", "https://cdn.axway.com/globalnav/axway-logo-top.svg");
		sdMetadata.put("longDescription", "Axway Amplify APIM Service Broker");
		sdMetadata.put("providerDisplayName", "Axway");
		sdMetadata.put("documentationUrl", "https://github.com/Axway-API-Management-Plus");
		sdMetadata.put("supportUrl", "https://github.com/Axway-API-Management-Plus");
		return sdMetadata;
	}


	private List<String> getTags() {
		return Arrays.asList("Axway", "api", "api management", "api platform", "api gateway", "Axway Amplify"); // tags
	}

	private List<Plan> getPlans() {
		InputStream inputStream = null;
		try {
			inputStream = resourceLoader.getResource("classpath:plan.json").getInputStream();
			TypeReference<List<Plan>> type = new TypeReference<List<Plan>>() {
			};
			List<Plan> plans = mapper.readValue(inputStream, type);
			return plans;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {

				}
		}
		return null;
	}

	private DashboardClient getDashboardClient() {
		DashboardClient dashboardClient = DashboardClient.builder().redirectUri("https://axway.com").build();
		return dashboardClient;
	}


}
