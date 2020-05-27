package com.axway.apim.servicebroker.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ServiceInstanceBindingController {

    @PutMapping(value = "/v2/service_instances/{instanceId}/service_bindings/{bindingId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public String createServiceInstanceBinding(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @PathVariable("bindingId") String bindingId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity,
            @RequestBody String request){
        return "test";
    }

    @DeleteMapping(value = "/v2/service_instances/{instanceId}/service_bindings/{bindingId}")
    ResponseEntity deleteServiceInstanceBinding(
            @PathVariable Map<String, String> pathVariables,
            @PathVariable("instanceId") String serviceInstanceId,
            @PathVariable("bindingId") String bindingId,
            @RequestParam("service_id") String serviceDefinitionId,
            @RequestParam("plan_id") String planId,
            @RequestParam(value = "accepts_incomplete", required = false) boolean acceptsIncomplete,
            @RequestHeader(value = "X-Api-Info-Location", required = false) String apiInfoLocation,
            @RequestHeader(value = "X-Broker-API-Originating-Identity", required = false) String originatingIdentityString,
            @RequestHeader(value = "X-Broker-API-Request-Identity", required = false) String requestIdentity) {

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
