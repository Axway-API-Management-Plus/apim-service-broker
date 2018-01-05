# Description
- Axway Service Broker for Pivotal Cloud Foundry based on [Fully brokered Architecture](https://docs.pivotal.io/pivotalcf/1-12/services/route-services.html#fully-brokered)


## API Management Version Compatibilty
This artefact was successfully tested for the following versions:
- Axway AMPLIFY API Management 7.5.3


## Prerequisites

- Axway AMPLIFY API Management 7.5.3
- JDK 1.8.0_xxx
- Apache Maven 3.3.9
- Pivotal Cloud Foundry Elastic Runtime  version 1.12 
- CF Cli version 6.32.0+0191c33d9.2017-09-26 or above

## Axway Service Broker  Installation

- Publish Server broker Application

	Check out the code from github
	```bash
	$git clone https://github.com/axway-apim-service-broker.git
	```
	Build the project (output from `cf push` command provides fully qualified URL as output)
	```bash
	$mvn clean install
	$cf push 
	
	Showing health and status for app axway-apim-service-broker in org axwaydev / space dev as admin...
	OK
	
	requested state: started
	instances: 1/1
	usage: 1G x 1 instances
	urls: axway-apim-service-broker-nocuous-ovoviviparousness.cfapps.pie-25.cfplatformeng.com
	last uploaded: Wed Jan 3 21:35:47 UTC 2018
	stack: cflinuxfs2
	buildpack: client-certificate-mapper=1.2.0_RELEASE container-security-provider=1.8.0_RELEASE java-buildpack=v4.5-offline-https://github.com/cloudfoundry/java-buildpack.git#ffeefb9 java-main java-opts jvmkill-agent=1.10.0_RELEASE open-jdk-like-jre=1.8.0_1...
	```

- Create Service Broker 

	```bash
	$cf create-service-broker axway-apim-service-broker admin changeme http://axway-apim-service-broker-nocuous-ovoviviparousness.cfapps.pie-25.cfplatformeng.com --space-scoped
	
	Getting services from marketplace in org axwaydev / space dev as admin...
	OK
	```

- Check whether the service broker is added in Pivotal market place

	```bash
	$cf marketplace
	
	Getting services from marketplace in org axwaydev / space dev as admin...
	OK
	
	service                       plans                     description
	Axway-APIM                    APIM                      Axway service broker implementation
	app-autoscaler                standard                  Scales bound applications in response to load
	p-circuit-breaker-dashboard   standard                  Circuit Breaker Dashboard for Spring Cloud Applications
	p-config-server               standard                  Config Server for Spring Cloud Applications
	p-mysql                       100mb                     MySQL databases on demand
	p-rabbitmq                    standard                  RabbitMQ is a robust and scalable high-performance multi-protocol messaging broker.
	p-redis                       shared-vm, dedicated-vm   Redis service to provide pre-provisioned instances configured as a datastore, running on a shared or dedicated VM.
	p-service-registry            standard                  Service Registry for Spring Cloud Applications
	p.rabbitmq                    solo                      RabbitMQ Service
	p.redis                       cache-small               Redis service to provide on-demand dedicated instances configured as a cache.
	```
	
- Create Cloud Foundry Service to Bind and Unbind Routes
 
	```bash
    $cf create-service Axway-APIM APIM AxwayAPIM
	Creating service instance AxwayAPIM in org axwaydev / space dev as admin...
	OK
	```

- Add environment variables to Service Broker

	```bash
	$cf set-env axway-apim-service-broker axway_apimanager_url https://phx-107.demo.axway.com:8075
	$cf set-env axway-apim-service-broker axway_apimanager_username apiadmin
	$cf set-env axway-apim-service-broker axway_apimanager_password changeme
	$cf set-env aaxway-apim-service-broker axway_apimanager_traffic_url https://phx-107.demo.axway.com:8065 // In High Availability scenario the URL will be a Load Balancer URL
	```
									
- Refresh Service Broker Instance to read the new environment variable
	
	```bash
    $cf restage axway-service-broker
    ```
    

## Axway Service Broker update

If any changes are made in the code base, publish the changes to Pivotal Elastic run time
```bash
$cf push
```

## Setup API Gateway

1. Upload the following project in Axway Policy Studio
  * The Policy Studio Project (src/main/resources/apiproject) must be checked-out locally and imported into Policy Studio using the option `Open Project`.

2. Deploy Policy Studio project on the target API Gateway



## Test Service Broker

- Publish a application on Pivotal Cloud Foundry runtime
- Add custom attribute in a json file (param.json)
	
    ```json
	{
		"orgName": "Axway",
		"backendName": "pcftest2",
		"swaggerURI": "/v2/api-docs" 
		
	}
	```
	The SwaggerURI value will be context name  or Fully qualified Swagger URL (http://greeting-app2-unwrinkleable-carriole.cfapps.pie-25.cfplatformeng.com/v2/api-docs). If context name is provided as input, the service broker reads the host name from Cloud Foundry Application.
	
- Run  Route binding command

	```bash
	$cf bind-route-service cfapps.pie-25.cfplatformeng.com  AxwayAPIM --hostname greeting-app-tournois-postresurrection -c param.json
	```
	
	The route binding command invokes Axway Serivce broker. The Service broker creates Backend, Front End and apply API Key as security. 
	
- Test the Pivotal Application

- Un-bind Application from Axway Service broker

	```bash
	$cf unbind-route-service cfapps.pie-25.cfplatformeng.com  AxwayAPIM --hostname greeting-app-tournois-postresurrection 
	```
	
	The route unbinding command invokes Axway Service broker and Service broker Does the following. 
		1. If API is in Published state, it will throw an error. 
		2. If API is in un-published state, delete frontend, backend API. 
		
 

## Axway Service Broker  uninstallation
	
```bash
$cf delete-service AxwayAPIM
$cf delete-service-broker axway-apim-service-broker
```

## Contributing
Please read [Contributing.md](https://github.com/Axway-API-Management-Plus/Common/blob/master/Contributing.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Team

![alt text](https://github.com/Axway-API-Management-Plus/Common/blob/master/img/AxwayLogoSmall.png)
Axway Team



## License
[Apache License 2.0](/LICENSE)
