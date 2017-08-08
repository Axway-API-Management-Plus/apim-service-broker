#-----------------------------------------------------#
# Script used to simulate the CloudController         #
# Action : Delete a service	                          #
#-----------------------------------------------------#
# Version | Label                                     #
#   1.0   | Initial creation                          #
#                                                     #
#-----------------------------------------------------#
#                                                     #
# Usage: cf_delete-service.sh [OrganizationName]      #
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

curl -i -X DELETE ${APIManagerAPIUserLogin}:${APIManagerAPIUserPwd} ${baseURLSBAPI}/v2/service_instances/${OrganizationName} 2>/dev/null


