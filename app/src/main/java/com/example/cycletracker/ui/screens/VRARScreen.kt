package com.example.cycletracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * VR/AR экран для 3D визуализации менструального цикла
 * Поддерживает иммерсивное представление данных здоровья в трехмерном пространстве
 */
@Composable
fun VRARScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 3D Visualization Header
        VRARHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // 3D Cycle Model Viewer
        Cycle3DModelViewer()

        Spacer(modifier = Modifier.height(16.dp))

        // AR Controls
        ARControlButtons()

        Spacer(modifier = Modifier.height(16.dp))

        // 3D Features List
        VRARFeaturesList()

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun VRARHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF6B35).copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🥽 VR/AR 3D ВИЗУАЛИЗАЦИЯ ЦИКЛА",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6B35),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ИММЕРСИВНОЕ ПРЕДСТАВЛЕНИЕ ЗДОРОВЬЯ",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun Cycle3DModelViewer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Simulated 3D Model
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFF6B35).copy(alpha = 0.8f),
                                    Color(0xFFFF6B35).copy(alpha = 0.2f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🧬",
                        fontSize = 48.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "3D МОДЕЛЬ ЦИКЛА",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Text(
                    text = "ФАЗА: ФОЛЛИКУЛЯРНАЯ",
                    fontSize = 12.sp,
                    color = Color(0xFFFF6B35),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ARControlButtons() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VRARButton(
                "📱 AR Режим",
                "Наложение на камеру",
                Color(0xFFFF6B35),
                modifier = Modifier.weight(1f)
            )
            VRARButton(
                "🥽 VR Режим",
                "Полная иммерсия",
                Color(0xFF8A2BE2),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VRARButton(
                "🔄 3D Вращение",
                "Интерактивный контроль",
                Color(0xFF14B8A6),
                modifier = Modifier.weight(1f)
            )
            VRARButton(
                "📊 Данные в 3D",
                "Визуализация показателей",
                Color(0xFF00FF88),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VRARButton(title: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .height(80.dp)
            .clickable { /* Handle VR/AR action */ },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun VRARFeaturesList() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "🚀 3D ВОЗМОЖНОСТИ 2025",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Feature3DItem("Голографическая проекция цикла")
            Feature3DItem("AR-наложение на тело в реальном времени")
            Feature3DItem("VR-прогулка внутри организма")
            Feature3DItem("Интерактивные гормоны и процессы")
            Feature3DItem("Пространственный анализ симптомов")
        }
    }
}

@Composable
private fun Feature3DItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color(0xFFFF6B35), CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFFCAC4D0)
        )
    }
}