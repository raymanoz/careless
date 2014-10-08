#!/bin/bash

function moan(){
  echo -e "$1" 1>&2
  exit 1
}

cd "$( dirname "$0" )"

./jcompilo.sh

repo=https://api.bintray.com/content/raymanoz/repo
version_url=${repo}/com/unsprung/careless/careless/${BUILD_NUMBER}
artifact="careless-"${BUILD_NUMBER}

cd build/artifacts

function publish_to_bintray() {
    curl --fail -T "{"${artifact}".jar,"${artifact}".pom,"${artifact}"-sources.jar}" -uraymanoz:${BINTRAY_API_KEY}  -H "X-Bintray-Package:careless" -H "X-Bintray-Version:"${BUILD_NUMBER}  ${version_url}/ || moan "Failed to push distribution to ${version_url}/"
    echo

    curl --fail -X POST -uraymanoz:${BINTRAY_API_KEY} ${repo}/careless/${BUILD_NUMBER}/publish || moan "Failed to publish distribution with ${version_url}/publish"
    echo

    mkdir s3
    cp *.jar build/artifacts/s3
    cp *.pom build/artifacts/s3
}

if [[ "${TRAVIS_BRANCH}" == 'master' && "${TRAVIS_PULL_REQUEST}" == 'false' ]]; then
    publish_to_bintray || moan 'Failed to publish to bintray'
fi