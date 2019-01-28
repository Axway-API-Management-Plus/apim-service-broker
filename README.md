[![Build Status](https://travis-ci.org/Axway-API-Management-Plus/apim-service-broker.svg?branch=master)]
# Description
- Axway Service Broker for Pivotal Cloud Foundry based on [Fully brokered Architecture](https://docs.pivotal.io/pivotalcf/1-12/services/route-services.html#fully-brokered)


## API Management Version Compatibilty
This artefact was successfully tested for the following versions:
- Axway AMPLIFY API Management 7.5.3 and 7.6.2


## Prerequisites

- Axway AMPLIFY API Management 7.5.3 or above
- JDK 1.8.0_xxx
- Apache Maven 3.3.9 or above 
- Pivotal Cloud Foundry Elastic Runtime  version 1.12 or above
- CF Cli version 6.32.0+0191c33d9.2017-09-26 or above
- Service Broker version 2.12 or above

## Axway Service Broker  Installation

- Add a custom property to organizations

	Edit the following file:
	```bash
	INSTALL_DIR/apigateway/webapps/apiportal/vordel/apiportal/app/app.config
	```
	Insert the following code fragment marked in bold in the organizations property:
	```bash
	customPropertiesConfig: {
     
     	 organization: {
            service_instance_id:{
                label: 'Service Instance Id'
            }
        }
    }
    ```


- Publish Server broker Application

	Check out the code from github
	```bash
	$git clone https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample.git
	```
	Build the project (output from `cf push` command provides fully qualified URL as output)
	```bash
	$mvn clean install
	```
	
	or you can use the following command if you want to skip testing step:
	```bash
	$mvn clean install -Dmaven.test.skip=true
	```
	Now, you can push your app to PCF:
	```bash
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
	
	Note: In the `create-service-broker` command, use an app URL shown in the output from the `cf push` command

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
	
- Add environment variables to Service Broker

	```bash
	$cf set-env axway-apim-service-broker axway_apimanager_url https://myAPIM.server.com:8075
	$cf set-env axway-apim-service-broker axway_apimanager_username apiadmin
	$cf set-env axway-apim-service-broker axway_apimanager_password changeme
	$cf set-env axway-apim-service-broker axway_apimanager_traffic_url https://myAPIM.server.com:8065 // In High Availability scenario the URL will be a Load Balancer URL
	
	$cf set-env axway-apim-service-broker cf_admin_username admin@axway.com
	$cf set-env axway-apim-service-broker cf_admin_password changme
	$cf set-env axway-apim-service-broker login_host https://login.sys.pie-25.cfplatformeng.com/oauth/token
	$cf set-env axway-apim-service-broker cc_host https://api.sys.pie-25.cfplatformeng.com
	
	$cf set-env axway-apim-service-broker TRUST_CERTS login.sys.pie-25.cfplatformeng.com,api.sys.pie-25.cfplatformeng.com  //If your PCF instance uses self-signed certs, you may need to use this environment variable to prevent some security errors
	```

- Refresh Service Broker Instance to read the new environment variable
	
	```bash
    $cf restage axway-apim-service-broker
    ```						
    
- Create Cloud Foundry Service to Bind and Unbind Routes
 
	```bash
    $cf create-service Axway-APIM APIM-Free AxwayAPIM
	Creating service instance AxwayAPIM in org axwaydev / space dev as admin...
	OK
	```
Create service command does the following

1. Fetch space name from Cloud controller and use it as organization name
2. Create a new organization
3. Fetch Cloud foundry login  email id by calling Cloud Controller. 
4. Create a new User
5. Reset password for the newly created user which triggers an email.   



    

## Axway Service Broker update

If any changes are made in the code base, publish the changes to Pivotal Elastic run time
```bash
$cf push
```

## Setup API Gateway

1. Upload the following project in Axway Policy Studio
  * The Policy Studio Project (src/main/resources/apiproject) must be checked-out locally and imported into Policy Studio using the option `Open Project`.

2. Deploy this project to your instance of Axway API Gateway. You may also export the `Forward Request to API Manger Traffic Port` policy from the project and import it in your API Gateway configuration. In addition, you will need to configure Gateway Listeners similar to how it is done in the provided project: port 8065 is mapped to the `PCF` listener, port 7070 is mapped to the `API Manager Traffic` listener.



## Test Service Broker

- Publish a target PCF application on Pivotal Cloud Foundry runtime
- Add custom attribute in a json file (param.json)
	
    ```json
	{
		"apiname": "pcftest",
		"type":": "swagger", 
		"uri": "/v2/api-docs" 
	}
	
	```
	`apiName` is optional. If `apiName` is not specified, Service broker fetch the API Name and from swagger or WSDL.
	Possible values of type paramters are `wsdl` and `swagger` 
	The URI value will be context name  or Fully qualified Swagger / WSDL URL (http://greeting-app2-unwrinkleable-carriole.cfapps.pie-25.cfplatformeng.com/v2/api-docs). If context name is provided as input, the service broker reads the host name from Cloud Foundry Application.
	
- Run  Route binding command

	```bash
	$cf bind-route-service cfapps.pie-25.cfplatformeng.com  AxwayAPIM --hostname greeting-app-tournois-postresurrection -c param.json
	```
	
	The route binding command invokes Axway Service broker. The Service broker creates Backend, Front-End API using Pass-Through as inbound security. 
	
- Test the Pivotal Application

    Try to access your apps endpoint. To verify that the request goes through API Gateway, open Axway API Gateway Manager and look at the traffic tab. You should see two entries:
    - One that comes from GoRouter (PCF)
    - The second one comes as a riderect from API Gateway itself

- Un-bind Application from Axway Service broker

```bash
$cf unbind-route-service cfapps.pie-25.cfplatformeng.com  AxwayAPIM --hostname greeting-app-tournois-postresurrection 
```

The route unbinding command invokes Axway Service Broker and Service Broker does the following:
1. If API is in Published state, it will throw an error.
2. If API is in un-published state, delete frontend, backend API.


## Axway Service Broker  uninstallation
	
```bash
$cf delete-service AxwayAPIM
```

The delete service command does the following:
1. If the service has binded application or routes, it throws an error.  
2. Delete Frontend and Backend APIs
3. Delete applications
4. Delete User
5. Delete Organization 

```bash
$cf delete-service-broker axway-apim-service-broker
```

## Contributing
Please read [Contributing.md](https://github.com/Axway-API-Management-Plus/Common/blob/master/Contributing.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Team

![alt text](https://github.com/Axway-API-Management-Plus/Common/blob/master/img/AxwayLogoSmall.png)
Axway Team



## License
[Apache License 2.0](/LICENSE)
