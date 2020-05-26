package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.servicebroker.model.CloudFoundryContext;
import org.springframework.cloud.servicebroker.model.Context;
import org.springframework.cloud.servicebroker.model.binding.BindResource;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@WithMockUser
@AutoConfigureWebTestClient
public class AxwayServiceInstanceBindingTest  {



	@MockBean
	private AxwayServiceBroker axwayServiceBroker;

	@MockBean
	private CloudFoundryClient cloudFoundryClient;


	@MockBean
	private CFClient cfClient;
	
	@MockBean
	private Util util;

	@Autowired
	WebTestClient webTestClient;



	private ObjectMapper objectMapper = new ObjectMapper();

	private String service_id = "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";
	private String plan_id = "1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F";

	private String instance_id = "5a76d1c5-4bc3-455a-98b1-e3c079dc5cb2";
	private String binding_id = "7ed4c3d3-c3a4-41b6-9acc-72b3a7fa2f39";




	@Test
	public void shouldCreateServiceBinding() throws Exception {

		//webTestClient = webTestClient.mutateWith(csrf());

		Map<String, Object> parameters = new HashMap<>();

		parameters.put("apiname", "pcftest");
		parameters.put("uri", "http://petstore.swagger.io/v2/swagger.json");


		Context context = CloudFoundryContext.builder().organizationGuid("dff68133-725d-4b50-9c94-670c7bc5ee7d")
				.spaceGuid("dea89260-6f9f-40ad-a5ec-dffa48692c18").build();

		System.out.println(context.getProperties());

				//new CloudFoundryContext("dff68133-725d-4b50-9c94-670c7bc5ee7d", "dea89260-6f9f-40ad-a5ec-dffa48692c18");

		Map<String, Object> bind_resource = new HashMap<>();

		BindResource bindResource = BindResource.builder().appGuid(UUID.randomUUID().toString()).route("testapp.axway.com").properties(bind_resource).build();



		CreateServiceInstanceBindingRequest createServiceInstanceBindingRequest = CreateServiceInstanceBindingRequest
				.builder().planId(plan_id).bindingId(binding_id).bindResource(bindResource)
				.serviceDefinitionId(service_id)
				.context(context)
				.parameters(parameters).build();

		//createServiceInstanceBindingRequest.getContext().getProperties().remove("platform");

	//	System.out.println();

//				new CreateServiceInstanceBindingRequest(
//				service_id, plan_id, bindResource, context, parameters);

		String request = objectMapper.writeValueAsString(createServiceInstanceBindingRequest);
		DocumentContext documentContext = JsonPath.parse(request);
		documentContext.delete("$.context.platform");
		documentContext.put("$.context", "platform", "cloudfoundry");
		webTestClient.put()
				.uri("/v2/service_instances/{instance_id}/service_bindings/{binding_id}", instance_id, binding_id)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				//.header("CSRF", "test_csrf_token")
				//.header("Authorization", "Basic ", Base64.getEncoder().encodeToString((username + ":" +password).getBytes()))
				.body(Mono.just(documentContext.jsonString()), String.class)
				.header("X-Broker-API-Originating-Identity", getCfUserId())
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()

				.jsonPath("$.route_service_url").exists();

	}



//	private WebTestClientConfigurer csrf() {
//	}

	protected String getCfUserId() throws JsonProcessingException {
		Map<String, Object> propMap = new HashMap<>();
		propMap.put("user_id", "rnatarajan@axway.com");
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
		String properties = mapper.writeValueAsString(propMap);
		String encodedProperties = new String(Base64Utils.encode(properties.getBytes()));
		String userId = "cloudfoundry "+encodedProperties;
		return userId;
	}

	@Test
	public void shouldDeleteServiceBinding() throws Exception {

		webTestClient.delete().uri("/v2/service_instances/{instance_id}/service_bindings/{binding_id}", instance_id, binding_id)
				.attribute("service_id", service_id)
				.attribute("plan_id", plan_id)
				.header("X-Broker-API-Version", "2.13")
				.exchange()
				.expectStatus().is4xxClientError();

	}

}
