# Policy documentation

5.2.1.	Focus on Catalog API
a.	Policy configuration

 
Figure 15 – Policy GetCatalog

b.	Specific steps description
?	The first step is used to set some environment variables. All those variables are set in the environment settings configuration part:

 
Figure 16 – Environment variables settings

?	A mapping step is called at the end of the policy to map the message body returned by the API Manager API /organization to the format expected by the Cloud Controller:
 
Figure 17 – Message Body mapping
5.2.2.	Focus on Provisioning API
a.	Policy configuration
 
Figure 18 – Policy Provisioning


b.	Specific steps description
?	Accept incomplete
Some Service Broker can accept request for asynchronous provisioning. In that case, the request will include “?accepts_incomplete=true”. As it is not the case for Axway Service Broker, an error must be sent back for call including this option with a response body { "error": "AsyncRequired", "description": "This service plan requires client support for asynchronous service operations." }.

?	Error code management 

 
Figure 19 – Set error code
To return the right error code to the Cloud Foundry Cloud Controller, a specific error code management is set.
At each step, a specific error code is defined. This error code is built with:
•	A first part identifying the Called API Manager (here ORGA-POST for /organization POST)
•	A second part identifying the return code of the API Call
This created return code is then sent to the Policy “Return Error” which will map this error code into the final error code (and its related error label) for Cloud Foundry.


 
5.2.3.	Focus on Binding
a.	Policy configuration
 
Figure 20 – Policy Binding

b.	Specific steps description
No specific step in this policy

5.2.4.	Focus on Unbinding
a.	Policy configuration
 
Figure 21 – Policy Unbinding

b.	Specific steps description
No specific step in this policy

 
5.2.5.	Focus on Deprovisioning
a.	Policy configuration
 
Figure 22 – Policy Deprovisioning

b.	Specific steps description
No specific step in this policy


 
5.2.6.	Focus on Error Management
a.	Policy configuration

 
Figure 23 – Policy ReturnError

This policy is used to send back an error code and an error label in case of error during the process. This is a common policy called by GetCatalog, Provisioninig, Binding, Unbinding and Deprovisioning policies.

b.	Specific steps description
A user KPS table “errorcodeandlabelmapping” is used to store error codes (from API Manager APIs and for Cloud Foundry) and related error labels.
 
Figure 24 – KPS table for error code management

This table is used to map error code that occurred during the policy execution (during a call of an API Manager API for example) into an error code (and the related error label) that are sent to the Cloud Foundry Cloud Controller.
?	apimgrerrorcode is used to store error code return by API Manager APIs
?	apimgrerrorkey is the error code built after a policy step in error (refer to 5.2.2.b-Specific steps description)
?	servicebrokererrorcode is the error code returned by the Service Broker to Cloud Foundry Cloud Controller
?	servicebrokererrorlabel is the error label returned by the Service Broker to Cloud Foundry Cloud Controller

The mapping is done with the following set attribute filter:

 
Figure 25 – Error code mapping

c.	Error code list
Refer to annex 9.1-Error code list
