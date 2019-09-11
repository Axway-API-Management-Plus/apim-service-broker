package com.axway.apim.servicebroker.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionController implements Constants {

    @RequestMapping(value = "/version" , method = RequestMethod.GET, produces = { "application/json" })
    public String getVersion() {
        return "{\"version\" :" + Constants.VERSION + "}";
    }
}
