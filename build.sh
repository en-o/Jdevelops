#!/bin/bash

echo  " ==============当前maven环境 =============="
echo  $(mvn -v)
echo  " ==============当前maven环境 =============="


cd jdevelops-build
mvn clean package install | tee -a build.log

cd ../jdevelops-dependencies
mvn clean package install | tee -a dependencies.log

cd ../jdevelops-parent
mvn clean package install | tee -a parent.log

cd ../jdevelops-annotations
mvn clean package install | tee -a annotations.log

cd ../jdevelops-spi
mvn clean package install | tee -a annotations.log

cd ../jdevelops-apis
mvn clean package install | tee -a apis.log

cd ../jdevelops-utils
mvn clean package install | tee -a utils.log

cd ../jdevelops-webs
mvn clean package install | tee -a webs.log

cd ../jdevelops-apis
mvn clean package install | tee -a apis.log

cd ../jdevelops-dals
mvn clean package install | tee -a dals.log

cd ../jdevelops-delays
mvn clean package install | tee -a delays.log


