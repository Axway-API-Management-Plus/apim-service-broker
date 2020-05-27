package com.axway.apim.servicebroker.service;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.v2.organizations.GetOrganizationRequest;
import org.cloudfoundry.client.v2.spaces.GetSpaceRequest;
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

	public String getSpaceName(String spaceGuid) {


		String spaceName =  cloudFoundryClient.spaces().get(GetSpaceRequest.builder().spaceId(spaceGuid)
				.build()).block().getEntity().getName();
		return spaceName;

	}

	public String getUserName(String userGuid) {


		String userName = cloudFoundryClient.users().get(GetUserRequest.builder().userId(userGuid).build()).block()
				.getEntity().getUsername();
		return userName;

	}

	public String getOrg(String orgGuid) {

		String orgName = cloudFoundryClient.organizations().get(GetOrganizationRequest.builder()
				.organizationId(orgGuid).build()).block().getEntity().getName();
		return orgName;
	}

}
