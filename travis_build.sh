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

function publish_to_raymanoz() {
    echo "This is a test" > test.txt
    echo $(s3cmd --version)
    s3cmd --access_key=${AWS_ACCESS_KEY} --secret_key=${AWS_SECRET_KEY} put --acl-public --add-header=Cache-Control:"public, max-age=3600" --guess-mime-type test.txt s3://repo.raymanoz.com/test.txt
    echo "s3cmd done"
}

function publish_to_bintray() {
    curl --fail -T "{"${artifact}".jar,"${artifact}".pom,"${artifact}"-sources.jar}" -uraymanoz:${BINTRAY_API_KEY}  -H "X-Bintray-Package:careless" -H "X-Bintray-Version:"${BUILD_NUMBER}  ${version_url}/ || moan "Failed to push distribution to ${version_url}/"
    echo

    curl --fail -X POST -uraymanoz:${BINTRAY_API_KEY} ${repo}/careless/${BUILD_NUMBER}/publish || moan "Failed to publish distribution with ${version_url}/publish"
    echo

    publish_to_raymanoz || moan 'Failed to publish to raymanoz'
}

if [[ "${TRAVIS_BRANCH}" == 'master' && "${TRAVIS_PULL_REQUEST}" == 'false' ]]; then
    publish_to_bintray || moan 'Failed to publish to bintray'
fi