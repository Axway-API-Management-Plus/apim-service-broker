package com.axway.apim.servicebroker.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.servicebroker.model.BindResource;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.axway.apim.servicebroker.BaseClass;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AxwayServiceInstanceBindingTest extends BaseClass {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AxwayServiceBroker axwayServiceBroker;
	@MockBean
	private CFClient cfClient;

	private ObjectMapper objectMapper = new ObjectMapper();

	private String service_id = "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";
	private String plan_id = "1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F";


	private String instance_id = "5a76d1c5-4bc3-455a-98b1-e3c079dc5cb2";
	private String binding_id = "7ed4c3d3-c3a4-41b6-9acc-72b3a7fa2f39";
	

	@Test
	public void shouldCreateServiceBinding() throws Exception {
		Map<String, Object> parameters = new HashMap<>();

		parameters.put("apiname", "pcftest");
		parameters.put("uri", "http://petstore.swagger.io/v2/swagger.json");
		Map<String, Object> bind_resource = new HashMap<String, Object>();
		
		BindResource bindResource = new BindResource("","testapp.axway.com",bind_resource);
		

		CreateServiceInstanceBindingRequest createServiceInstanceBindingRequest = new CreateServiceInstanceBindingRequest(
				service_id, plan_id,  bindResource, null,parameters);

		String request = objectMapper.writeValueAsString(createServiceInstanceBindingRequest);
		mockMvc.perform(MockMvcRequestBuilders
				.put("/v2/service_instances/{instance_id}/service_bindings/{binding_id}", instance_id, binding_id)
				.with(httpBasic(username, password)).contentType(MediaType.APPLICATION_JSON).content(request))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.route_service_url", containsString("https")));
	}

	@Test
	public void shouldDeleteServiceBinding() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/v2/service_instances/{instance_id}/service_bindings/{binding_id}", instance_id, binding_id)
				.param("service_id", service_id).param("plan_id", plan_id).header("X-Broker-API-Version", "2.13")
				.header("X-Broker-API-Originating-Identity", "cloudfoundry eyANCiAgInVzZXJfaWQiOiAiNjgzZWE3NDgtMzA5Mi00ZmY0LWI2NTYtMzljYWNjNGQ1MzYwIiwNCiAgInVzZXJfbmFtZSI6ICJqb2VAZXhhbXBsZS5jb20iDQp9")
				.with(httpBasic(username, password))).andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}

}
