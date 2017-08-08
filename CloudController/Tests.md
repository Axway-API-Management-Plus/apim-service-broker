# Service Broker Tests

Tests can be performed using:
- Curl samples described in each API definition exposed in API Manager. Shell scripts below are provided to test the Service Broker Sample
- Other tools such as Soap UI (https://www.soapui.org/) and Postman (https://www.getpostman.com/).


## For marketplace
The shell script “cf_marketplace.sh” can be used to simulate the Cloud Controller

**Setup:**
The following variables must be set:
```
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
```

**Usage:** 
```
> cf_marketplace.sh
```

**Result:**
```
All organizations available in API Manager are displayed: 
[{"name":"FastCars","id":"4e2490b2-b495-4599-b26e-03b26b9aa25b","description":"Car Manufacturer"},{"name":"Plexus Health","id":"cefb8d78-25f3-4750-bdad-d7352e7d9268","description":"Heathcare provider\nThis organization includes 3 teams related to API:\n1. Internal development team responsible for the Plexus Suite (physician applications)\n2. Security developers who provide authentification, compliance and control of APIs for internal APIs access\n3. HL7 providers who provides the comprehensive framework and related standards for the exchange, integration, sharing, and retrieval of electronic health information that supports clinical practice and the management, delivery and evaluation of health services"},{"name":"API Development","id":"14df2c8b-c28c-4062-ba8b-31449524a611","description":"API Development team"},{"name":"HealthMob","id":"fb452ab9-dba6-4bfe-84e7-40effafd6804","description":"Partner developing the Patient Care mobile app"},{"name":"CareConnex","id":"4df1ce67-1bf3-46ab-bf3f-84374b249238","description":"Partner providing smart health devices and wearable applications"},{"name":"Partners","id":"59b3dba9-579a-4865-b558-11d5e8b45c08"},{"name":"OrganizationFHU","id":"43a84b0e-daa1-4af5-a8c9-530d0c2a4031"},{"name":"FHIR","id":"53437f82-8db5-4f61-9f9b-0b34db8be847","description":"FHIR Focused Healtcare Providers"},{"name":"Health Center","id":"28586e9f-4f1f-4cea-8cf5-e4860780dbad","description":"Subcontractor developing the Plexus Health Center web app"},{"name":"ORGA02","id":"fbaf9fad-e54e-421a-a78d-a4f55070f5ae"},{"name":"Axway","id":"9aef95ec-8167-46ab-8270-19cf1582c03f"},{"name":"Community","id":"532f6ba6-243b-4ade-a093-7fa874adb287","description":"Unverified untrusted developer users. Not intended for production-level client applications"}]
```

## For provisioning
The shell script “cf_create-service.sh” can be used to simulate the Cloud Controller
**Setup:**
The following variables must be set:
```
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
export AdminName=orgaadmin
export AdminEmail=orgaadmin@demo.axway.com
```

**Usage:** 
```
> cf_create-service.sh <OrganizationName>
```

**Result:**
```
- An organization <OrganizationName> created in API Manager
- An organization Administrator <AdminName> is created in API Manager
- An email is sent to Organization Administrator to update his password (at the address <AdminEmail>)
```

## For binding
The shell script “cf_bind-service.sh” can be used to simulate the Cloud Controller
**Setup:**
The following variables must be set:
```
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
export SwaggerURL=http://petstore.swagger.io/v2/swagger.json
```
**Usage:** 
```
> cf_bind-service.sh <OrganizationName> <WebServiceName>
```

**Result:**
```
- A BackEnd API is created based on the swagger file located at the URL <SwaggerURL> with the name BIC_<WebServiceName> in the organization <OrganizationName>
- A FrontEnd API is created with with the name FE_BIC_<WebServiceName>
- The Inbound Security profile is set to Pass Through
- The FrontEnd API is published
- An Application is created with the name FE_BIC_<WebServiceName>_App
```

## For unbinding
The shell script “cf_unbind-service.sh” can be used to simulate the Cloud Controller
**Setup:**
The following variables must be set:
```
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
```

**Usage:** 
```
> cf_bind-service.sh <OrganizationName> <WebServiceName>
```

**Result:**
```
- The Application FE_BIC_<WebServiceName>_App is deleted
- The FrontEnd API FE_BIC_<WebServiceName> is unpublished and deleted
- The BackEnd API BIC_<WebServiceName> is deleted
```

## For deprovisioning
The shell script “cf_delete-service.sh” can be used to simulate the Cloud Controller
**Setup:**
The following variables must be set:
```
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
```
**Usage:** 
```
> cf_delete-service.sh <OrganizationName>
```

**Result:**
```
- The organization <OrganizationName> is deleted and so all linked objects such as the organization Administrator <AdminName>
```
