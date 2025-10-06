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
    
    // Glass morphism properties (deprecated - use Glass object)
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
    
    // Legacy Navigation tokens (deprecated - use Navigation object below)
    val navigationBarHeight = 80.dp
    
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

    // Navigation tokens
    object Navigation {
        val barHeight = 80.dp
        val itemSize = 64.dp
        val iconSize = 24.dp
        val badgeSize = 20.dp
        val cornerRadius = 24.dp
        val indicatorHeight = 40.dp
        val indicatorWidth = 64.dp
    }

    // Navigation corner radius (backward compatibility)
    val navigationCornerRadius = Navigation.cornerRadius

    // Glass morphism tokens
    object Glass {
        const val lightAlpha = 0.8f
        const val darkAlpha = 0.6f
        const val blurRadius = 20f
        const val frostAlpha = 0.15f
        const val acrylicAlpha = 0.2f
    }

    // Neumorphism tokens
    object Neumorphism {
        val lightShadowAlpha = 0.8f
        val darkShadowAlpha = 0.15f
        val surfaceElevation = 4.dp
        val buttonElevation = 8.dp
        val cardElevation = 12.dp
        val clayIntensity = 0.8f
        val softDepth = 3
    }

    // Particle system tokens
    object Particles {
        const val defaultLife = 2f
        const val maxLife = 4f
        const val sparklesEmission = 15
        const val confettiEmission = 20
        const val fireworksEmission = 50
        const val snowEmission = 25
        const val rainEmission = 40
        const val maxParticles = 200
        const val gravity = 98f
        val sparkleSize = 2f..6f
        val confettiSize = 4f..12f
        val heartSize = 8f..16f
    }

    // Motion tokens
    object Motion {
        const val quickInteractionDuration = 200
        const val pageTransitionDuration = 300
        const val contentLoadingDuration = 500
        const val microInteractionDuration = 150
        const val scaleBounceMin = 0.9f
        const val scaleBounceMax = 1.1f
        const val physicsStiffness = 200f
        const val physicsDamping = 0.8f
        const val elasticStiffness = 300f
    }

    // Progress bar tokens
    object Progress {
        val heightSm = 4.dp
        val heightMd = 8.dp
        val heightLg = 12.dp
        val heightXl = 60.dp // For wave progress
        val cornerRadius = 4.dp
        val cycleIndicatorSize = 120.dp
        val milestoneSize = 8.dp
        val loadingDotSize = 8.dp
        val loadingDotCount = 3
    }

    // Backward compatibility variables
    val progressHeightMd = Progress.heightMd
    val progressCornerRadius = Progress.cornerRadius

    // Component specific tokens
    object Components {
        // Button tokens
        val buttonHeightSm = 40.dp
        val buttonHeightMd = 48.dp
        val buttonHeightLg = 56.dp
        val buttonHeightXl = 64.dp
        val buttonCornerRadius = 12.dp

        // Card tokens
        val cardCornerRadius = 16.dp
        val cardPadding = 16.dp
        val cardElevation = 4.dp

        // FAB tokens
        val fabSizeSm = 48.dp
        val fabSizeMd = 56.dp
        val fabSizeLg = 64.dp
        val fabSizeXl = 80.dp
        val fabElevation = 16.dp
        val fabCornerRadius = 20.dp

        // Chip tokens
        val chipHeight = 32.dp
        val chipPadding = 8.dp
        val chipCornerRadius = 16.dp

        // Text field tokens
        val textFieldCornerRadius = 12.dp
        val textFieldHeight = 56.dp

        // Switch tokens
        val switchWidth = 52.dp
        val switchHeight = 32.dp
        val switchThumbSize = 24.dp

        // Slider tokens
        val sliderHeight = 48.dp
        val sliderThumbSize = 20.dp
    }

    // Backward compatibility variables
    val chipCornerRadius = Components.chipCornerRadius

    // Effect tokens
    object Effects {
        // Pulse effect
        const val pulseMinScale = 0.9f
        const val pulseMaxScale = 1.1f
        const val pulseDuration = 800

        // Shake effect
        const val shakeIntensity = 8f
        const val shakeDuration = 300

        // Bounce effect
        const val bounceScale = 1.1f
        const val bounceDuration = 200

        // Glow effect
        const val glowIntensity = 1.2f
        const val glowDuration = 300

        // Elastic effect
        const val elasticDamping = 0.5f
        const val elasticStiffness = 250f
    }

    // Animation tokens
    object Animation {
        const val durationXs = 100
        const val durationSm = 200
        const val durationMd = 300
        const val durationLg = 500
        const val durationXl = 700

        // Easing curves
        const val standardDecelerateA = 0.0f
        const val standardDecelerateB = 0.0f
        const val standardDecelerateC = 0.0f
        const val standardDecelerateD = 1.0f

        const val emphasizedDecelerateA = 0.05f
        const val emphasizedDecelerateB = 0.7f
        const val emphasizedDecelerateC = 0.1f
        const val emphasizedDecelerateD = 1.0f

        const val emphasizedAccelerateA = 0.3f
        const val emphasizedAccelerateB = 0.0f
        const val emphasizedAccelerateC = 0.8f
        const val emphasizedAccelerateD = 0.15f

        const val bounceInP = 0.3f
        const val elasticOutP = 0.3f
    }

    // Accessibility tokens
    object Accessibility {
        const val reducedMotionScale = 0.6f
        const val minimalMotionScale = 0.3f
        const val accessibleDuration = 200
        val focusRingWidth = 2.dp
        const val highContrastRatio = 4.5f
    }

    // Performance tokens
    object Performance {
        const val maxParticlesHighEnd = 300
        const val maxParticlesLowEnd = 100
        const val particleUpdateInterval = 16L // ~60 FPS
        const val animationFrameRate = 60
    }
}