#------------------------------------------------------------------#
# Script used to simulate the CloudController                      #
# Action : bind a service										   #                                                       #
#------------------------------------------------------------------#
# Version | Label                                                  #
#   1.0   | Initial creation                                       #
#                                                                  #
#------------------------------------------------------------------#
#                                                                  #
# Usage: cf_create-service.sh [OrganizationName] [WebServiceName]  #
#                                                                  #
#----------------------------------------------------------------- #


Usage="`basename $0` [OrganizationName] [WebServiceName]"

if [ "$1" == "-h" ]; then
  echo Usage:$Usage
  exit 0
fi

if [ "$#" -ne 2 ]; then
  echo Usage:$Usage
  exit 1
fi


#Environment settings
export OrganizationName=$1
export WebServiceName=$2
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
export SwaggerURL=http://petstore.swagger.io/v2/swagger.json


curl -i -X PUT ${APIManagerAPIUserLogin}:${APIManagerAPIUserPwd} ${baseURLSBAPI}/v2/service_instances/${OrganizationName}/service_bindings/${WebServiceName} \
-H "content-type: application/json" \
-d '{"service_id": "id1", "plan_id": "id2", "bind_resource": {"app_guid": "id3"},"parameters": {"SwaggerURL": "'${SwaggerURL}'"}}' 2>/dev/null


