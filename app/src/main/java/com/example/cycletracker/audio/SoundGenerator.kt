package com.example.cycletracker.audio

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.math.*

class SoundGenerator(private val context: Context) {
    private val sampleRate = 44100
    private val duration = 0.8f // секунды
    private val numSamples = (sampleRate * duration).toInt()

    // Оригинальные звуковые паттерны для CycleTracker
    private val cycleMelody = listOf(523.25, 587.33, 659.25, 698.46, 783.99) // C-D-E-F-G
    private val periodMelody = listOf(440.0, 493.88, 523.25, 587.33) // A-B-C-D
    private val moodMelodies = mapOf(
        1 to listOf(261.63, 293.66, 329.63), // C-D-E (минор)
        2 to listOf(293.66, 329.63, 349.23), // D-E-F (минор)
        3 to listOf(329.63, 349.23, 392.00), // E-F-G (минор)
        4 to listOf(349.23, 392.00, 440.0),  // F-G-A (минор)
        5 to listOf(392.00, 440.0, 493.88),  // G-A-B (минор)
        6 to listOf(440.0, 493.88, 523.25),  // A-B-C (мажор)
        7 to listOf(493.88, 523.25, 587.33), // B-C-D (мажор)
        8 to listOf(523.25, 587.33, 659.25), // C-D-E (мажор)
        9 to listOf(587.33, 659.25, 698.46), // D-E-F (мажор)
        10 to listOf(659.25, 698.46, 783.99) // E-F-G (мажор)
    )

    fun generateComplexTone(
        frequencies: List<Double>, 
        amplitudes: List<Double> = listOf(0.1),
        duration: Float = this.duration,
        envelope: (Int, Int) -> Double = { _, _ -> 1.0 }
    ): ByteArray {
        val samples = ShortArray((sampleRate * duration).toInt())
        
        for (i in 0 until samples.size) {
            var sample = 0.0
            frequencies.forEachIndexed { index, freq ->
                val amplitude = amplitudes.getOrElse(index) { 0.1 }
                val angle = 2.0 * PI * i * freq / sampleRate
                val envelopeValue = envelope(i, samples.size)
                sample += amplitude * sin(angle) * envelopeValue
            }
            samples[i] = (sample * Short.MAX_VALUE).toInt().toShort()
        }
        
        val generatedSnd = ByteArray(2 * samples.size)
        var idx = 0
        for (i in 0 until samples.size) {
            val sample = samples[i]
            generatedSnd[idx++] = (sample and 0x00ff).toByte()
            generatedSnd[idx++] = ((sample and 0xff00) shr 8).toByte()
        }
        
        return generatedSnd
    }

    fun playComplexTone(
        frequencies: List<Double>, 
        amplitudes: List<Double> = listOf(0.1),
        duration: Float = this.duration,
        envelope: (Int, Int) -> Double = { _, _ -> 1.0 }
    ) {
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            (sampleRate * duration).toInt() * 2,
            AudioTrack.MODE_STATIC
        )
        
