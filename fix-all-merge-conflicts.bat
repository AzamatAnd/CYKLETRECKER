@echo off
echo ========================================
echo Fixing ALL merge conflicts in XML files
echo ========================================

echo.
echo Processing files...

powershell -ExecutionPolicy Bypass -Command "$files = Get-ChildItem -Path 'app\src\main\res' -Include '*.xml' -Recurse; foreach ($file in $files) { $content = Get-Content $file.FullName -Raw; if ($content -match '<<<<<<< HEAD') { Write-Host \"Fixing: $($file.FullName)\"; $lines = Get-Content $file.FullName; $output = @(); $skip = $false; foreach ($line in $lines) { if ($line -match '^<<<<<<< HEAD') { $skip = $true; continue; } if ($line -match '^=======') { $skip = $false; continue; } if ($line -match '^>>>>>>> ') { continue; } if (-not $skip) { $output += $line; } } $output | Set-Content $file.FullName -Encoding UTF8; } }"

echo.
echo ========================================
echo Committing and pushing...
echo ========================================

git add .
git commit -m "fix: remove all merge conflict markers from XML files"
git push origin main-branch:main

echo.
echo ========================================
echo DONE! Check GitHub Actions now!
echo ========================================
echo https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
pause

