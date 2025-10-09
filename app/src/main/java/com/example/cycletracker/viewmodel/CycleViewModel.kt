package com.example.cycletracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class CycleViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(CycleUiState())
    val uiState: StateFlow<CycleUiState> = _uiState.asStateFlow()
    
    init {
        loadCycleData()
        generateAIInsights()
    }
    
    private fun loadCycleData() {
        viewModelScope.launch {
            // Симуляция загрузки данных
            val currentDate = LocalDate.now()
            val lastPeriod = currentDate.minusDays(15) // 15 дней назад
            val cycleLength = 28
            val currentDay = ChronoUnit.DAYS.between(lastPeriod, currentDate).toInt() % cycleLength + 1
            
            val phase = when {
                currentDay <= 5 -> "Менструация"
                currentDay <= 13 -> "Фолликулярная"
                currentDay <= 16 -> "Овуляция"
                else -> "Лютеиновая"
            }
            
            _uiState.value = _uiState.value.copy(
                currentCycleDay = currentDay,
                cycleLength = cycleLength,
                currentPhase = phase,
                lastPeriodDate = lastPeriod.toString(),
                nextPeriodDate = lastPeriod.plusDays(cycleLength.toLong()).toString()
            )
        }
    }
    
    private fun generateAIInsights() {
        viewModelScope.launch {
            val insights = listOf(
                "Ваш цикл стабилен в течение последних 3 месяцев",
                "Рекомендуется увеличить потребление воды в текущей фазе",
                "Уровень стресса может влиять на продолжительность цикла",
                "Физическая активность поможет улучшить самочувствие"
            )
            
            val activities = listOf(
                "Записано настроение: Хорошее",
                "Добавлен симптом: Легкая усталость",
                "Обновлен вес: 65.2 кг",
                "Записана физическая активность: 30 мин"
            )
            
            _uiState.value = _uiState.value.copy(
                aiInsights = insights,
                recentActivities = activities
            )
        }
    }
    
    fun addSymptom(symptom: String) {
        viewModelScope.launch {
            val newActivity = "Добавлен симптом: $symptom"
            val updatedActivities = listOf(newActivity) + _uiState.value.recentActivities.take(3)
            
            _uiState.value = _uiState.value.copy(
                recentActivities = updatedActivities
            )
        }
    }
    
    fun addMood(mood: String) {
        viewModelScope.launch {
            val newActivity = "Записано настроение: $mood"
            val updatedActivities = listOf(newActivity) + _uiState.value.recentActivities.take(3)
            
            _uiState.value = _uiState.value.copy(
                recentActivities = updatedActivities
            )
        }
    }
    
    fun recordPeriod() {
        viewModelScope.launch {
            val newActivity = "Начало менструации зафиксировано"
            val updatedActivities = listOf(newActivity) + _uiState.value.recentActivities.take(3)
            
            _uiState.value = _uiState.value.copy(
                recentActivities = updatedActivities,
                currentCycleDay = 1,
                currentPhase = "Менструация"
            )
        }
    }
    
    fun generateAIAnalysis() {
        viewModelScope.launch {
            // Симуляция AI анализа
            val analysis = listOf(
                "Анализ гормонального фона: В норме",
                "Прогноз овуляции: Через 3 дня",
                "Рекомендации по питанию: Увеличить железо",
                "Уровень стресса: Средний (7/10)"
            )
            
            _uiState.value = _uiState.value.copy(
                aiInsights = analysis
            )
        }
    }
}

data class CycleUiState(
    val currentCycleDay: Int = 1,
    val cycleLength: Int = 28,
    val currentPhase: String = "Менструация",
    val lastPeriodDate: String = "",
    val nextPeriodDate: String = "",
    val aiInsights: List<String> = emptyList(),
    val recentActivities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
