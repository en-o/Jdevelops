#!/bin/bash

echo  " ==============当前maven环境 =============="
echo  $(mvn -v)
echo  " ==============当前maven环境 =============="


cd jdevelops-build
mvn clean  | tee -a build.log

cd ../jdevelops-dependencies
mvn clean  | tee -a dependencies.log

cd ../jdevelops-parent
mvn clean  | tee -a parent.log

cd ../jdevelops-annotations
mvn clean  | tee -a annotations.log

cd ../jdevelops-spi
mvn clean  | tee -a annotations.log

cd ../jdevelops-apis
mvn clean  | tee -a apis.log

cd ../jdevelops-utils
mvn clean  | tee -a utils.log

cd ../jdevelops-webs
mvn clean  | tee -a webs.log

cd ../jdevelops-apis
mvn clean  | tee -a apis.log

cd ../jdevelops-dals
mvn clean  | tee -a dals.log

cd ../jdevelops-delays
mvn clean  | tee -a delays.log


cd ../jdevelops-events
mvn clean  | tee -a delays.log

read -n 1 -s -r -p "Press any key to continue..."
