#!/bin/bash

# Copyright (C) 2025 Rostislav Suleimanov
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

PACKAGE_NAME="sonar-magento2-rules"
PACKAGE_VERSION="1.2.0"
SONARQUBE_VERSION="25.4.0.105899"
INSTANCE_DIR="/var/www/html/sonarqube-instance/docker/"
BIN_DIR="${INSTANCE_DIR}/bin/linux-x86-64"

cd ../ && mvn clean package
cp ./target/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar ${INSTANCE_DIR}/${PACKAGE_NAME}-${PACKAGE_VERSION}.jar

cd ${INSTANCE_DIR}
docker compose up