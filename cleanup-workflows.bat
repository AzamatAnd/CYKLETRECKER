@echo off
echo ========================================
echo Cleaning up extra workflows
echo ========================================

echo.
echo Keeping ONLY: .github\workflows\android.yml
echo Deleting all other workflows...

del /f .github\workflows\ultra-simple-build.yml 2>nul
del /f .github\workflows\android-build.yml 2>nul
del /f .github\workflows\android-simple.yml 2>nul
del /f .github\workflows\basic-build.yml 2>nul
del /f .github\workflows\build-apk.yml 2>nul
del /f .github\workflows\debug-build.yml 2>nul
del /f .github\workflows\guaranteed-build.yml 2>nul
del /f .github\workflows\minimal-build.yml 2>nul
del /f .github\workflows\release.yml 2>nul
del /f .github\workflows\simple-build.yml 2>nul
del /f .github\workflows\ultra-minimal.yml 2>nul

echo.
echo Deleting extra batch files...
del /f auto-update.bat 2>nul
del /f create-release.bat 2>nul
del /f fix-all-conflicts.bat 2>nul
del /f fix-all-conflicts.ps1 2>nul
del /f fix-conflicts.ps1 2>nul
del /f fix-wrapper.bat 2>nul
del /f final-fix.bat 2>nul
del /f push-to-main.bat 2>nul
del /f test-trigger.txt 2>nul
del /f universal-update.bat 2>nul
del /f cleanup-workflows.bat 2>nul

echo.
echo Deleting misplaced workflows folder...
rd /s /q workflows 2>nul

echo.
echo Adding changes to Git...
git add .

echo.
echo Committing cleanup...
git commit -m "cleanup: keep only working android.yml workflow"

echo.
echo Pushing to GitHub...
git push origin main-branch:main

echo.
echo ========================================
echo DONE! Only android.yml remains
echo ========================================
echo Check: https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
pause
