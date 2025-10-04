package com.example.cycletracker.audio

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

class CycleSoundManager(private val context: Context) {
    private val soundGenerator = SoundGenerator(context)
    private var isEnabled = true
    private var volume = 0.7f
    private var ambientEnabled = false

    // Основные звуки приложения
    fun playButtonClick() {
        if (isEnabled) soundGenerator.playButtonClick()
    }

    fun playSuccess() {
        if (isEnabled) soundGenerator.playSuccess()
    }

    fun playError() {
        if (isEnabled) soundGenerator.playError()
    }

    fun playNotification() {
        if (isEnabled) soundGenerator.playNotification()
    }

    fun playDataSaved() {
        if (isEnabled) soundGenerator.playDataSaved()
    }

    // Звуки циклов
    fun playCycleStart() {
        if (isEnabled) soundGenerator.playCycleStart()
    }

    fun playPeriodStart() {
        if (isEnabled) soundGenerator.playPeriodStart()
    }

    fun playOvulationPredicted() {
        if (isEnabled) soundGenerator.playOvulationPredicted()
    }

    fun playReminder() {
        if (isEnabled) soundGenerator.playReminder()
    }

    // Звуки настроения
    fun playMoodSound(moodLevel: Int) {
        if (isEnabled) soundGenerator.playMoodSound(moodLevel)
    }

    // Звуки достижений
    fun playAchievement() {
        if (isEnabled) soundGenerator.playAchievement()
    }

    // Фоновые звуки
    fun playAmbientNature() {
        if (ambientEnabled) soundGenerator.playAmbientNature()
    }

    fun playAmbientRain() {
        if (ambientEnabled) soundGenerator.playAmbientRain()
    }

    // Настройки
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun setAmbientEnabled(enabled: Boolean) {
        ambientEnabled = enabled
    }

    fun isSoundEnabled(): Boolean = isEnabled
    fun isAmbientEnabled(): Boolean = ambientEnabled
    fun getVolume(): Float = volume

    // Специальные звуковые сценарии
    fun playCycleComplete() {
        if (isEnabled) {
            soundGenerator.playSuccess()
            kotlinx.coroutines.GlobalScope.launch {
                delay(500)
                soundGenerator.playAchievement()
            }
        }
    }

    fun playSymptomAdded() {
        if (isEnabled) {
            soundGenerator.playDataSaved()
            kotlinx.coroutines.GlobalScope.launch {
                delay(200)
                soundGenerator.playButtonClick()
            }
        }
    }

    fun playCalendarNavigation() {
        if (isEnabled) {
            soundGenerator.playButtonClick()
        }
    }

    fun playTabSwitch() {
        if (isEnabled) {
            soundGenerator.playButtonClick()
        }
    }

    fun playFormSubmit() {
        if (isEnabled) {
            soundGenerator.playDataSaved()
        }
    }

    fun playDataDeleted() {
        if (isEnabled) {
            soundGenerator.playError()
        }
    }

    fun playStreakMilestone() {
        if (isEnabled) {
            soundGenerator.playAchievement()
        }
    }

    fun playGoalCompleted() {
        if (isEnabled) {
            soundGenerator.playSuccess()
            kotlinx.coroutines.GlobalScope.launch {
                delay(300)
                soundGenerator.playAchievement()
            }
        }
    }

    fun playMilestoneReached() {
        if (isEnabled) {
            soundGenerator.playAchievement()
        }
    }

    // Контекстные звуки для разных экранов
    fun playHomeScreenEnter() {
        if (isEnabled) {
            soundGenerator.playCycleStart()
        }
    }

    fun playCalendarScreenEnter() {
        if (isEnabled) {
            soundGenerator.playNotification()
        }
    }

    fun playStatisticsScreenEnter() {
        if (isEnabled) {
            soundGenerator.playSuccess()
        }
    }

    fun playSettingsScreenEnter() {
        if (isEnabled) {
            soundGenerator.playButtonClick()
        }
    }

    fun playHistoryScreenEnter() {
        if (isEnabled) {
            soundGenerator.playReminder()
        }
    }

    // Звуки для разных типов симптомов
    fun playSymptomSound(symptomType: String) {
        if (isEnabled) {
            when (symptomType.lowercase()) {
                "боль в животе", "спазмы" -> {
                    soundGenerator.playError()
                }
                "головная боль" -> {
                    soundGenerator.playError()
                }
                "усталость" -> {
                    soundGenerator.playReminder()
                }
                "перепады настроения" -> {
                    soundGenerator.playMoodSound(5)
                }
                "вздутие" -> {
                    soundGenerator.playButtonClick()
                }
                "тошнота" -> {
                    soundGenerator.playError()
                }
                "болезненность груди" -> {
                    soundGenerator.playNotification()
                }
                else -> {
                    soundGenerator.playDataSaved()
                }
            }
        }
    }

    // Звуки для разных фаз цикла
    fun playCyclePhaseSound(phase: String) {
        if (isEnabled) {
            when (phase.lowercase()) {
                "месячные", "период" -> {
                    soundGenerator.playPeriodStart()
                }
                "фолликулярная" -> {
                    soundGenerator.playCycleStart()
                }
                "овуляция" -> {
                    soundGenerator.playOvulationPredicted()
                }
                "лютеиновая" -> {
                    soundGenerator.playReminder()
                }
                else -> {
                    soundGenerator.playButtonClick()
                }
            }
        }
    }

    fun release() {
        // Освобождаем ресурсы если нужно
    }
}

@Composable
fun rememberCycleSoundManager(): CycleSoundManager {
    val context = LocalContext.current
    return remember { CycleSoundManager(context) }
}

// Расширения для удобного использования в Compose
@Composable
fun rememberSoundManager(): CycleSoundManager = rememberCycleSoundManager()

// Глобальный звуковой менеджер
object GlobalSoundManager {
    private var instance: CycleSoundManager? = null
    
    fun initialize(context: Context) {
        instance = CycleSoundManager(context)
    }
    
    fun getInstance(): CycleSoundManager? = instance
    
    fun playButtonClick() = instance?.playButtonClick()
    fun playSuccess() = instance?.playSuccess()
    fun playError() = instance?.playError()
    fun playNotification() = instance?.playNotification()
    fun playDataSaved() = instance?.playDataSaved()
    fun playCycleStart() = instance?.playCycleStart()
    fun playPeriodStart() = instance?.playPeriodStart()
    fun playOvulationPredicted() = instance?.playOvulationPredicted()
    fun playReminder() = instance?.playReminder()
    fun playMoodSound(moodLevel: Int) = instance?.playMoodSound(moodLevel)
    fun playAchievement() = instance?.playAchievement()
    fun playAmbientNature() = instance?.playAmbientNature()
    fun playAmbientRain() = instance?.playAmbientRain()
}
