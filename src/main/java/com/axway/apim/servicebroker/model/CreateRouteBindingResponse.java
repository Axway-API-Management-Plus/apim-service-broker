package com.axway.apim.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRouteBindingResponse {


    @JsonProperty("route_service_url")
    private String dashboardUrl;

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }
}
