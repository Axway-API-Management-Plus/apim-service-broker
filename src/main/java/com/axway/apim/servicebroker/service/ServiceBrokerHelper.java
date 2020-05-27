package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.exception.ServiceBrokerException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.Map;

public class ServiceBrokerHelper {

    private static final int ORIGINATING_IDENTITY_HEADER_PARTS = 2;
    private static final String ORIGINATING_IDENTITY_HEADER = "X-Broker-API-Originating-Identity";

    public String parseIdentity(String originatingIdentityString){
        String[] parts = splitOriginatingIdentityHeaderParts(originatingIdentityString);
        String encodedProperties = decodeOriginatingIdentityHeader(parts[1]);
        Map<String, Object> properties = parseOriginatingIdentityHeader(encodedProperties);
        return (String) properties.get("user_id");
    }

    private String[] splitOriginatingIdentityHeaderParts(String header) {
        String[] parts = header.split(" ", ORIGINATING_IDENTITY_HEADER_PARTS);
        if (parts.length != ORIGINATING_IDENTITY_HEADER_PARTS) {
            throw new ServiceBrokerException("Expected platform and properties values in "
                    + ORIGINATING_IDENTITY_HEADER + " header in request");
        }
        return parts;
    }

    private String decodeOriginatingIdentityHeader(String encodedProperties) {
        try {
            return new String(Base64Utils.decode(encodedProperties.getBytes()));
        }
        catch (IllegalArgumentException e) {
            throw new ServiceBrokerException("Error decoding JSON properties from "
                    + ORIGINATING_IDENTITY_HEADER + " header in request", e);
        }
    }

    private Map<String, Object> parseOriginatingIdentityHeader(String encodedProperties) {
        try {
            return readJsonFromString(encodedProperties);
        }
        catch (IOException e) {
            throw new ServiceBrokerException("Error parsing JSON properties from "
                    + ORIGINATING_IDENTITY_HEADER + " header in request", e);
        }
    }

    private Map<String, Object> readJsonFromString(String value) throws IOException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        return objectMapper.readValue(value, new TypeReference<Map<String, Object>>() {});

    }

}
