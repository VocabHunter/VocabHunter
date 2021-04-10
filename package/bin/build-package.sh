#!/bin/bash

set -e

INSTALLER_TYPE=${1}
MODULE_PATH=${2}
INPUT=${3}
OUTPUT=${4}
JAR=${5}
VERSION=${6}
FILE_ASSOCIATIONS=${7}
APP_ICON=${8}
EXTRA_BUNDLER_ARGUMENTS=${9}

jpackage \
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
