#-----------------------------------------------------#
# Script used to simulate the CloudController         #
# Action : Create a service	                          #
#-----------------------------------------------------#
# Version | Label                                     #
#   1.0   | Initial creation                          #
#                                                     #
#-----------------------------------------------------#
#                                                     #
# Usage: cf_create-service.sh [OrganizationName]      #
#                                                     #
#-----------------------------------------------------#


Usage="`basename $0` [OrganizationName]"

if [ "$1" == "-h" ]; then
  echo Usage:$Usage
  exit 0
fi

if [ "$#" -ne 1 ]; then
  echo Usage:$Usage
  exit 1
fi


#Environment settings
export export OrganizationName=$1
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065
export AdminName=orgaadmin
export AdminEmail=orgaadmin@demo.axway.com


curl -i -X PUT ${APIManagerAPIUserLogin}:${APIManagerAPIUserPwd} ${baseURLSBAPI}/v2/service_instances/${OrganizationName}?accepts_incomplete=false \
-H "content-type: application/json" \
-d '{"service_id": "id1", "plan_id": "id2", "organization_guid": "id3", "space_guid": "id4", "parameters": {"AdminName": "'${AdminName}'","AdminEmail": "'${AdminEmail}'"}}' 2>/dev/null


