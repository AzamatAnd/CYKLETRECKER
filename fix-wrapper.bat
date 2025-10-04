@echo off
echo Downloading gradle-wrapper.jar...
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $ProgressPreference = 'SilentlyContinue'; Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.10.2/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'"

echo.
echo Checking file...
if exist "gradle\wrapper\gradle-wrapper.jar" (
    echo File created successfully!
    dir "gradle\wrapper\gradle-wrapper.jar"
) else (
    echo ERROR: File not created!
    exit /b 1
)

echo.
echo Adding to Git...
git add -f gradle/wrapper/gradle-wrapper.jar
git status

echo.
echo Committing...
git commit -m "Add gradle-wrapper.jar"

echo.
echo Pushing...
git push origin main

echo.
echo DONE!
pause

