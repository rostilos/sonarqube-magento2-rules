#!/bin/bash

PACKAGE_NAME="sonar-magento2-rules"
PACKAGE_VERSION="1.2.0"
SONARQUBE_VERSION="25.4.0.105899"
INSTANCE_DIR="../sonarqube-instance/native/sonarqube-${SONARQUBE_VERSION}"
PLUGIN_DIR="${INSTANCE_DIR}/extensions/plugins"
BIN_DIR="${INSTANCE_DIR}/bin/linux-x86-64"

cd ../ && mvn clean package
cp "./target/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar" "${PLUGIN_DIR}/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar"
"${BIN_DIR}/sonar.sh" console