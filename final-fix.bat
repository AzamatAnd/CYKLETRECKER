@echo off
echo ========================================
echo FINAL FIX - Cleaning everything
echo ========================================

echo.
echo Step 1: Switching to main branch...
git checkout main

echo.
echo Step 2: Checking current status...
git status

echo.
echo Step 3: Adding gradle-wrapper.jar...
git add -f gradle/wrapper/gradle-wrapper.jar

echo.
echo Step 4: Adding all changes...
git add .

echo.
echo Step 5: Committing...
git commit -m "Add gradle wrapper and fix conflicts"

echo.
echo Step 6: Pushing to GitHub...
git push origin main

echo.
echo ========================================
echo DONE! Check GitHub Actions now!
echo ========================================
echo https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
pause

