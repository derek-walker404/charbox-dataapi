#!/bin/bash


cd ~/Workspace/java/tpofof-core/;mvn clean install -DskipTests
cd ~/Workspace/java/tpofof-dwa/;mvn clean install -DskipTests
cd ~/Workspace/java/charbox-domain/;mvn clean install -DskipTests
cd ~/Workspace/java/charbox-core/;mvn clean install -DskipTests
cd ~/Workspace/java/charbox-dataapi/;mvn clean install -DskipTests
