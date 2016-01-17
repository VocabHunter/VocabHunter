#!/usr/bin/env bash

set -e

. $(dirname $0)/setup-release-variables.sh

echo "Creating draft release ${GIT_TAG}"
github-release release --tag ${GIT_TAG} --draft