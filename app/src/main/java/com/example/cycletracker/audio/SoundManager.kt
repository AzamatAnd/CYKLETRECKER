<<<<<<< HEAD
package com.example.cycletracker.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundType, Int>()
    private var isEnabled = true
    private var volume = 0.7f

    init {
        initializeSoundPool()
        loadSounds()
    }

    private fun initializeSoundPool() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            
            SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            @Suppress("DEPRECATION")
            SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
    }

    private fun loadSounds() {
        // Загружаем звуки из ресурсов
        // В реальном приложении здесь будут загружены настоящие звуковые файлы
        soundMap[SoundType.BUTTON_CLICK] = 1
        soundMap[SoundType.SUCCESS] = 2
        soundMap[SoundType.ERROR] = 3
        soundMap[SoundType.NOTIFICATION] = 4
        soundMap[SoundType.CALENDAR_NAVIGATE] = 5
        soundMap[SoundType.DATA_SAVED] = 6
        soundMap[SoundType.PERIOD_START] = 7
        soundMap[SoundType.REMINDER] = 8
        soundMap[SoundType.AMBIENT_NATURE] = 9
        soundMap[SoundType.AMBIENT_RAIN] = 10
    }

    fun playSound(soundType: SoundType) {
        if (!isEnabled) return
        
        val soundId = soundMap[soundType] ?: return
        soundPool?.play(soundId, volume, volume, 1, 0, 1f)
    }

    fun playSoundWithDelay(soundType: SoundType, delayMs: Long = 100) {
        if (!isEnabled) return
        
        kotlinx.coroutines.GlobalScope.launch {
            delay(delayMs)
            playSound(soundType)
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}

enum class SoundType {
    // UI Sounds
    BUTTON_CLICK,
    SUCCESS,
    ERROR,
    NOTIFICATION,
    
    // Navigation Sounds
    CALENDAR_NAVIGATE,
    TAB_SWITCH,
    SCREEN_TRANSITION,
    
    // Data Sounds
    DATA_SAVED,
    DATA_DELETED,
    FORM_SUBMIT,
    
    // Cycle Sounds
    PERIOD_START,
    PERIOD_END,
    OVULATION_PREDICTED,
    CYCLE_COMPLETE,
    
    // Reminder Sounds
    REMINDER,
    DAILY_REMINDER,
    WEEKLY_SUMMARY,
    
    // Ambient Sounds
    AMBIENT_NATURE,
    AMBIENT_RAIN,
    AMBIENT_OCEAN,
    AMBIENT_FOREST,
    
    // Mood Sounds
    MOOD_HAPPY,
    MOOD_SAD,
    MOOD_NEUTRAL,
    MOOD_ENERGETIC,
    
    // Achievement Sounds
    ACHIEVEMENT_UNLOCKED,
    STREAK_MILESTONE,
    GOAL_COMPLETED,
    MILESTONE_REACHED
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { SoundManager(context) }
}

// Расширения для удобного использования
fun SoundManager.playButtonClick() = playSound(SoundType.BUTTON_CLICK)
fun SoundManager.playSuccess() = playSound(SoundType.SUCCESS)
fun SoundManager.playError() = playSound(SoundType.ERROR)
fun SoundManager.playNotification() = playSound(SoundType.NOTIFICATION)
fun SoundManager.playDataSaved() = playSound(SoundType.DATA_SAVED)
fun SoundManager.playPeriodStart() = playSound(SoundType.PERIOD_START)
fun SoundManager.playReminder() = playSound(SoundType.REMINDER)
=======
package com.example.cycletracker.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

class SoundManager(private val context: Context) {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundType, Int>()
    private var isEnabled = true
    private var volume = 0.7f

    init {
        initializeSoundPool()
        loadSounds()
    }

    private fun initializeSoundPool() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            
            SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            @Suppress("DEPRECATION")
            SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
    }

    private fun loadSounds() {
        // Загружаем звуки из ресурсов
        // В реальном приложении здесь будут загружены настоящие звуковые файлы
        soundMap[SoundType.BUTTON_CLICK] = 1
        soundMap[SoundType.SUCCESS] = 2
        soundMap[SoundType.ERROR] = 3
        soundMap[SoundType.NOTIFICATION] = 4
        soundMap[SoundType.CALENDAR_NAVIGATE] = 5
        soundMap[SoundType.DATA_SAVED] = 6
        soundMap[SoundType.PERIOD_START] = 7
        soundMap[SoundType.REMINDER] = 8
        soundMap[SoundType.AMBIENT_NATURE] = 9
        soundMap[SoundType.AMBIENT_RAIN] = 10
    }

    fun playSound(soundType: SoundType) {
        if (!isEnabled) return
        
        val soundId = soundMap[soundType] ?: return
        soundPool?.play(soundId, volume, volume, 1, 0, 1f)
    }

    fun playSoundWithDelay(soundType: SoundType, delayMs: Long = 100) {
        if (!isEnabled) return
        
        kotlinx.coroutines.GlobalScope.launch {
            delay(delayMs)
            playSound(soundType)
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    fun setVolume(volume: Float) {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}

enum class SoundType {
    // UI Sounds
    BUTTON_CLICK,
    SUCCESS,
    ERROR,
    NOTIFICATION,
    
    // Navigation Sounds
    CALENDAR_NAVIGATE,
    TAB_SWITCH,
    SCREEN_TRANSITION,
    
    // Data Sounds
    DATA_SAVED,
    DATA_DELETED,
    FORM_SUBMIT,
    
    // Cycle Sounds
    PERIOD_START,
    PERIOD_END,
    OVULATION_PREDICTED,
    CYCLE_COMPLETE,
    
    // Reminder Sounds
    REMINDER,
    DAILY_REMINDER,
    WEEKLY_SUMMARY,
    
    // Ambient Sounds
    AMBIENT_NATURE,
    AMBIENT_RAIN,
    AMBIENT_OCEAN,
    AMBIENT_FOREST,
    
    // Mood Sounds
    MOOD_HAPPY,
    MOOD_SAD,
    MOOD_NEUTRAL,
    MOOD_ENERGETIC,
    
    // Achievement Sounds
    ACHIEVEMENT_UNLOCKED,
    STREAK_MILESTONE,
    GOAL_COMPLETED,
    MILESTONE_REACHED
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    return remember { SoundManager(context) }
}

// Расширения для удобного использования
fun SoundManager.playButtonClick() = playSound(SoundType.BUTTON_CLICK)
fun SoundManager.playSuccess() = playSound(SoundType.SUCCESS)
fun SoundManager.playError() = playSound(SoundType.ERROR)
fun SoundManager.playNotification() = playSound(SoundType.NOTIFICATION)
fun SoundManager.playDataSaved() = playSound(SoundType.DATA_SAVED)
fun SoundManager.playPeriodStart() = playSound(SoundType.PERIOD_START)
fun SoundManager.playReminder() = playSound(SoundType.REMINDER)
>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
