#!/usr/bin/env bash

set -e

if [ "$#" -ne 1 ]; then
    echo "Usage: filename-to-upload"
    exit 1
fi

FILE_PATH=$1
FILE_NAME=$(basename $FILE_PATH)

. $(dirname $0)/setup-release-variables.sh

echo "Uploading ${FILE_PATH}"
time github-release upload --tag "${GIT_TAG}" --name "${FILE_NAME}" --file "${FILE_PATH}"