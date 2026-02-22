@echo off
echo ========================================
echo JDK 17 Setup Script for Trivia App
echo ========================================
echo.

REM Check if JDK 17 is installed
echo Checking for JDK 17 installation...
java -version 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java not found. Please install JDK 17 first!
    echo.
    echo Please follow the JDK_SETUP_GUIDE.md for instructions.
    pause
    exit /b 1
)

echo [SUCCESS] Java found!
echo.
echo Current Java version:
java -version
echo.

REM Check JAVA_HOME
echo Checking JAVA_HOME environment variable...
if "%JAVA_HOME%"=="" (
    echo [INFO] JAVA_HOME not set.
    echo.
    echo Setting JAVA_HOME to default location...
    setx JAVA_HOME "C:\Program Files\Java\jdk-17" >nul 2>&1
    if %errorlevel% equ 0 (
        echo [SUCCESS] JAVA_HOME set to: C:\Program Files\Java\jdk-17
    ) else (
        echo [WARNING] Could not set JAVA_HOME automatically.
        echo Please set it manually in: System Properties > Environment Variables
    )
) else (
    echo [INFO] JAVA_HOME is already set to: %JAVA_HOME%
)

echo.
echo Adding JDK to PATH...
setx PATH "%PATH%;%JAVA_HOME%\bin" >nul 2>&1

echo.
echo ========================================
echo Setup Complete!
echo ========================================
echo.
echo Next steps:
echo 1. Open a new Command Prompt
echo 2. Run: gradlew build
echo 3. Or in Android Studio: File > Sync Project with Gradle Files
echo.
pause