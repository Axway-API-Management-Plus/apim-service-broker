package com.axway.apim.servicebroker;

import com.axway.apim.servicebroker.service.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AxwayServiceBrokerApplication {
    private static final Logger logger = LoggerFactory.getLogger(AxwayServiceBrokerApplication.class);

    public static void main(String[] args) {
    	logger.info("Version : {}", Constants.VERSION);
        SpringApplication.run(AxwayServiceBrokerApplication.class, args);
    }
}
