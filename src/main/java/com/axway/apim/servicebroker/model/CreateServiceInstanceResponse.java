package com.axway.apim.servicebroker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateServiceInstanceResponse {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String dashboardUrl;

    @JsonIgnore //not sent on the wire as json payload, but as http status instead
    private boolean instanceExisted;

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public void setInstanceExisted(boolean instanceExisted) {
        this.instanceExisted = instanceExisted;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public boolean isInstanceExisted() {
        return instanceExisted;
    }
}
