package com.axway.apim.servicebroker.util;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.exception.ServiceBrokerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
//import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

public class Util {


    public void isValidEmail(String email) {

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ServiceBrokerException("Username should be a valid email address");
        }
    }

    public String getNameFromEmail(String email) {

        if (EmailValidator.getInstance().isValid(email)) {
            return email.split("@")[0];
        }
        return null;
    }

    public void log(Object jsonObj, Logger logger) {

        try {
            String request = Jackson2ObjectMapperBuilder.json().build().writeValueAsString(jsonObj);
            logger.info("Request {}", request);

        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON");
        }
    }


}
