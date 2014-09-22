#!/bin/sh

cd "$( dirname "$0" )"
source function_moan.bash

./jcompilo.sh

version_url=https://api.bintray.com/content/raymanoz/repo/careless/${BUILD_NUMBER}
artifact="careless-"${BUILD_NUMBER}

cd build/artifacts
curl --fail -T "{"${artifact}".jar,"${artifact}".pom,"${artifact}"-sources.jar}" -uraymanoz:${BINTRAY_API_KEY} ${version_url}/ || moan "Failed to push distribution to ${version_url}/"
echo

curl --fail -X POST -uraymanoz:${BINTRAY_API_KEY} ${version_url}/publish || moan "Failed to publish distribution with ${version_url}/publish"
echo
