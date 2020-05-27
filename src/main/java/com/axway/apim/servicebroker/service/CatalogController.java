package com.axway.apim.servicebroker.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {

    private String catalog;

    public CatalogController(String catalog){
        this.catalog = catalog;
    }

    @GetMapping(value = "/v2/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCatalog(){
        return catalog;
    }
}
