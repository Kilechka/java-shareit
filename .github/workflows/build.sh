#!/bin/bash

echo "Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

docker-compose --version

cp -rf ./tests/checkstyle.xml ./checkstyle.xml
cp -rf ./tests/suppressions.xml ./suppressions.xml
mvn enforcer:enforce -Drules=requireProfileIdsExist -P check --no-transfer-progress
mvn verify -P check,coverage --no-transfer-progress
docker-compose build