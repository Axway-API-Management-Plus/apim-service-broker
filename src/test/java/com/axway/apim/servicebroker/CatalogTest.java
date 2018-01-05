package com.axway.apim.servicebroker;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class CatalogTest extends BaseClass {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCatalog() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v2/catalog")
				.with(httpBasic(username, password))).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.jsonPath("$.services[0].name", is("Axway-APIM")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.services[0].id", is("ED01F448-40C7-4A9D-93D0-51E7D4E93CA1")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.services[0].description", is("Axway service broker implementation")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.services[0].bindable", is(true)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.services[0].plans[0].name", is("APIM")));
	}

}
