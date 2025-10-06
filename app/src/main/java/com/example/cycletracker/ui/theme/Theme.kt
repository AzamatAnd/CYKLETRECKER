package com.example.cycletracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF9C27B0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A148C),
    
    secondary = Color(0xFF7B1FA2),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE1BEE7),
    onSecondaryContainer = Color(0xFF4A148C),
    
    tertiary = Color(0xFFAB47BC),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFF3E5F5),
    onTertiaryContainer = Color(0xFF6A1B9A),
    
    background = Color(0xFFF8F6FA),
    onBackground = Color(0xFF1D1B20),
    
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = Color(0xFFF3E5F5),
    onSurfaceVariant = Color(0xFF49454F),
    
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFCD8DF),
    onErrorContainer = Color(0xFF690005),
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFC4C7C5),
    
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF303033),
    inverseOnSurface = Color(0xFFF5F5F5),
    inversePrimary = Color(0xFFD1C4E9),
    
    surfaceTint = Color(0xFF9C27B0)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD1C4E9),
    onPrimary = Color(0xFF4A148C),
    primaryContainer = Color(0xFF6A1B9A),
    onPrimaryContainer = Color(0xFFE1BEE7),
    
    secondary = Color(0xFFCE93D8),
    onSecondary = Color(0xFF4A148C),
    secondaryContainer = Color(0xFF7B1FA2),
    onSecondaryContainer = Color(0xFFE1BEE7),
    
    tertiary = Color(0xFFE1BEE7),
    onTertiary = Color(0xFF6A1B9A),
    tertiaryContainer = Color(0xFF9C27B0),
    onTertiaryContainer = Color(0xFFF3E5F5),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2D2D2D),
    onSurfaceVariant = Color(0xFFC4C7C5),
    
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color(0xFFFFFFFF),
    
    outline = Color(0xFF8E8E8E),
    outlineVariant = Color(0xFF444746),
    
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF2D2D2D),
    inversePrimary = Color(0xFF9C27B0),
    
    surfaceTint = Color(0xFFD1C4E9)
)

@Composable
fun CycleTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}