package com.axway.apim.servicebroker.service;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.axway.apim.servicebroker.BaseClass;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class CF extends BaseClass {

	// @Autowired
	// ReactorCloudFoundryClient cloudFoundryClient;
	//
	// @Autowired
	// UaaClient uaaClient;
	//
//	@Autowired
//	@Qualifier("cfOauthRestTemplate")
//	OAuth2RestTemplate cfOauthRestTemplate;

	/*
	 * @Test public void testOrg() { CloudFoundryOperations
	 * cloudFoundryOperations = DefaultCloudFoundryOperations.builder()
	 * .cloudFoundryClient(cloudFoundryClient).build(); String orgGuid =
	 * "dff68133-725d-4b50-9c94-670c7bc5ee7d";
	 * 
	 * 
	 * 
	 * OrganizationSummary orgSummary =
	 * cloudFoundryOperations.organizations().list() .filter(organizationSummary
	 * -> orgGuid.equals(organizationSummary.getId()))
	 * .blockFirst(Duration.ofSeconds(20));
	 * 
	 * String organizationName = orgSummary.getName();
	 * 
	 * String spaceGuid = "dea89260-6f9f-40ad-a5ec-dffa48692c18";
	 * 
	 * cloudFoundryOperations =
	 * DefaultCloudFoundryOperations.builder().organization(organizationName)
	 * .cloudFoundryClient(cloudFoundryClient).build();
	 * 
	 * SpaceSummary space = cloudFoundryOperations.spaces().list()
	 * .filter(spaceSummary ->
	 * spaceGuid.equals(spaceSummary.getId())).blockFirst(Duration.ofSeconds(20)
	 * );
	 * 
	 * String spaceName = space.getName();
	 * 
	 * System.out.println("Space name:" + space.getName());
	 * 
	 * 
	 * String id = "bf9fa74d-1857-42bd-9518-6bb80410f7cf"; ListUsersRequest r =
	 * ListUsersRequest.builder().filter(String.format("id eq \"%s\"",
	 * id)).build();
	 * 
	 * uaaClient.users().list(r).flatMapIterable(ListUsersResponse::getResources
	 * ).map(User::getUserName).blockLast();
	 * 
	 * 
	 * }
	 */

//	@Test
//	public void getOrg() {
//
//		System.out.println(cfOauthRestTemplate.getAccessToken().getValue());
//		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(
//				"https://api.sys.pie-25.cfplatformeng.com/v2/organizations/dff68133-725d-4b50-9c94-670c7bc5ee7d",
//				String.class);
//		System.out.println(response.getBody());
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		String orgName = documentContext.read("$.entity.name", String.class);
//		String spaceURL = documentContext.read("$.entity.spaces_url", String.class);
//		System.out.println(orgName);
//
//	}
//
//	@Test
//	public void getSpace() {
//
//		String spaceGuid = "dea89260-6f9f-40ad-a5ec-dffa48692c18";
//		System.out.println(cfOauthRestTemplate.getAccessToken().getValue());
//		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(
//				"https://api.sys.pie-25.cfplatformeng.com/v2/spaces/" + spaceGuid , String.class);
//		System.out.println(response.getBody());
//		DocumentContext documentContext = JsonPath.parse(response.getBody());
//		String spaceName = documentContext.read("$.entity.name", String.class);
//		System.out.println(spaceName);
//
//	}
//
//	@Test
//	public void getUser() {
//
//		System.out.println(cfOauthRestTemplate.getAccessToken().getValue());
//		ResponseEntity<String> response = cfOauthRestTemplate.getForEntity(
//				"https://api.sys.pie-25.cfplatformeng.com/v2/users/d466d8f8-c2ad-46c1-b195-d342d9159889",
//				String.class);
//		 DocumentContext documentContext = JsonPath.parse(response.getBody());
//		 String userName = documentContext.read("$.entity.username", String.class);
//		 System.out.println(userName);
//}

}
