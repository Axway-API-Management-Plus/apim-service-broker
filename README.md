# Description
- Sample for Axway Service Broker for Cloud Foundry
- The goal of this sample is to quickly build a proof of concept to create and bind services in Axway API Management Solution for Applications hosted in Cloud Foundry. 
- It is not a production ready solution. 
- This sample can be customized by the customer to fit their needs regarding Cloud Foundry integration and enhanced to reach production readiness.


![alt text][Screenshot1]

[Screenshot1]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Readme/Screenshot01.png "Screenshot1"

The Axway Service Broker is developed in API Gateway/Manager using native functionalities provided by those products.

This version of the Axway Service Broker implements the following APIs:

![alt text][Screenshot2]

[Screenshot2]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Readme/Screenshot02.png "Screenshot2"


In the previous diagram:
- API Manager-1 and API Gateway represent the Axway Service Broker
- API-Manager-2 represents the Service Back-End

The 5 Axway Service Broker APIs are exposed by API Manager-1 (/Catalog (Get), /Provisioning (Put and Delete), Binding (Put and Delete)). 
These 5 APIs virtualize 5 APIs provided by API Gateway which orchestrate APIs calls to manage API Manager-2 objects.


## API Management Version Compatibilty
This artefact was successfully tested for the following versions:
- 7.5.3


## Install
**Prerequisites**
```
API Gateway/API Manager and Policy Studio must be installed
```

**Axway Service Broker sample installation**
```
1. API Manager APIs upload in API Manager
- The swagger file name is apigateway/samples/swagger/api-manager-V_1_3-swagger.json. 
- Connect to API Manager using an account with an “API Admin” profile
- Use the Import Swagger file option to create the Back-End API and select the swagger file defined previously
- Rename the API “APIManagerAPI” for example to get a more striking name
- Create the Front-End API with the “New API from Back End API” option

2. Policy Studio project upload in Policy Studio
- The “CloudFoundryServiceBroker” Policy Studio Project must be checked-out locally and imported into Policy Studio using the function “Open Project”.

3. Policy Studio project updates in Policy Studio
- Update Server Settings 
- Update Certificates
- Update Environment settings
- Update listeners
accordingly to your environment

4. Copy locally files ServiceList.json and OrganizationList.json file and update the datamap "ServiceListFromOrganizationList" with new paths

5. Deploy Policy Studio project on the target API Gateway

6. Define API in API Manager 
- Create the Back-End API with "import API from Topology" option. 
- Create the Front-End API using the previously created Back-End API
```

## Tests
- Scripts stored in the Tests directory can be used to simulate Cloud Controller calls
- There is one script per ServiceBroker API
- Refer to [Tests.md](https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Tests/Tests.md) for more information  
- The [swagger file](https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Swagger/ServiceBrokerAPISwagger.json) can also be used to define test cases 
   
## Additional documentation on API Gateway policies
Detailed documentation on ServiceBroker Policies is available here [Policy Documentation](https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation.md)  

## Bug and Caveats
To be implemented: 
- Cloud Controller (final release v145+) authenticates with the Broker using HTTP basic authentication (the Authorization: header) on every request and will reject any broker registrations that do not contain a username and password. The broker is responsible for checking the username and password and returning a 401 Unauthorized message if credentials are invalid. 

## Contributing
Please read [Contributing.md](https://github.com/Axway-API-Management-Plus/Common/blob/master/Contributing.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Team

![alt text][Axwaylogo] Axway Team

[Axwaylogo]: https://github.com/Axway-API-Management-Plus/Common/blob/master/img/AxwayLogoSmall.png  "Axway logo"


## License
[Apache License 2.0](/LICENSE)

