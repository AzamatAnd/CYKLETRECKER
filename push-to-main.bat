@echo off
echo ========================================
echo Pushing to MAIN branch
echo ========================================

echo.
echo Step 1: Checking which branch we're on...
git branch

echo.
echo Step 2: Pushing main-branch to main...
git push origin main-branch:main --force

echo.
echo ========================================
echo DONE! Now checking GitHub Actions...
echo ========================================
echo https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
pause

