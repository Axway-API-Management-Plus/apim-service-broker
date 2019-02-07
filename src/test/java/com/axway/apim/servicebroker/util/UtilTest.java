package com.axway.apim.servicebroker.util;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

public class UtilTest {

	@Test
	public void testIsValidEmail() {
		try {
			Util util = new Util();
			util.isValidEmail("rnatarjan@axway.com");
		} catch (ServiceBrokerInvalidParametersException e) {
			fail("Invalid emai");
		}
	}
}
