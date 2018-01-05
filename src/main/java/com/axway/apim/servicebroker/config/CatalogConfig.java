package com.axway.apim.servicebroker.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.DashboardClient;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {	
	
	
	//private static String BROKER_ID="ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";

	@Bean
	public Catalog catalog() {
		return new Catalog(
				Collections.singletonList(new ServiceDefinition(getEnvOrDefault("SERVICE_ID", "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1"), // env
						getEnvOrDefault("SERVICE_NAME", "Axway-APIM"), // env
						"Axway service broker implementation", true, false, getPlans(), getTags(),
						getServiceDefinitionMetadata(), Arrays.asList("route_forwarding"), getDashboardClient())));
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

	private Map<String, Object> getPlanMetadata() {
		Map<String, Object> planMetadata = new HashMap<>();
		planMetadata.put("displayName", "Axway APIM Route Service");
		return planMetadata;
	}

	private List<String> getTags() {
		return Arrays.asList("Axway", "api", "api management", "api platform", "api gateway", "Axway Amplify"); // tags
	}

	private List<Plan> getPlans() {
		List<Plan> plans = new ArrayList<>();
		Plan plan = new Plan(getEnvOrDefault("PLAN_ID", "1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F"), // env
				"APIM", "This is a default Axway plan.  All services are created equally.", getPlanMetadata(),
				true);
		plans.add(plan);
		return plans;
	}

	private DashboardClient getDashboardClient() {
		DashboardClient dashboardClient = new DashboardClient("id", "secret", "https://axway.com");
		return dashboardClient;
	}

	private String getEnvOrDefault(final String variable, final String defaultValue) {
		String value = System.getenv(variable);
		if (value != null) {
			return value;
		} else {
			return defaultValue;
		}
	}

}
