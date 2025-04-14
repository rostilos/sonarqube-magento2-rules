#!/bin/bash

PACKAGE_NAME="sonarqube-magento2-rules"
PACKAGE_VERSION="1.1.0"
SONARQUBE_VERSION="25.2.0.102705"
INSTANCE_DIR="/var/www/html/sonarqube-instance/docker/"
BIN_DIR="${INSTANCE_DIR}/bin/linux-x86-64"

cd ../ && mvn clean package
cp "./target/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar" "${INSTANCE_DIR}/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar"

cd ${INSTANCE_DIR}
docker compose up