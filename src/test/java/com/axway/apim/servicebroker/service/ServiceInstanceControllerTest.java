package com.axway.apim.servicebroker.service;

import com.axway.apim.servicebroker.BaseClass;
import com.axway.apim.servicebroker.util.Util;
import org.cloudfoundry.client.CloudFoundryClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public class ServiceInstanceControllerTest extends BaseClass {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AxwayServiceBroker axwayServiceBroker;

    @MockBean
    private CFClient cfClient;

    @MockBean
    Util util;

    @MockBean
    private CloudFoundryClient cloudFoundryClient;

    @MockBean
    ServiceBrokerHelper serviceBrokerHelper;

    private String instanceId = "5a76d1c5-4bc3-455a-98b1-e3c079dc5cb2";
    private String service_id = "ED01F448-40C7-4A9D-93D0-51E7D4E93CA1";
    private String plan_id = "1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F";

    @Test
    public void testCreateService() throws Exception{
        String request = "{\"context\":{\"platform\":\"cloudfoundry\",\"platform\":\"cloudfoundry\",\"organization_guid\":\"2b397586-f831-40dd-b261-8a6239b4f999\",\"organization_name\":\"axway\",\"space_guid\":\"7376cb70-e5a4-4c47-af04-1edfdf72ead7\",\"space_name\":\"dev\",\"instance_name\":\"AxwayAPIM\"},\"organization_guid\":\"2b397586-f831-40dd-b261-8a6239b4f999\",\"space_guid\":\"7376cb70-e5a4-4c47-af04-1edfdf72ead7\",\"service_id\":\"ED01F448-40C7-4A9D-93D0-51E7D4E93CA1\",\"plan_id\":\"1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .put("/v2/service_instances/{instanceId}", instanceId)
                .with(httpBasic(username, password)).contentType(MediaType.APPLICATION_JSON).content(request)
                .header("X-Broker-API-Originating-Identity", getCfUserId()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

    @Test
    public void testDeleteService() throws Exception{
        String request = "{\"context\":{\"platform\":\"cloudfoundry\",\"platform\":\"cloudfoundry\",\"organization_guid\":\"2b397586-f831-40dd-b261-8a6239b4f999\",\"organization_name\":\"axway\",\"space_guid\":\"7376cb70-e5a4-4c47-af04-1edfdf72ead7\",\"space_name\":\"dev\",\"instance_name\":\"AxwayAPIM\"},\"organization_guid\":\"2b397586-f831-40dd-b261-8a6239b4f999\",\"space_guid\":\"7376cb70-e5a4-4c47-af04-1edfdf72ead7\",\"service_id\":\"ED01F448-40C7-4A9D-93D0-51E7D4E93CA1\",\"plan_id\":\"1A6C15A6-1DE1-4870-A4F2-EA0A905F4A0F\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/v2/service_instances/{instanceId}", instanceId)
                .param("service_id",service_id)
                .param("plan_id",plan_id)
                .header("X-Broker-API-Originating-Identity", getCfUserId())
                .header("X-Broker-API-Version", "2.15")
                .with(httpBasic(username, password)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}
