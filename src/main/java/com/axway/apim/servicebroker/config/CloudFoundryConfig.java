package com.axway.apim.servicebroker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.BrokerApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2ClientContext;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;
//import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
//import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

@Configuration
public class CloudFoundryConfig {

    private String TOKEN_URI = "/oauth/token";

    //private OAuth2ClientContext oauth2ClientContext = new DefaultOAuth2ClientContext();

    @Value("${cf_admin_username:admin}")
    private String username;

    @Value("${cf_admin_password:changeme}")
    private char[] password;

    @Value("${login_host:https://login.sys.pie-25.cfplatformeng.com}")
    private String accessTokenURI;



//	@Bean
//	public OAuth2ProtectedResourceDetails cf() {
//		ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
//		details.setUsername(username);
//		details.setPassword(new String(password));
//		details.setGrantType("password");
//		accessTokenURI = accessTokenURI + TOKEN_URI;
//		details.setAccessTokenUri(accessTokenURI);
//		return details;
//	}
//
//	@Bean
//	OAuth2RestTemplate cfOauthRestTemplate(){
//
//		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(cf(),oauth2ClientContext);
//		oAuth2RestTemplate.setAccessTokenProvider(new CFResourceOwnerPasswordAccessTokenProvider());
//		oAuth2RestTemplate.setErrorHandler(new AxwayAPIGatewayErrorHandler());
//		return oAuth2RestTemplate;
//	}


    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .build();
    }

//	@Bean
//	OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
//														  OAuth2AuthorizedClientRepository authorizedClientRepository) {
//		OAuth2AuthorizedClientProvider authorizedClientProvider =
//				OAuth2AuthorizedClientProviderBuilder.builder()
//						.refreshToken()
//						.password()
//						.build();
//		DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
//				clientRegistrationRepository, authorizedClientRepository);
//		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//		// For the `password` grant, the `username` and `password` are supplied via request parameters,
//		// so map it to `OAuth2AuthorizationContext.getAttributes()`.
//		authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());
//		return authorizedClientManager;
//	}

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager() {
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("pcf").
                tokenUri(accessTokenURI + TOKEN_URI).clientAuthenticationMethod(ClientAuthenticationMethod.BASIC).
                authorizationGrantType(AuthorizationGrantType.PASSWORD).clientId("abcd").clientSecret("abcd").

                build();


        ClientRegistrationRepository clients = new InMemoryClientRegistrationRepository(clientRegistration);
        OAuth2AuthorizedClientService service = new InMemoryOAuth2AuthorizedClientService(clients);
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager = new
                AuthorizedClientServiceOAuth2AuthorizedClientManager(clients, service);
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .refreshToken()
                        .password()
                        .build();
        manager.setAuthorizedClientProvider(authorizedClientProvider);
        manager.setContextAttributesMapper(contextAttributesMapper());
        return manager;
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
        return authorizeRequest -> {
            Map<String, Object> contextAttributes = Collections.emptyMap();
            contextAttributes = new HashMap<>();

            // `PasswordOAuth2AuthorizedClientProvider` requires both attributes
            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);

            return contextAttributes;
        };
    }

//	private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
//		return authorizeRequest -> {
//			Map<String, Object> contextAttributes = Collections.emptyMap();
//			HttpServletRequest servletRequest = authorizeRequest.getAttribute(HttpServletRequest.class.getName());
//			String username = servletRequest.getParameter(OAuth2ParameterNames.USERNAME);
//			String password = servletRequest.getParameter(OAuth2ParameterNames.PASSWORD);
//			if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
//				contextAttributes = new HashMap<>();
//				// `PasswordOAuth2AuthorizedClientProvider` requires both attributes
//				contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
//				contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
//			}
//			return contextAttributes;
//		};
//	}

}
