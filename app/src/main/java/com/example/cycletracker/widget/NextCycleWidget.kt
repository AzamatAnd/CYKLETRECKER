package com.example.cycletracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.cycletracker.MainActivity
import com.example.cycletracker.R
import com.example.cycletracker.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class NextCycleWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgets(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (ACTION_REFRESH == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, NextCycleWidget::class.java)
            val ids = appWidgetManager.getAppWidgetIds(componentName)
            updateWidgets(context, appWidgetManager, ids)
        }
    }

    companion object {
        private const val ACTION_REFRESH = "com.example.cycletracker.widget.ACTION_REFRESH"

        fun requestRefresh(context: Context) {
            val intent = Intent(context, NextCycleWidget::class.java).apply {
                action = ACTION_REFRESH
            }
            context.sendBroadcast(intent)
        }

        fun updateWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, NextCycleWidget::class.java)
            val ids = appWidgetManager.getAppWidgetIds(componentName)
            updateWidgets(context, appWidgetManager, ids)
        }

        private fun updateWidgets(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {
                val database = AppDatabase.getDatabase(context)
                val cycles = database.cycleDao().getAllCyclesSnapshot()
                val displayText = buildDisplayText(cycles)
                withContext(Dispatchers.Main) {
                    appWidgetIds.forEach { appWidgetId ->
                        val views = RemoteViews(context.packageName, R.layout.widget_next_cycle)
                        views.setTextViewText(R.id.widget_days_label, displayText.daysLabel)
                        views.setTextViewText(R.id.widget_date_label, displayText.dateLabel)
                        views.setTextViewText(R.id.widget_status_label, displayText.status)

                        val intent = Intent(context, MainActivity::class.java)
                        val pendingIntent = PendingIntent.getActivity(
                            context,
                            0,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                        views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            }
        }

        private fun buildDisplayText(cycles: List<com.example.cycletracker.data.Cycle>): WidgetDisplayText {
            if (cycles.isEmpty()) {
                return WidgetDisplayText(
                    daysLabel = "—",
                    dateLabel = "Нет данных",
                    status = "Начните отслеживание цикла"
                )
            }

            val latestCycle = cycles.first()
            val today = LocalDate.now()
            val cycleLength = latestCycle.cycleLength ?: 28
            val nextCycleStart = latestCycle.startDate.plusDays(cycleLength.toLong())
            val daysUntil = (nextCycleStart.toEpochDay() - today.toEpochDay()).toInt()

            val daysLabel = when {
                daysUntil < 0 -> "Просрочено на ${-daysUntil}"
                daysUntil == 0 -> "Сегодня"
                daysUntil == 1 -> "Завтра"
                else -> "$daysUntil"
            }

            val suffix = when {
                daysUntil < 0 -> "дней"
                daysUntil == 0 -> ""
                daysUntil == 1 -> "день"
                daysUntil in 2..4 -> "дня"
                else -> "дней"
            }

            val dateLabel = if (daysUntil >= 0) {
                nextCycleStart.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
            } else {
                latestCycle.endDate?.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
                    ?: latestCycle.startDate.format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
            }

            val status = when {
                daysUntil < 0 -> "Цикл уже начался"
                daysUntil in 0..3 -> "Подготовьте всё нужное"
                daysUntil <= 10 -> "Отличное время для энергии"
                else -> "Следующий цикл отмечен"
            } + if (suffix.isNotBlank() && daysUntil > 1) " $suffix" else ""

            return WidgetDisplayText(
                daysLabel = daysLabel,
                dateLabel = dateLabel,
                status = status.trim()
            )
        }
    }
}

private data class WidgetDisplayText(
    val daysLabel: String,
    val dateLabel: String,
    val status: String
)

