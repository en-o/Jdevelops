#!/bin/bash

echo  " ==============当前maven环境 =============="
echo  $(mvn -v)
echo  " ==============当前maven环境 =============="


cd jdevelops-build
mvn clean

cd ../jdevelops-parent
mvn clean

cd ../jdevelops-annotations
mvn clean

cd ../jdevelops-spi
mvn clean

cd ../jdevelops-utils
mvn clean

cd ../jdevelops-webs
mvn clean

cd ../jdevelops-apis
mvn clean

cd ../jdevelops-dals
mvn clean

cd ../jdevelops-delays
mvn clean

cd ../jdevelops-events
mvn clean

cd ../jdevelops-files
mvn clean

cd ../jdevelops-logs
mvn clean

cd ../jdevelops-authentications
mvn clean

cd ../jdevelops-monitor
mvn clean

cd ../jdevelops-dependencies
mvn clean

cd ../jdevelops-frameworks
mvn clean

read -n 1 -s -r -p "Press any key to continue..."
