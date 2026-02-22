@echo off
echo ===============================
echo Build Diagnostic Script
echo ===============================
echo.

echo 1. Checking Java version:
java -version 2>&1
echo.

echo 2. Checking Java compiler:
javac -version 2>&1
echo.

echo 3. Checking JAVA_HOME:
echo JAVA_HOME = %JAVA_HOME%
echo.

echo 4. Checking Gradle version:
call gradlew --version
echo.

echo 5. Listing Gradle tasks:
call gradlew tasks --all | findstr /i "hilt ksp room" | head -20
echo.

echo 6. Checking Kotlin plugin version:
echo From gradle/libs.versions.toml:
type gradle\libs.versions.toml | findstr "kotlin ksp"
echo.

pause