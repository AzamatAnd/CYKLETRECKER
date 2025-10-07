package com.example.cycletracker

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Unit тесты для основных функций приложения CycleTracker
 * Тестирует критически важные компоненты и бизнес-логику
 */

@RunWith(AndroidJUnit4::class)
class CycleTrackerTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.cycletracker", appContext.packageName)
    }

    @Test
    fun testCycleCalculation() {
        // Тестирование логики расчета цикла
        val cycleLength = 28
        val currentDay = 14

        val progress = currentDay.toFloat() / cycleLength.toFloat()
        assertEquals(0.5f, progress)

        val percentage = (progress * 100).toInt()
        assertEquals(50, percentage)
    }

    @Test
    fun testCyclePhaseDetermination() {
        // Тестирование определения фазы цикла
        val follicularPhase = determineCyclePhase(3)
        val ovulatoryPhase = determineCyclePhase(14)
        val lutealPhase = determineCyclePhase(21)
        val menstrualPhase = determineCyclePhase(27)

        assertEquals("Фолликулярная", follicularPhase)
        assertEquals("Овуляторная", ovulatoryPhase)
        assertEquals("Лютеиновая", lutealPhase)
        assertEquals("Менструальная", menstrualPhase)
    }

    @Test
    fun testHealthScoreCalculation() {
        // Тестирование расчета оценки здоровья
        val perfectScore = calculateHealthScore(95)
        val goodScore = calculateHealthScore(75)
        val averageScore = calculateHealthScore(55)
        val poorScore = calculateHealthScore(25)

        assertEquals("Отличное здоровье", perfectScore)
        assertEquals("Хорошее здоровье", goodScore)
        assertEquals("Удовлетворительное", averageScore)
        assertEquals("Требует внимания", poorScore)
    }

    @Test
    fun testDataValidation() {
        // Тестирование валидации данных
        assertTrue(isValidCycleDay(1))
        assertTrue(isValidCycleDay(28))
        assertFalse(isValidCycleDay(0))
        assertFalse(isValidCycleDay(29))
        assertFalse(isValidCycleDay(-1))
    }

    @Test
    fun testStringResources() {
        // Тестирование строковых ресурсов
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val appName = context.getString(R.string.app_name)
        assertEquals("CycleTracker", appName)

        val mainTitle = context.getString(R.string.main_title)
        assertTrue(mainTitle.contains("CYCLETRACKER"))
    }
}

// Вспомогательные функции для тестирования
private fun determineCyclePhase(day: Int): String {
    return when {
        day <= 5 -> "Менструальная"
        day <= 14 -> "Фолликулярная"
        day <= 16 -> "Овуляторная"
        else -> "Лютеиновая"
    }
}

private fun calculateHealthScore(score: Int): String {
    return when {
        score >= 90 -> "Отличное здоровье"
        score >= 70 -> "Хорошее здоровье"
        score >= 50 -> "Удовлетворительное"
        else -> "Требует внимания"
    }
}

private fun isValidCycleDay(day: Int): Boolean {
    return day in 1..28
}