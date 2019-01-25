package com.axway.apim.servicebroker;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BaseClass {

	@Value("${spring.security.user.name}")
	protected String username;
	@Value("${spring.security.user.password}")
	protected String password;
}
