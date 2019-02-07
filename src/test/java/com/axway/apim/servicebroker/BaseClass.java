package com.axway.apim.servicebroker;

import java.util.HashMap;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BaseClass {

	@Value("${security.user.name}")
	protected String username;
	@Value("${security.user.password}")
	protected String password;
	
	protected String getCfUserId() throws JsonProcessingException {
		Map<String, Object> propMap = new HashMap<>();
		propMap.put("user_id", "rnatarajan@axway.com");
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
		String properties = mapper.writeValueAsString(propMap);
		String encodedProperties = new String(Base64Utils.encode(properties.getBytes()));
		String userId = "cloudfoundry "+encodedProperties;
		return userId;
	}
}
