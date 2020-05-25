package com.axway.apim.servicebroker.service;

import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@WithMockUser
public class CatalogTest  {


	@Value("${spring.security.user.name}")
	protected String username;
	@Value("${spring.security.user.password}")
	protected String password;

	@MockBean
	private CFClient cfClient;


	@MockBean
	private CloudFoundryClient cloudFoundryClient;


	@Autowired
	WebTestClient webTestClient;


	@Test
	public void testCatalog() throws Exception {

			webTestClient.get().uri("/v2/catalog").accept(MediaType.APPLICATION_JSON).header("Authorization", "Basic ", Base64.getEncoder().encodeToString((username + ":" +password).getBytes()))
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.services[0].name").isEqualTo("Axway-APIM")
				.jsonPath("$.services[0].id").isEqualTo("ED01F448-40C7-4A9D-93D0-51E7D4E93CA1")
				.jsonPath("$.services[0].description").isEqualTo("Axway service broker implementation")
				.jsonPath("$.services[0].bindable").isEqualTo("true")
				.jsonPath("$.services[0].plans[0].name").isEqualTo("APIM-Free");


	}



}
