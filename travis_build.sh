#!/bin/sh

./jcompilo.sh

version_url=https://api.bintray.com/content/raymanoz/careless/careless/${BUILD_NUMBER}

curl --fail -T build/artifacts/careless* -uraymanoz:${BINTRAY_API_KEY} ${version_url}/ || moan "Failed to push distribution to ${version_url}/"
echo

curl --fail -X POST -uraymanoz:${BINTRAY_API_KEY} ${version_url}/publish || moan "Failed to publish distribution with ${version_url}/publish"
echo

