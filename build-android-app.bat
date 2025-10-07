@echo off
chcp 65001 >nul
echo 🚀 Начинаем сборку Ultra Modern Cycle Tracker 2025 с оптимизацией для Android 14+...
echo.

echo 📦 Очистка предыдущих сборок...
call gradlew.bat clean

echo.
echo 🔧 Сборка debug версии с оптимизациями...
call gradlew.bat assembleDebug

echo.
echo 🧪 Запуск тестов...
call gradlew.bat testDebugUnitTest

echo.
echo 📱 Сборка release версии с полной оптимизацией для Android 14+...
call gradlew.bat assembleRelease

echo.
echo 📦 Создание бандла для Google Play с поддержкой Android 14...
call gradlew.bat bundleRelease

echo.
echo ✅ Сборка завершена! Приложение оптимизировано для:
echo   🎯 Android 14 (API 34)
echo   📱 Poco X6 Pro и других Xiaomi устройств
echo   📱 Samsung Galaxy S24/S23
echo   📱 Google Pixel 8/7
echo   📱 Все устройства Android 8-14
echo.
echo 📁 Проверьте файлы в папке app/build/outputs/
echo 📱 APK файлы готовы к установке на современные Android устройства
echo.
pause