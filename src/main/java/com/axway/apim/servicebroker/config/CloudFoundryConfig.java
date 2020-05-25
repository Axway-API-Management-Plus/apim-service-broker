package com.axway.apim.servicebroker.config;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudFoundryConfig {

    @Bean
    DefaultConnectionContext connectionContext(@Value("${api_host:api.cloudfoundry.com}") String apiHost) {
        return DefaultConnectionContext.builder()
                .apiHost(apiHost)
                .build();
    }

    @Bean
    PasswordGrantTokenProvider tokenProvider(@Value("${cf_admin_username:admin}") String username,
                                             @Value("${cf_admin_password:changeme}") String password) {
        return PasswordGrantTokenProvider.builder()
                .password(password)
                .username(username)
                .build();
    }

    @Bean
    CloudFoundryClient cloudFoundryClient(ConnectionContext connectionContext, TokenProvider tokenProvider) {
        return ReactorCloudFoundryClient.builder()
                .connectionContext(connectionContext)
                .tokenProvider(tokenProvider)
                .build();
    }

}
