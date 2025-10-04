@echo off
echo Fixing all Git merge conflicts...

REM Find all files with conflicts and fix them
for /r %%f in (*) do (
    findstr /c:"<<<<<<< HEAD" "%%f" >nul 2>&1
    if not errorlevel 1 (
        echo Processing: %%f
        powershell -Command "(Get-Content '%%f' -Raw) -replace '<<<<<<< HEAD\s*\n', '' -replace '=======.*?\n', '' -replace '>>>>>>> [a-f0-9]+\s*\n', '' | Set-Content '%%f' -NoNewline"
    )
)

echo All conflicts fixed!
echo Run: git add . && git commit -m "Fix all conflicts" && git push origin main
pause
