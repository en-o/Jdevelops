@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo  ==============tips ==============
echo 0.x use jdk8 please
echo jdk manage tool :  https://sdkman.io/install
echo  ==============tips ==============
echo  ==============当前maven环境 ==============
call mvn -v
echo  ==============当前maven环境 ==============

cd jdevelops-build
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-parent
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-annotations
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-spi
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-utils
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-webs
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-apis
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-dals
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-delays
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-events
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-logs
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-files
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-authentications
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-monitor
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-dependencies
call mvn clean package install -Dmaven.test.skip=true

cd ..\jdevelops-frameworks
call mvn clean package install -Dmaven.test.skip=true

echo.
echo 构建完成！按任意键继续...
pause >nul
