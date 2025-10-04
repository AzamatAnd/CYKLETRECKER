@echo off
echo ========================================
echo Deleting ALL workflows except android.yml
echo ========================================

cd .github\workflows

echo.
echo Keeping: android.yml
echo Deleting:

del /f android-build.yml
echo   - android-build.yml

del /f android-simple.yml
echo   - android-simple.yml

del /f basic-build.yml
echo   - basic-build.yml

del /f build-apk.yml
echo   - build-apk.yml

del /f debug-build.yml
echo   - debug-build.yml

del /f guaranteed-build.yml
echo   - guaranteed-build.yml

del /f minimal-build.yml
echo   - minimal-build.yml

del /f release.yml
echo   - release.yml

del /f simple-build.yml
echo   - simple-build.yml

del /f ultra-minimal.yml
echo   - ultra-minimal.yml

del /f ultra-simple-build.yml
echo   - ultra-simple-build.yml

cd ..\..

echo.
echo ========================================
echo Committing and pushing...
echo ========================================

git add .
git commit -m "cleanup: remove all workflows except android.yml"
git push origin main-branch:main

echo.
echo ========================================
echo DONE! Only android.yml remains!
echo ========================================
echo Check: https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
pause

