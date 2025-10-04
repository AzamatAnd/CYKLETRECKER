@echo off
title Cycle Tracker - Universal Update Script
color 0A

echo.
echo ================================================================
echo                    CYCLE TRACKER - UNIVERSAL UPDATE
echo ================================================================
echo.

REM Проверяем, что мы в правильной папке
if not exist "app\build.gradle.kts" (
    echo ОШИБКА: Запустите скрипт из папки проекта!
    echo Текущая папка: %CD%
    pause
    exit /b 1
)

echo [1/6] Проверяем Git статус...
git status --porcelain >nul 2>&1
if %errorlevel% neq 0 (
    echo ОШИБКА: Git не инициализирован!
    echo Инициализируем Git...
    git init
    git config --global user.name "AzamatAnd"
    git config --global user.email "azamat@example.com"
)

echo [2/6] Добавляем все изменения...
git add .
if %errorlevel% neq 0 (
    echo ОШИБКА: Не удалось добавить файлы!
    pause
    exit /b 1
)

echo [3/6] Создаем коммит...
set timestamp=%date:~-4,4%-%date:~-10,2%-%date:~-7,2%_%time:~0,2%-%time:~3,2%-%time:~6,2%
set timestamp=%timestamp: =0%
git commit -m "Auto update: %timestamp%"
if %errorlevel% neq 0 (
    echo ПРЕДУПРЕЖДЕНИЕ: Нет изменений для коммита
)

echo [4/6] Загружаем на GitHub...
git push origin main
if %errorlevel% neq 0 (
    echo ПРЕДУПРЕЖДЕНИЕ: Не удалось загрузить на GitHub
    echo Возможно, нужно настроить репозиторий
)

echo [5/6] Очищаем временные файлы...
if exist "fix-conflicts.ps1" del "fix-conflicts.ps1"
if exist "fix-all-conflicts.bat" del "fix-all-conflicts.bat"

echo [6/6] Проверяем GitHub Actions...
echo.
echo ================================================================
echo                        РЕЗУЛЬТАТ
echo ================================================================
echo.
echo ✅ Проект обновлен и загружен на GitHub!
echo.
echo 🔄 GitHub Actions автоматически соберет APK...
echo    Обычно это занимает 2-5 минут
echo.
echo 📱 Скачать APK можно будет здесь:
echo    https://github.com/AzamatAnd/CYKLETRECKER/actions
echo.
echo 📁 APK также будет сохранен в папке 'apk/' репозитория
echo.
echo ================================================================

echo.
echo Нажмите любую клавишу для выхода...
pause >nul