        val generatedSnd = generateComplexTone(frequencies, amplitudes, duration, envelope)
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
        
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            audioTrack.stop()
            audioTrack.release()
        }, (duration * 1000).toLong())
    }

    // 🌸 Оригинальный звук "Цветок распускается" для начала цикла
    fun playCycleStart() {
        val frequencies = listOf(261.63, 329.63, 392.00, 523.25) // C-E-G-C
        val amplitudes = listOf(0.15, 0.12, 0.10, 0.08)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            when {
                progress < 0.3 -> progress / 0.3 // Нарастание
                progress < 0.7 -> 1.0 // Плавное удержание
                else -> (1.0 - progress) / 0.3 // Затухание
            }
        }
        playComplexTone(frequencies, amplitudes, 1.2f, envelope)
    }

    // 🩸 Звук "Капля воды" для периода
    fun playPeriodStart() {
        val frequencies = listOf(440.0, 523.25, 659.25) // A-C-E
        val amplitudes = listOf(0.12, 0.10, 0.08)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            exp(-progress * 3) * (1 + 0.3 * sin(progress * PI * 4)) // Экспоненциальное затухание с пульсацией
        }
        playComplexTone(frequencies, amplitudes, 0.8f, envelope)
    }

    // ✨ Звук "Звездочка" для овуляции
    fun playOvulationPredicted() {
        val frequencies = listOf(523.25, 659.25, 783.99, 1046.50) // C-E-G-C
        val amplitudes = listOf(0.08, 0.10, 0.12, 0.15)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            sin(progress * PI) * (1 + 0.2 * sin(progress * PI * 8)) // Синусоидальная огибающая с трелью
        }
        playComplexTone(frequencies, amplitudes, 1.0f, envelope)
    }

    // 🎵 Оригинальный звук кнопки "Лепесток касается воды"
    fun playButtonClick() {
        val frequencies = listOf(800.0, 1000.0) // Два тона
        val amplitudes = listOf(0.06, 0.04)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            exp(-progress * 8) // Быстрое затухание
        }
        playComplexTone(frequencies, amplitudes, 0.2f, envelope)
    }

    // 🌟 Звук успеха "Цветение"
    fun playSuccess() {
        val frequencies = listOf(392.00, 493.88, 587.33, 698.46, 783.99) // G-B-D-F-G
        val amplitudes = listOf(0.10, 0.12, 0.14, 0.12, 0.10)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            when {
                progress < 0.2 -> progress / 0.2
                progress < 0.8 -> 1.0
                else -> (1.0 - progress) / 0.2
            }
        }
        playComplexTone(frequencies, amplitudes, 1.5f, envelope)
    }

    // 💫 Звук ошибки "Увядание"
    fun playError() {
        val frequencies = listOf(440.0, 392.00, 349.23, 293.66) // A-G-F-D
        val amplitudes = listOf(0.12, 0.10, 0.08, 0.06)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            exp(-progress * 2) * (1 - progress * 0.5) // Экспоненциальное затухание
        }
        playComplexTone(frequencies, amplitudes, 0.8f, envelope)
    }

    // 🔔 Звук уведомления "Колокольчик ветра"
    fun playNotification() {
        val frequencies = listOf(523.25, 659.25, 783.99) // C-E-G
        val amplitudes = listOf(0.08, 0.10, 0.12)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            sin(progress * PI) * (1 + 0.3 * sin(progress * PI * 6)) // Синусоида с трелью
        }
        playComplexTone(frequencies, amplitudes, 0.6f, envelope)
    }

    // 💾 Звук сохранения "Капля в озеро"
    fun playDataSaved() {
        val frequencies = listOf(440.0, 523.25) // A-C
        val amplitudes = listOf(0.08, 0.06)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            exp(-progress * 4) * (1 + 0.2 * sin(progress * PI * 3)) // Быстрое затухание с пульсацией
        }
        playComplexTone(frequencies, amplitudes, 0.4f, envelope)
    }

    // 🌙 Звук напоминания "Лунный свет"
    fun playReminder() {
        val frequencies = listOf(392.00, 440.0, 493.88) // G-A-B
        val amplitudes = listOf(0.06, 0.08, 0.06)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            sin(progress * PI) * (1 + 0.1 * sin(progress * PI * 4)) // Мягкая синусоида
        }
        playComplexTone(frequencies, amplitudes, 0.8f, envelope)
    }

    // 😊 Звук настроения "Эмоциональная волна"
    fun playMoodSound(moodLevel: Int) {
        val melody = moodMelodies[moodLevel] ?: moodMelodies[5]!!
        val baseAmplitude = 0.08
        val amplitudes = melody.map { baseAmplitude }
        
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            when (moodLevel) {
                in 1..3 -> exp(-progress * 2) // Быстрое затухание для плохого настроения
                in 4..6 -> sin(progress * PI) // Плавная синусоида для нейтрального
                in 7..10 -> sin(progress * PI) * (1 + 0.2 * sin(progress * PI * 3)) // Пульсирующая для хорошего
                else -> sin(progress * PI)
            }
        }
        playComplexTone(melody, amplitudes, 1.0f, envelope)
    }

    // 🏆 Звук достижения "Цветочный фейерверк"
    fun playAchievement() {
        val frequencies = listOf(523.25, 659.25, 783.99, 1046.50, 1318.51, 1567.98) // C-E-G-C-E-G
        val amplitudes = listOf(0.10, 0.12, 0.14, 0.16, 0.14, 0.12)
        val envelope: (Int, Int) -> Double = { i, total ->
            val progress = i.toDouble() / total
            when {
                progress < 0.3 -> progress / 0.3
                progress < 0.7 -> 1.0 + 0.3 * sin(progress * PI * 4)
                else -> (1.0 - progress) / 0.3
            }
        }
        playComplexTone(frequencies, amplitudes, 2.0f, envelope)
    }

    // 🌿 Фоновый звук природы "Лесной шепот"
    fun playAmbientNature() {
        val samples = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val noise = (Math.random() * 2 - 1) * 0.05
            val lowFreq = sin(2 * PI * i / sampleRate * 0.1) * 0.02
            val midFreq = sin(2 * PI * i / sampleRate * 0.5) * 0.01
            val highFreq = sin(2 * PI * i / sampleRate * 2.0) * 0.005
            
            val filtered = noise * (1 + sin(progress * PI * 2)) / 2
            val result = filtered + lowFreq + midFreq + highFreq
            samples[i] = (result * Short.MAX_VALUE).toInt().toShort()
        }
        
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            numSamples * 2,
            AudioTrack.MODE_STATIC
        )
        
        val generatedSnd = ByteArray(2 * numSamples)
        var idx = 0
        for (i in 0 until numSamples) {
            val sample = samples[i]
            generatedSnd[idx++] = (sample and 0x00ff).toByte()
            generatedSnd[idx++] = ((sample and 0xff00) shr 8).toByte()
        }
        
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
    }

    // 🌧️ Звук дождя "Капли на листьях"
    fun playAmbientRain() {
        val samples = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val progress = i.toDouble() / numSamples
            val noise = (Math.random() * 2 - 1) * 0.08
            val dropPattern = if (Math.random() < 0.1) sin(2 * PI * i / sampleRate * 200) * 0.03 else 0.0
            val wind = sin(2 * PI * i / sampleRate * 0.3) * 0.02
            
            val result = noise * (1 + sin(progress * PI * 3)) / 2 + dropPattern + wind
            samples[i] = (result * Short.MAX_VALUE).toInt().toShort()
        }
        
        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            numSamples * 2,
            AudioTrack.MODE_STATIC
        )
        
        val generatedSnd = ByteArray(2 * numSamples)
        var idx = 0
        for (i in 0 until numSamples) {
            val sample = samples[i]
            generatedSnd[idx++] = (sample and 0x00ff).toByte()
            generatedSnd[idx++] = ((sample and 0xff00) shr 8).toByte()
        }
        
        audioTrack.write(generatedSnd, 0, generatedSnd.size)
        audioTrack.play()
    }
}
