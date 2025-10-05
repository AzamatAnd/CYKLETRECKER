package com.example.cycletracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.example.cycletracker.MainActivity
import com.example.cycletracker.R
import com.example.cycletracker.data.AppDatabase
import com.example.cycletracker.data.CycleRepository
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class NotificationManager(private val context: Context) {
    
    companion object {
        const val CYCLE_CHANNEL_ID = "cycle_reminders"
        const val MEDICATION_CHANNEL_ID = "medication_reminders"
        const val CYCLE_NOTIFICATION_ID = 1001
        const val MEDICATION_NOTIFICATION_ID = 2001
        
        fun createNotificationChannels(context: Context) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Канал для напоминаний о цикле
                val cycleChannel = NotificationChannel(
                    CYCLE_CHANNEL_ID,
                    "Напоминания о цикле",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Уведомления о приближении менструации и овуляции"
                    enableVibration(true)
                    setShowBadge(true)
                }
                
                // Канал для напоминаний о лекарствах
                val medicationChannel = NotificationChannel(
                    MEDICATION_CHANNEL_ID,
                    "Напоминания о лекарствах",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Напоминания о приёме лекарств"
                    enableVibration(false)
                    setShowBadge(true)
                }
                
                notificationManager.createNotificationChannels(listOf(cycleChannel, medicationChannel))
            }
        }
    }
    
    fun scheduleCycleReminder(daysUntilPeriod: Int, nextPeriodDate: LocalDate) {
        val workRequest = OneTimeWorkRequestBuilder<CycleReminderWorker>()
            .setInputData(workDataOf(
                "days_until_period" to daysUntilPeriod,
                "next_period_date" to nextPeriodDate.toString()
            ))
            .setInitialDelay(1, TimeUnit.HOURS) // Напоминание через час
            .build()
            
        WorkManager.getInstance(context).enqueueUniqueWork(
            "cycle_reminder_$daysUntilPeriod",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    fun scheduleMedicationReminder(medicationName: String, time: String) {
        val workRequest = OneTimeWorkRequestBuilder<MedicationReminderWorker>()
            .setInputData(workDataOf(
                "medication_name" to medicationName,
                "time" to time
            ))
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
            
        WorkManager.getInstance(context).enqueueUniqueWork(
            "medication_reminder_${medicationName}_$time",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
    
    fun showCycleNotification(daysUntilPeriod: Int, nextPeriodDate: LocalDate) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val title = when {
            daysUntilPeriod == 0 -> "Сегодня начало цикла"
            daysUntilPeriod == 1 -> "Завтра начало цикла"
            daysUntilPeriod <= 3 -> "Цикл начнётся через $daysUntilPeriod дня"
            else -> "Напоминание о цикле"
        }
        
        val message = when {
            daysUntilPeriod == 0 -> "Подготовьте всё необходимое"
            daysUntilPeriod == 1 -> "Завтра, ${nextPeriodDate.dayOfMonth} ${getMonthName(nextPeriodDate.monthValue)}"
            else -> "Ожидаемая дата: ${nextPeriodDate.dayOfMonth} ${getMonthName(nextPeriodDate.monthValue)}"
        }
        
        val notification = NotificationCompat.Builder(context, CYCLE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cycle_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
            
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(CYCLE_NOTIFICATION_ID, notification)
    }
    
    fun showMedicationNotification(medicationName: String, time: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_screen", "medications")
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, MEDICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_medication_notification)
            .setContentTitle("Время принять лекарство")
            .setContentText("$medicationName в $time")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()
            
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(MEDICATION_NOTIFICATION_ID, notification)
    }
    
    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""
        }
    }
}

class CycleReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    
    override fun doWork(): Result {
        val daysUntilPeriod = inputData.getInt("days_until_period", 0)
        val nextPeriodDate = LocalDate.parse(inputData.getString("next_period_date"))
        
        val notificationManager = NotificationManager(applicationContext)
        notificationManager.showCycleNotification(daysUntilPeriod, nextPeriodDate)
        
        return Result.success()
    }
}

class MedicationReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    
    override fun doWork(): Result {
        val medicationName = inputData.getString("medication_name") ?: ""
        val time = inputData.getString("time") ?: ""
        
        val notificationManager = NotificationManager(applicationContext)
        notificationManager.showMedicationNotification(medicationName, time)
        
        return Result.success()
    }
}
