package com.axway.apim.servicebroker.util;

import com.axway.apim.servicebroker.exception.AxwayException;
import com.axway.apim.servicebroker.exception.ServiceBrokerException;
import org.apache.commons.validator.routines.EmailValidator;
//import org.springframework.cloud.servicebroker.exception.ServiceBrokerInvalidParametersException;

public class Util {


    public static void isValidEmail(String email) {

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ServiceBrokerException("Username should be a valid email address");
        }
    }

    public static  String getNameFromEmail(String email) {

        if (EmailValidator.getInstance().isValid(email)) {
            return email.split("@")[0];
        }
        return null;
    }


}
