#!/bin/bash

PACKAGE_NAME="sonarqube-magento2-rules"
PACKAGE_VERSION="1.0.0"
SONARQUBE_VERSION="25.2.0.102705"
INSTANCE_DIR="../sonarqube-instance/native/sonarqube-${SONARQUBE_VERSION}"
PLUGIN_DIR="${INSTANCE_DIR}/extensions/plugins"
BIN_DIR="${INSTANCE_DIR}/bin/linux-x86-64"

cd ../ && mvn clean package
cp "./target/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar" "${PLUGIN_DIR}/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar"
cp "./target/sonarqube-js-custom-rules-10.0.0.jar" "${PLUGIN_DIR}/sonarqube-js-custom-rules-10.0.0.jar"
"${BIN_DIR}/sonar.sh" console