@echo off
title Cycle Tracker - Create Release
color 0B

echo.
echo ================================================================
echo                    CYCLE TRACKER - CREATE RELEASE
echo ================================================================
echo.

REM Проверяем, что мы в правильной папке
if not exist "app\build.gradle.kts" (
    echo ОШИБКА: Запустите скрипт из папки проекта!
    pause
    exit /b 1
)

echo [1/5] Введите данные для релиза...
set /p version="Введите версию (например: v1.0.0): "
if "%version%"=="" (
    echo ОШИБКА: Версия не может быть пустой!
    pause
    exit /b 1
)

set /p release_notes="Введите описание релиза (или Enter для автоматического): "
if "%release_notes%"=="" (
    set release_notes=Release %version% - Cycle Tracker Android App
)

echo.
echo [2/5] Добавляем все изменения...
git add .

echo [3/5] Создаем коммит...
git commit -m "Release %version%: %release_notes%"

echo [4/5] Создаем тег и загружаем...
git tag %version%
git push origin main
git push origin %version%

echo [5/5] Ожидаем создание релиза...
echo.
echo ================================================================
echo                        РЕЗУЛЬТАТ
echo ================================================================
echo.
echo ✅ Релиз %version% создан!
echo.
echo 📱 GitHub автоматически создаст релиз с APK...
echo    Обычно это занимает 2-5 минут
echo.
echo 🔗 Скачать релиз можно будет здесь:
echo    https://github.com/AzamatAnd/CYKLETRECKER/releases
echo.
echo ================================================================

echo.
echo Нажмите любую клавишу для выхода...
pause >nul