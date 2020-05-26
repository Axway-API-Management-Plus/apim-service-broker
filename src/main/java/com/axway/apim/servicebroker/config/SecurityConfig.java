package com.axway.apim.servicebroker.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .httpBasic().and()
                .authorizeExchange()
                .pathMatchers("/v2/**").authenticated()
                .pathMatchers("/actuator/**", "/version").permitAll()
                .matchers(EndpointRequest.toAnyEndpoint()).hasAuthority("ADMIN")
                .and().build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User.withUsername("admin").password("changeme")..roles("ADMIN").build();
//
//        return new MapReactiveUserDetailsService(user);
//    }
}
