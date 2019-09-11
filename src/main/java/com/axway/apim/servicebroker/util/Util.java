package com.axway.apim.servicebroker.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

public class Util {


    public void isValidEmail(String email) throws ServiceBrokerInvalidParametersException {

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ServiceBrokerInvalidParametersException("Username should be a valid email address");
        }
    }

    public String getNameFromEmail(String email) {

        if (EmailValidator.getInstance().isValid(email)) {
            return email.split("@")[0];
        }
        return null;
    }


}
