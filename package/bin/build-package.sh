#!/bin/bash

set -e

PACKAGER=${1}
INSTALLER_TYPE=${2}
MODULE_PATH=${3}
INPUT=${4}
OUTPUT=${5}
JAR=${6}
VERSION=${7}
FILE_ASSOCIATIONS=${8}
APP_ICON=${9}
EXTRA_BUNDLER_ARGUMENTS=${10}

${PACKAGER} \
  --type ${INSTALLER_TYPE} \
  --module-path ${MODULE_PATH} \
  --verbose \
  --add-modules java.base,java.datatransfer,java.desktop,java.scripting,java.xml,jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom,javafx.controls,javafx.fxml,java.naming,java.sql,jdk.charsets \
  --input "${INPUT}" \
  --dest "${OUTPUT}" \
  --name VocabHunter \
  --main-jar ${JAR} \
  --app-version ${VERSION} \
  --file-associations ${FILE_ASSOCIATIONS} \
  --icon $APP_ICON \
  $EXTRA_BUNDLER_ARGUMENTS \
  --main-class io.github.vocabhunter.gui.main.VocabHunterGuiExecutable
