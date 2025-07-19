@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ============== 配置区域 ==============
REM 设置Maven路径，请根据实际情况修改（建议3.6）
set "MAVEN_HOME=C:\tools\maven\apache-maven-3.6.3"
set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

REM 设置构建参数
set "MVN_GOALS=clean install"
set "MVN_PARAMS=-Dmaven.test.skip=true"
REM ============== 配置区域结束 ==============

echo  ==============tips ==============
echo 0.x use jdk8 please
echo jdk manage tool :  https://sdkman.io/install
echo  ==============tips ==============
echo  ==============当前maven环境 ==============
call "%MVN_CMD%" -v
echo  ==============当前maven环境 ==============

echo 开始构建 jdevelops-build...
cd jdevelops-build
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-build 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-build 构建完成

echo 开始构建 jdevelops-parent...
cd ..\jdevelops-parent
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-parent 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-parent 构建完成

echo 开始构建 jdevelops-annotations...
cd ..\jdevelops-annotations
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-annotations 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-annotations 构建完成

echo 开始构建 jdevelops-spi...
cd ..\jdevelops-spi
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-spi 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-spi 构建完成

echo 开始构建 jdevelops-utils...
cd ..\jdevelops-utils
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-utils 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-utils 构建完成

echo 开始构建 jdevelops-webs...
cd ..\jdevelops-webs
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-webs 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-webs 构建完成

echo 开始构建 jdevelops-apis...
cd ..\jdevelops-apis
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-apis 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-apis 构建完成

echo 开始构建 jdevelops-dals...
cd ..\jdevelops-dals
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-dals 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-dals 构建完成

echo 开始构建 jdevelops-delays...
cd ..\jdevelops-delays
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-delays 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-delays 构建完成

echo 开始构建 jdevelops-events...
cd ..\jdevelops-events
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-events 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-events 构建完成

echo 开始构建 jdevelops-logs...
cd ..\jdevelops-logs
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-logs 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-logs 构建完成

echo 开始构建 jdevelops-files...
cd ..\jdevelops-files
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-files 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-files 构建完成

echo 开始构建 jdevelops-authentications...
cd ..\jdevelops-authentications
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-authentications 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-authentications 构建完成

echo 开始构建 jdevelops-monitor...
cd ..\jdevelops-monitor
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-monitor 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-monitor 构建完成

echo 开始构建 jdevelops-dependencies...
cd ..\jdevelops-dependencies
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-dependencies 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-dependencies 构建完成

echo 开始构建 jdevelops-frameworks...
cd ..\jdevelops-frameworks
call "%MVN_CMD%" %MVN_GOALS% %MVN_PARAMS%
if errorlevel 1 (
    echo [ERROR] jdevelops-frameworks 构建失败！
    pause
    exit /b 1
)
echo [SUCCESS] jdevelops-frameworks 构建完成

echo.
echo ========================================
echo 所有模块构建成功完成！
echo ========================================
echo 按任意键继续...
pause >nul
