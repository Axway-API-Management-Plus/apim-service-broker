#------------------------------------------------------------------#
# Script used to simulate the CloudController                      #
# Action : List all organizations                                  #
#------------------------------------------------------------------#
# Version | Label                                                  #
#   1.0   | Initial creation                                       #
#                                                                  #
#------------------------------------------------------------------#
#                                                                  #
# Usage: cf_create-service.sh [OrganizationName] [WebServiceName]  #
#                                                                  #
#----------------------------------------------------------------- #

Usage="`basename $0`"

if [ "$1" == "-h" ]; then
  echo Usage:$Usage
  exit 0
fi

#Environment settings
export APIManagerAPIUserLogin=angel
export APIManagerAPIUserPwd=angel
export baseURLSBAPI=https://api-env.demo.axway.com:8065

# Get the catalog
curl -i -u ${APIManagerAPIUserLogin}:${APIManagerAPIUserPwd} ${baseURLSBAPI}/v2/catalog 2>/dev/null

