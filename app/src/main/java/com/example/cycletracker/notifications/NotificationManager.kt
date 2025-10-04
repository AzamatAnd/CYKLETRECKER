package com.example.cycletracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cycletracker.MainActivity
import com.example.cycletracker.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CycleNotificationManager(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID = "cycle_tracker_channel"
        private const val CHANNEL_NAME = "Cycle Tracker Notifications"
        private const val CHANNEL_DESCRIPTION = "Notifications for cycle tracking and reminders"
        
        private const val PERIOD_REMINDER_ID = 1
        private const val SYMPTOM_REMINDER_ID = 2
        private const val CYCLE_ANALYSIS_ID = 3
    }
    
    init {
        createNotificationChannel()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    fun showPeriodReminder(expectedDate: LocalDate) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Ожидается начало цикла")
            .setContentText("Согласно вашим данным, месячные должны начаться ${expectedDate.format(DateTimeFormatter.ofPattern("dd MMMM"))}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_add,
                "Отметить начало",
                pendingIntent
            )
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(PERIOD_REMINDER_ID, notification)
        }
    }
    
    fun showSymptomReminder() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Напоминание о симптомах")
            .setContentText("Не забудьте отметить симптомы сегодня")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_add,
                "Добавить симптом",
                pendingIntent
            )
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(SYMPTOM_REMINDER_ID, notification)
        }
    }
    
    fun showCycleAnalysis(cycleLength: Int, regularity: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Анализ цикла")
            .setContentText("Ваш цикл: $cycleLength дней, регулярность: $regularity%")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        with(NotificationManagerCompat.from(context)) {
            notify(CYCLE_ANALYSIS_ID, notification)
        }
    }
    
    fun cancelAllNotifications() {
        with(NotificationManagerCompat.from(context)) {
            cancelAll()
        }
    }
}
