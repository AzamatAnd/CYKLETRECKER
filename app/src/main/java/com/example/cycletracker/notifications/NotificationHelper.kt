package com.example.cycletracker.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cycletracker.MainActivity
import com.example.cycletracker.R

class NotificationHelper(private val context: Context) {
    
    companion object {
        private const val CHANNEL_ID_CYCLE = "cycle_notifications"
        private const val CHANNEL_ID_REMINDER = "reminder_notifications"
        private const val CHANNEL_ID_SHOPPING = "shopping_notifications"
        
        const val NOTIFICATION_ID_CYCLE_START = 1001
        const val NOTIFICATION_ID_CYCLE_SOON = 1002
        const val NOTIFICATION_ID_SHOPPING = 1003
        const val NOTIFICATION_ID_HEALTH_TIP = 1004
    }
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Канал для уведомлений о цикле
            val cycleChannel = NotificationChannel(
                CHANNEL_ID_CYCLE,
                "Уведомления о цикле",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления о начале и прогнозе цикла"
                enableLights(true)
                lightColor = android.graphics.Color.MAGENTA
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
            }
            
            // Канал для напоминаний
            val reminderChannel = NotificationChannel(
                CHANNEL_ID_REMINDER,
                "Напоминания",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Полезные напоминания и советы"
                enableLights(true)
                lightColor = android.graphics.Color.CYAN
            }
            
            // Канал для покупок
            val shoppingChannel = NotificationChannel(
                CHANNEL_ID_SHOPPING,
                "Напоминания о покупках",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Напоминания о покупке прокладок и других товаров"
                enableLights(true)
                lightColor = android.graphics.Color.YELLOW
                enableVibration(true)
            }
            
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(cycleChannel)
            notificationManager.createNotificationChannel(reminderChannel)
            notificationManager.createNotificationChannel(shoppingChannel)
        }
    }
    
    fun showCycleStartNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_CYCLE)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🌸 Начало цикла")
            .setContentText("Сегодня начинается новый цикл. Позаботьтесь о себе!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Сегодня начинается новый цикл. Не забудьте:\n" +
                        "💧 Пить больше воды\n" +
                        "😴 Больше отдыхать\n" +
                        "🧘 Избегать стресса"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFFE91E63.toInt())
            .build()
        
        showNotification(NOTIFICATION_ID_CYCLE_START, notification)
    }
    
    fun showCycleSoonNotification(daysUntil: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_CYCLE)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🔮 Скоро начало цикла")
            .setContentText("Цикл начнётся через $daysUntil ${getDaysWord(daysUntil)}")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Цикл начнётся через $daysUntil ${getDaysWord(daysUntil)}.\n\n" +
                        "Подготовьтесь заранее:\n" +
                        "✅ Проверьте запасы прокладок\n" +
                        "✅ Подготовьте обезболивающее\n" +
                        "✅ Планируйте меньше дел"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFF9C27B0.toInt())
            .build()
        
        showNotification(NOTIFICATION_ID_CYCLE_SOON, notification)
    }
    
    fun showShoppingReminderNotification(daysUntil: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_SHOPPING)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("🛒 Время пополнить запасы!")
            .setContentText("Цикл через $daysUntil ${getDaysWord(daysUntil)}. Проверьте прокладки!")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("🛒 Напоминание о покупках\n\n" +
                        "Цикл начнётся через $daysUntil ${getDaysWord(daysUntil)}.\n\n" +
                        "Проверьте наличие:\n" +
                        "• Прокладки/тампоны\n" +
                        "• Обезболивающие\n" +
                        "• Грелка\n" +
                        "• Любимые снеки 🍫\n\n" +
                        "Лучше купить заранее!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFFFF9800.toInt())
            .addAction(
                R.drawable.ic_notification,
                "Напомнить позже",
                pendingIntent
            )
            .build()
        
        showNotification(NOTIFICATION_ID_SHOPPING, notification)
    }
    
    fun showHealthTipNotification(tip: String, emoji: String = "💡") {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("$emoji Совет дня от Марии")
            .setContentText(tip)
            .setStyle(NotificationCompat.BigTextStyle().bigText(tip))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFF4CAF50.toInt())
            .build()
        
        showNotification(NOTIFICATION_ID_HEALTH_TIP, notification)
    }
    
    fun showMotivationalNotification() {
        val messages = listOf(
            "🌟 Вы прекрасны! Помните об этом каждый день",
            "💪 Ваше тело удивительно! Заботьтесь о нём",
            "✨ Сегодня отличный день, чтобы быть счастливой",
            "🌸 Вы делаете всё правильно. Продолжайте!",
            "💖 Любите себя такой, какая вы есть"
        )
        
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDER)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Мотивация от Марии")
            .setContentText(messages.random())
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setColor(0xFFE91E63.toInt())
            .build()
        
        showNotification(NOTIFICATION_ID_HEALTH_TIP, notification)
    }
    
    private fun showNotification(id: Int, notification: android.app.Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }
        
        NotificationManagerCompat.from(context).notify(id, notification)
    }
    
    private fun getDaysWord(days: Int): String {
        return when {
            days % 10 == 1 && days % 100 != 11 -> "день"
            days % 10 in 2..4 && days % 100 !in 12..14 -> "дня"
            else -> "дней"
        }
    }
}
