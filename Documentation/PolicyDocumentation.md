# Policy documentation

## Overview

The following REST API are set in API Gateway. Each API is based on a "master" Policy of the same name

![alt text][Screenshot0]

[Screenshot0]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation00.png "Screenshot0"


## Focus on Catalog API
**Policy configuration**

![alt text][Screenshot1]

[Screenshot1]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation01.png "Screenshot1"


**Specific steps description**

The first step is used to set some environment variables. All those variables are set in the environment settings configuration part

 
![alt text][Screenshot2]

[Screenshot2]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation02.png "Screenshot2"
 
A mapping step is called at the end of the policy to map the message body returned by the API Manager API /organization to the format expected by the Cloud Controller:
 
 
![alt text][Screenshot3]

[Screenshot3]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation03.png "Screenshot3"


## Focus on Provisioning API
**Policy configuration**

 
![alt text][Screenshot4]

[Screenshot4]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation04.png "Screenshot4"


**Specific steps description**

- Accept incomplete
Some Service Broker can accept request for asynchronous provisioning. In that case, the request will include “?accepts_incomplete=true”. As it is not the case for Axway Service Broker, an error must be sent back for call including this option with a response body { "error": "AsyncRequired", "description": "This service plan requires client support for asynchronous service operations." }.

 
## Focus on Binding
**Policy configuration**
 
![alt text][Screenshot5]

[Screenshot5]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation05.png "Screenshot5"

**Specific steps description**
No specific step in this policy

## Focus on Unbinding
**Policy configuration**

 
![alt text][Screenshot6]

[Screenshot6]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation06.png "Screenshot6"

**Specific steps description**

No specific step in this policy

 
## Focus on Deprovisioning
**Policy configuration**

 
![alt text][Screenshot7]

[Screenshot7]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation07.png "Screenshot7"

**Specific steps description**

No specific step in this policy


## Focus on Error management
**Policy configuration**
 
![alt text][Screenshot8]

[Screenshot8]: https://github.com/Axway-API-Management-Plus/Cloud-Foundry-Service-Broker-Sample/blob/master/Documentation/PolicyDocumentation08.png "Screenshot8"


To return the right information to the Cloud Foundry Cloud Controller, a specific error label management is set.
At each step, a specific error label is defined. This error label is built with:
•	The name of the step
•	Information provided by the API Called to perform the step
•	Additional context information 

