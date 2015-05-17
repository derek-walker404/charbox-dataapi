#!/bin/bash


cd ../tpofof-core/;mvn clean install; cd ../charbox-dataapi/
cd ../tpofof-dwa/;mvn clean install; cd ../charbox-dataapi/
cd ../charbox-domain/;mvn clean install; cd ../charbox-dataapi/
cd ../charbox-core/;mvn clean install; cd ../charbox-dataapi/
cd ../charbox-dataapi/;mvn clean install; cd ../charbox-dataapi/
