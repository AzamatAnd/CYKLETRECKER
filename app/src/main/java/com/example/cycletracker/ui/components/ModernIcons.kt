package com.example.cycletracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Современные анимированные иконки для приложения 2025 года
 * Содержит инновационные визуальные эффекты и интерактивность
 */

// Пульсирующая неоновая иконка
@Composable
fun PulsingNeonIcon(
    icon: ImageVector,
    contentDescription: String?,
    color: Color,
    modifier: Modifier = Modifier
) {
    val pulseScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        pulseScale.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val glowIntensity = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        glowIntensity.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer(scaleX = pulseScale.value, scaleY = pulseScale.value)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f * glowIntensity.value)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(32.dp)
        )

        // Неоновое свечение
        Box(
            modifier = Modifier
                .size(52.dp)
                .graphicsLayer(scaleX = 1.1f, scaleY = 1.1f)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f * glowIntensity.value))
        )
    }
}

// Вращающаяся градиентная иконка
@Composable
fun RotatingGradientIcon(
    icon: ImageVector,
    contentDescription: String?,
    gradient: Brush,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer(rotationZ = rotation.value)
            .clip(CircleShape)
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

// Волновая анимация для иконки
@Composable
fun WaveEffectIcon(
    icon: ImageVector,
    contentDescription: String?,
    color: Color,
    modifier: Modifier = Modifier
) {
    val waveOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        waveOffset.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        // Основная иконка
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(32.dp)
        )

        // Волновые эффекты
        repeat(3) { index ->
            val delay = index * 200L
            val alpha = (1f - (index * 0.3f)) * (0.5f + 0.5f * waveOffset.value)

            LaunchedEffect(delay) {
                delay(delay)
            }

            Box(
                modifier = Modifier
                    .size(48.dp + (index * 8).dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = alpha * 0.3f))
            )
        }
    }
}

// Искрящаяся частица для иконки
@Composable
fun SparkleParticleIcon(
    icon: ImageVector,
    contentDescription: String?,
    primaryColor: Color,
    modifier: Modifier = Modifier
) {
    val sparkleAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        sparkleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = modifier.size(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = primaryColor,
            modifier = Modifier.size(32.dp)
        )

        // Искрящиеся частицы
        repeat(5) { index ->
            val angle = (index * 72f * sparkleAnimation.value)
            val radius = 20.dp + (sparkleAnimation.value * 10).dp
            val x = (radius * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp
            val y = (radius * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp

            val sparkleAlpha = sparkleAnimation.value * (1f - (index * 0.2f))

            Box(
                modifier = Modifier
                    .offset(x, y)
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = sparkleAlpha))
            )
        }
    }
}

// Анимированная кнопка-переключатель
@Composable
fun AnimatedToggleIcon(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconOn: ImageVector,
    iconOff: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val toggleScale = remember { Animatable(if (checked) 1.2f else 1f) }

    LaunchedEffect(checked) {
        toggleScale.animateTo(
            targetValue = if (checked) 1.2f else 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
    }

    val backgroundColor = if (checked) Color(0xFF00FF88).copy(alpha = 0.2f) else Color(0xFF6366F1).copy(alpha = 0.2f)
    val iconColor = if (checked) Color(0xFF00FF88) else Color(0xFF6366F1)

    Box(
        modifier = modifier
            .size(56.dp)
            .graphicsLayer(scaleX = toggleScale.value, scaleY = toggleScale.value)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (checked) iconOn else iconOff,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

// Кнопка с эффектом ряби
@Composable
fun RippleIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    color: Color = Color(0xFF6366F1),
    modifier: Modifier = Modifier
) {
    var rippleEffect by remember { mutableStateOf(false) }
    val rippleScale = remember { Animatable(0f) }

    LaunchedEffect(rippleEffect) {
        if (rippleEffect) {
            rippleScale.animateTo(
                targetValue = 2f,
                animationSpec = tween(600, easing = EaseOutCubic)
            )
            rippleScale.snapTo(0f)
            rippleEffect = false
        }
    }

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .clickable {
                rippleEffect = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon = icon,
            contentDescription = contentDescription,
            tint = color,
            modifier = Modifier.size(28.dp)
        )

        // Эффект ряби
        if (rippleEffect) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer(scaleX = rippleScale.value, scaleY = rippleScale.value)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.3f * (2f - rippleScale.value)))
            )
        }
    }
}

// Голографическая карточка с 3D эффектом
@Composable
fun HolographicCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val hologramOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        hologramOffset.animateTo(
            targetValue = 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Card(
        modifier = modifier.graphicsLayer(
            translationZ = hologramOffset.value,
            rotationX = hologramOffset.value * 0.1f
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6366F1).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp + (hologramOffset.value * 0.5f).dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1).copy(alpha = 0.2f + (hologramOffset.value * 0.01f)),
                            Color(0xFF14B8A6).copy(alpha = 0.1f + (hologramOffset.value * 0.005f)),
                            Color(0xFFFF006E).copy(alpha = 0.1f + (hologramOffset.value * 0.005f))
                        )
                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

