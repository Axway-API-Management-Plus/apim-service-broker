package com.axway.apim.servicebroker.util;

import com.axway.apim.servicebroker.exception.AxwayException;
import org.apache.commons.validator.routines.EmailValidator;
//import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

public class Util {


    public void isValidEmail(String email) throws AxwayException {

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new AxwayException("Username should be a valid email address");
        }
    }

    public String getNameFromEmail(String email) {

        if (EmailValidator.getInstance().isValid(email)) {
            return email.split("@")[0];
        }
        return null;
    }


}
