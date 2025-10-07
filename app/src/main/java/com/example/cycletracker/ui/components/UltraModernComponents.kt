package com.example.cycletracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Ультра современные компоненты для приложения 2025 года
 * Содержит продвинутые анимации и визуальные эффекты
 */

// Голографическая карточка с эффектом глубины
@Composable
fun HolographicCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val hologramEffect = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        hologramEffect.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Card(
        modifier = modifier.graphicsLayer(
            translationZ = hologramEffect.value * 10f
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6366F1).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp + (hologramEffect.value * 4).dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6366F1).copy(alpha = 0.2f * hologramEffect.value),
                            Color(0xFF14B8A6).copy(alpha = 0.1f * hologramEffect.value),
                            Color(0xFFFF006E).copy(alpha = 0.1f * hologramEffect.value)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            content()
        }
    }
}

// Неоновая кнопка с эффектом свечения
@Composable
fun NeonGlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF00F5FF)
) {
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
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        color.copy(alpha = 0.8f),
                        color.copy(alpha = 0.4f),
                        color.copy(alpha = 0.8f)
                    )
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        color.copy(alpha = glowIntensity.value),
                        color.copy(alpha = glowIntensity.value * 0.5f),
                        color.copy(alpha = glowIntensity.value)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
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

// Плазменный индикатор с энергетическими волнами
@Composable
fun PlasmaEnergyIndicator(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val energyWave = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        energyWave.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(2500, easing = EaseInOutCubic),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color.copy(alpha = 0.4f + (energyWave.value * 0.3f)),
                            color.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = color,
                    letterSpacing = 1.sp
                )

                // Энергетическая волна
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    color.copy(alpha = energyWave.value),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

// Квантовый загрузчик с эффектом частиц
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

// Гиперпространственный переход между экранами
@Composable
fun HyperspaceTransition(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    val hyperspaceEffect = remember { Animatable(0f) }

    LaunchedEffect(visible) {
        if (visible) {
            hyperspaceEffect.animateTo(
                targetValue = 1f,
                animationSpec = tween(1000, easing = EaseInOutCubic)
            )
            delay(2000)
            hyperspaceEffect.animateTo(
                targetValue = 0f,
                animationSpec = tween(1000, easing = EaseInOutCubic)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = hyperspaceEffect.value * 0.8f))
    ) {
        // Звездное поле
        repeat(50) {
            val x = (0..1000).random().dp
            val y = (0..1000).random().dp

            Box(
                modifier = Modifier
                    .offset(x, y)
                    .size(2.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = hyperspaceEffect.value))
            )
        }

        // Гиперпространственные линии
        repeat(10) {
            val startX = (0..1000).random().dp
            val startY = (0..1000).random().dp

            Box(
                modifier = Modifier
                    .offset(startX, startY)
                    .width(100.dp)
                    .height(2.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF00F5FF).copy(alpha = 0f),
                                Color(0xFF00F5FF).copy(alpha = hyperspaceEffect.value),
                                Color(0xFF00F5FF).copy(alpha = 0f)
                            )
                        )
                    )
            )
        }
    }
}

// Нейронная сеть визуализация
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
                        .width(100.dp) // Simplified connection
                        .height(1.dp)
                        .background(Color(0xFF00F5FF).copy(alpha = connectionAlpha))
                )
            }
        }
    }
}

// Киберпанк индикатор уровня
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

            // Цифровой дисплей
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
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