// Киберпанк индикатор уровня с цифровым дисплеем
@Composable
fun CyberpunkLevelIndicator(
    level: Int,
    maxLevel: Int = 100,
    modifier: Modifier = Modifier
) {
    val levelProgress by animateFloatAsState(
        targetValue = level.toFloat() / maxLevel.toFloat(),
        animationSpec = tween(2000, easing = EaseOutCubic),
        label = "level"
    )

    val glitchEffect = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        glitchEffect.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(100, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "КИБЕР-УРОВЕНЬ",
                fontSize = 10.sp,
                color = Color(0xFFFF006E),
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Цифровой дисплей с глитч эффектом
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .graphicsLayer(
                        translationX = (glitchEffect.value - 0.5f) * 2f,
                        translationY = (glitchEffect.value - 0.5f) * 1f
                    )
                    .border(
                        width = 2.dp,
                        color = Color(0xFF00F5FF),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level.toString().padStart(3, '0'),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Прогресс бар в стиле киберпанк
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF0F0F23))
                    .clip(RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(levelProgress)
                        .height(4.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF006E),
                                    Color(0xFF00F5FF),
                                    Color(0xFF00FF88)
                                )
                            )
                        )
                )
            }
        }
    }
}

// Современная плазменная кнопка
@Composable
fun PlasmaButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF6366F1)
) {
    val plasmaAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        plasmaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val energyPulse = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        energyPulse.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Card(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer(scaleX = energyPulse.value, scaleY = energyPulse.value)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        color.copy(alpha = 0.8f * plasmaAnimation.value),
                        color.copy(alpha = 0.4f * (1f - plasmaAnimation.value)),
                        color.copy(alpha = 0.6f)
                    )
                )
            )
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp * energyPulse.value
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text.uppercase(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
        }
    }
}

// Квантовый загрузчик с орбитальными частицами
@Composable
fun QuantumLoader(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF6366F1)
) {
    if (!isLoading) return

    val particleAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        particleAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = modifier.size(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // Орбитальные частицы
        repeat(5) { index ->
            val angle = (index * 72 * particleAnimation.value).toFloat()
            val radius = 25.dp
            val x = (radius * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp
            val y = (radius * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp

            Box(
                modifier = Modifier
                    .offset(x, y)
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }

        // Центральный квантовый эффект
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.8f),
                            color.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}

// Нейронная сеть визуализация для аналитики
@Composable
fun NeuralNetworkVisualization(
    modifier: Modifier = Modifier,
    nodeCount: Int = 8
) {
    val networkAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        networkAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier.size(200.dp)) {
        // Нейронные узлы
        repeat(nodeCount) { index ->
            val angle = (index * 360f / nodeCount)
            val radius = 80.dp
            val x = (radius * kotlin.math.cos(Math.toRadians(angle.toDouble()))).dp
            val y = (radius * kotlin.math.sin(Math.toRadians(angle.toDouble()))).dp

            val nodeScale = if (index == (networkAnimation.value * nodeCount).toInt() % nodeCount) {
                1.3f
            } else {
                1f
            }

            Box(
                modifier = Modifier
                    .offset(x, y)
                    .size(12.dp)
                    .graphicsLayer(scaleX = nodeScale, scaleY = nodeScale)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFF14B8A6)
                            )
                        )
                    )
            )
        }

        // Связи между узлами
        for (i in 0 until nodeCount) {
            for (j in i + 1 until nodeCount) {
                val angle1 = (i * 360f / nodeCount)
                val angle2 = (j * 360f / nodeCount)
                val radius = 80.dp

                val startX = (radius * kotlin.math.cos(Math.toRadians(angle1.toDouble()))).dp
                val startY = (radius * kotlin.math.sin(Math.toRadians(angle1.toDouble()))).dp
                val endX = (radius * kotlin.math.cos(Math.toRadians(angle2.toDouble()))).dp
                val endY = (radius * kotlin.math.sin(Math.toRadians(angle2.toDouble()))).dp

                val connectionAlpha = if (
                    i == (networkAnimation.value * nodeCount).toInt() % nodeCount ||
                    j == (networkAnimation.value * nodeCount).toInt() % nodeCount
                ) {
                    0.8f
                } else {
                    0.2f
                }

                Box(
                    modifier = Modifier
                        .offset(startX, startY)
                        .width(100.dp)
                        .height(1.dp)
                        .background(Color(0xFF00F5FF).copy(alpha = connectionAlpha))
                )
            }
        }
    }
}

// Матричный дождь эффект для фона
@Composable
fun MatrixRainBackground(
    modifier: Modifier = Modifier
) {
    val matrixAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        matrixAnimation.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(100, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Симуляция матричного дождя
        repeat(20) {
            val x = (it * 50).dp
            val offsetY = ((matrixAnimation.value * 1000 + it * 100) % 1000).dp

            Column(modifier = Modifier.offset(x = x, y = -offsetY)) {
                repeat(20) { row ->
                    Text(
                        text = listOf("0", "1", "あ", "ア", "イ").random(),
                        fontSize = 12.sp,
                        color = Color(0xFF00FF88).copy(alpha = 0.6f),
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}