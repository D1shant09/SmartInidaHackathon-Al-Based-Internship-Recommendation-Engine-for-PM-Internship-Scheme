@echo off
echo Building and running Spring Boot application...
cd /d "c:\Users\Aizard\Desktop\JAVA\SIH"

REM Set JAVA_HOME to JDK (not JRE)
set "JAVA_HOME=C:\Program Files\Java\jdk-11"
echo JAVA_HOME set to: %JAVA_HOME%

echo.
echo Downloading Maven wrapper if needed...
.\mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b %errorlevel%
)

echo.
echo Starting Spring Boot application...
.\mvnw.cmd spring-boot:run
pause