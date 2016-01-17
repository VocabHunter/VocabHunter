#!/usr/bin/env bash

export GIT_TAG=$(git describe --abbrev=0 --tags)
export GITHUB_USER=VocabHunter
export GITHUB_REPO=VocabHunter

echo "User/Organisation: $GITHUB_USER, Repo: $GITHUB_REPO, Tag: $GIT_TAG"
