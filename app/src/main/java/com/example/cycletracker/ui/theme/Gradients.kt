package com.example.cycletracker.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradients {
    // Primary gradients
    val primaryGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF9C27B0),
            Color(0xFFBA68C8),
            Color(0xFFE1BEE7)
        )
    )
    
    val secondaryGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF7B1FA2),
            Color(0xFF9575CD),
            Color(0xFFD1C4E9)
        )
    )
    
    val tertiaryGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFAB47BC),
            Color(0xFFCE93D8),
            Color(0xFFF3E5F5)
        )
    )
    
    // Feature-specific gradients
    val calendarGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF6F61),
            Color(0xFFFF8A65),
            Color(0xFFFFAB91)
        )
    )
    
    val statisticsGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A),
            Color(0xFF8E24AA),
            Color(0xFFBA68C8)
        )
    )
    
    val moodGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF00BCD4),
            Color(0xFF26C6DA),
            Color(0xFF80DEEA)
        )
    )
    
    val notesGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF9800),
            Color(0xFFFFA726),
            Color(0xFFFFCC80)
        )
    )
    
    val medicationGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF009688),
            Color(0xFF26A69A),
            Color(0xFF80CBC4)
        )
    )
    
    val goalsGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF2E7D32),
            Color(0xFF43A047),
            Color(0xFF81C784)
        )
    )
    
    val assistantGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A),
            Color(0xFF8E24AA),
            Color(0xFFBA68C8)
        )
    )
    
    val backupGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1976D2),
            Color(0xFF2196F3),
            Color(0xFF64B5F6)
        )
    )
    
    val phasesGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFF6F61),
            Color(0xFFFF8A65),
            Color(0xFFFFAB91)
        )
    )
    
    val trendsGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF6A1B9A),
            Color(0xFF8E24AA),
            Color(0xFFBA68C8)
        )
    )
    
    // Glass morphism effects
    val glassLight = Brush.verticalGradient(
        colors = listOf(
            Color(0x66FFFFFF),
            Color(0x33FFFFFF)
        )
    )
    
    val glassDark = Brush.verticalGradient(
        colors = listOf(
            Color(0x66121212),
            Color(0x33121212)
        )
    )
    
    // Background gradients
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF8F6FA),
            Color(0xFFF3E5F5),
            Color(0xFFE1BEE7)
        )
    )
    
    val darkBackgroundGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF121212),
            Color(0xFF1E1E1E),
            Color(0xFF2D2D2D)
        )
    )
    
    // Success/Warning/Error gradients
    val successGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF4CAF50),
            Color(0xFF66BB6A),
            Color(0xFF81C784)
        )
    )
    
    val warningGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFFFC107),
            Color(0xFFFFCA28),
            Color(0xFFFFD54F)
        )
    )
    
    val errorGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFB00020),
            Color(0xFFC62828),
            Color(0xFFD32F2F)
        )
    )
    
    // Get gradient by feature name
    fun getGradientForFeature(feature: String): Brush {
        return when (feature.lowercase()) {
            "calendar", "календарь" -> calendarGradient
            "statistics", "статистика" -> statisticsGradient
            "mood", "настроение" -> moodGradient
            "notes", "заметки" -> notesGradient
            "medication", "лекарства" -> medicationGradient
            "goals", "цели" -> goalsGradient
            "assistant", "помощник" -> assistantGradient
            "backup", "бэкап" -> backupGradient
            "phases", "фазы" -> phasesGradient
            "trends", "тренды" -> trendsGradient
            else -> primaryGradient
        }
    }
}