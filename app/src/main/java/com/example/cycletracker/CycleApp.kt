package com.example.cycletracker

import android.app.Application
import com.example.cycletracker.data.AppDatabase
import com.example.cycletracker.notifications.NotificationManager

class CycleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Инициализация базы данных
        AppDatabase.getDatabase(this)
        
        // Создание каналов уведомлений
        NotificationManager.createNotificationChannels(this)
    }
}