@echo off
echo ========================================
echo    АВТОМАТИЧЕСКОЕ ОБНОВЛЕНИЕ ПРОЕКТА
echo ========================================

echo.
echo 1. Добавляем все изменения...
git add .

echo.
echo 2. Создаем коммит...
set /p commit_msg="Введите сообщение коммита (или Enter для автоматического): "
if "%commit_msg%"=="" (
    set commit_msg=Auto update: %date% %time%
)

git commit -m "%commit_msg%"

echo.
echo 3. Загружаем на GitHub...
git push origin main

echo.
echo 4. Ожидаем сборку...
echo GitHub Actions автоматически соберет APK...
echo Проверьте: https://github.com/AzamatAnd/CYKLETRECKER/actions

echo.
echo ========================================
echo    ОБНОВЛЕНИЕ ЗАВЕРШЕНО!
echo ========================================
pause
