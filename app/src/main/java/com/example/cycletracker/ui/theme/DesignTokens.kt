package com.example.cycletracker.ui.theme

import androidx.compose.ui.unit.dp

object DesignTokens {
    // Spacing
    val spacingXs = 4.dp
    val spacingSm = 8.dp
    val spacingMd = 16.dp
    val spacingLg = 24.dp
    val spacingXl = 32.dp
    val spacingXxl = 48.dp
    
    // Border radius
    val borderRadiusSm = 8.dp
    val borderRadiusMd = 16.dp
    val borderRadiusLg = 24.dp
    val borderRadiusXl = 32.dp
    val borderRadiusCircle = 50.dp
    
    // Elevation
    val elevationSm = 4.dp
    val elevationMd = 8.dp
    val elevationLg = 16.dp
    val elevationXl = 24.dp
    
    // Button sizes
    val buttonHeightSm = 40.dp
    val buttonHeightMd = 48.dp
    val buttonHeightLg = 56.dp
    val buttonHeightXl = 64.dp
    
    // Icon sizes
    val iconSizeSm = 16.dp
    val iconSizeMd = 24.dp
    val iconSizeLg = 32.dp
    val iconSizeXl = 48.dp
    
    // Card sizes
    val cardPaddingSm = 12.dp
    val cardPaddingMd = 16.dp
    val cardPaddingLg = 20.dp
    val cardPaddingXl = 24.dp
    
    // Animation durations
    const val animationDurationXs = 100
    const val animationDurationSm = 200
    const val animationDurationMd = 300
    const val animationDurationLg = 500
    const val animationDurationXl = 700
    
    // Glass morphism properties
    const val glassAlphaLight = 0.8f
    const val glassAlphaDark = 0.6f
    const val glassBlurRadius = 20f
    
    // Shadow properties
    val shadowOffset = 0.dp to 4.dp
    val shadowBlurRadius = 8.dp
    
    // Grid system
    val gridColumnCount = 12
    val gridGutterWidth = 16.dp
    
    // Aspect ratios
    const val aspectRatioSquare = 1f
    const val aspectRatioWide = 16f / 9f
    const val aspectRatioUltraWide = 21f / 9f
    
    // Component specific tokens
    object Navigation {
        val barHeight = 80.dp
        val itemSize = 64.dp
        val iconSize = 24.dp
        val badgeSize = 20.dp
        val cornerRadius = 24.dp
    }
    
    object Fab {
        val size = 64.dp
        val iconSize = 24.dp
        val elevation = 16.dp
        val cornerRadius = 20.dp
    }
    
    object Chip {
        val height = 32.dp
        val padding = 8.dp
        val cornerRadius = 16.dp
    }
    
    object Progress {
        val heightSm = 4.dp
        val heightMd = 8.dp
        val heightLg = 12.dp
        val cornerRadius = 4.dp
    }
    
    object Divider {
        val thickness = 1.dp
        val padding = 16.dp
    }
    
    object Avatar {
        val sizeSm = 32.dp
        val sizeMd = 48.dp
        val sizeLg = 64.dp
        val sizeXl = 96.dp
    }
    
    object Badge {
        val sizeSm = 16.dp
        val sizeMd = 20.dp
        val sizeLg = 24.dp
        val cornerRadius = 8.dp
    }
    
    // Responsive breakpoints
    object Breakpoints {
        val mobileSm = 320.dp
        val mobileMd = 375.dp
        val mobileLg = 428.dp
        val tablet = 768.dp
        val desktop = 1024.dp
    }
}