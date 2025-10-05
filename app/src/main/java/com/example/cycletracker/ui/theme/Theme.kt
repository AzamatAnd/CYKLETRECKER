package com.example.cycletracker.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFE91E63),
    secondary = Color(0xFFF06292),
    tertiary = Color(0xFFFF9800)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF06292),
    secondary = Color(0xFFE91E63),
    tertiary = Color(0xFFFFB74D)
)

@Composable
fun CycleTrackerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}