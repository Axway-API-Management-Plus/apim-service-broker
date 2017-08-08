#------------------------------------------------------------------#
# Script used to simulate the CloudController                      #
# Action : unbind a service										   #                                                       #
#------------------------------------------------------------------#
# Version | Label                                                  #
#   1.0   | Initial creation                                       #
#                                                                  #
#------------------------------------------------------------------#
#                                                                  #
# Usage: cf_unbind-service.sh [OrganizationName] [WebServiceName]  #
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


curl -i -X DELETE ${APIManagerAPIUserLogin}:${APIManagerAPIUserPwd} ${baseURLSBAPI}/v2/service_instances/${OrganizationName}/service_bindings/${WebServiceName} 2>/dev/null


