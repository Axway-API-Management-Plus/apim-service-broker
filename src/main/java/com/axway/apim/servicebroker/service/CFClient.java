package com.axway.apim.servicebroker.service;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.users.GetUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CFClient {

	private CloudFoundryClient cloudFoundryClient;

	@Autowired
	public CFClient(CloudFoundryClient cloudFoundryClient){
		this.cloudFoundryClient = cloudFoundryClient;
	}

	public String getUserName(String userGuid) {

		String userName = cloudFoundryClient.users().get(GetUserRequest.builder().userId(userGuid).build()).block()
				.getEntity().getUsername();
		return userName;
	}
}
