@echo off
echo ==========================================
echo Trivia App Build Verification
echo ==========================================
echo.

echo Step 1: Cleaning project...
call gradlew clean
if %errorlevel% neq 0 (
    echo [ERROR] Clean failed!
    pause
    exit /b 1
)
echo [SUCCESS] Clean completed!
echo.

echo Step 2: Building project...
call gradlew build --stacktrace
if %errorlevel% neq 0 (
    echo [ERROR] Build failed!
    pause
    exit /b 1
)
echo [SUCCESS] Build completed successfully!
echo.

echo ==========================================
echo All tests passed!
echo ==========================================
pause