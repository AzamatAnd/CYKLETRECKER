package com.example.cycletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import android.view.MotionEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: CycleViewModel = viewModel()
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF8F6FA)
                ) {
                    var currentScreen by remember { mutableStateOf("home") }

                    Scaffold(
                        bottomBar = {
                            androidx.compose.material3.NavigationBar(
                                modifier = Modifier,
                                containerColor = Color(0xFF16213E)
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == "home",
                                    onClick = { currentScreen = "home" },
                                    icon = { Icon(Icons.Default.Home, "Главная") },
                                    label = { Text("Главная") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == "calendar",
                                    onClick = { currentScreen = "calendar" },
                                    icon = { Icon(Icons.Default.DateRange, "Календарь") },
                                    label = { Text("Календарь") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == "analytics",
                                    onClick = { currentScreen = "analytics" },
                                    icon = { Icon(Icons.Default.Analytics, "Аналитика") },
                                    label = { Text("Аналитика") }
                                )
                                NavigationBarItem(
                                    selected = currentScreen == "settings",
                                    onClick = { currentScreen = "settings" },
                                    icon = { Icon(Icons.Default.Settings, "Настройки") },
                                    label = { Text("Настройки") }
                                )
                            }
                        }
                    ) { padding ->
                        when (currentScreen) {
                            "home" -> EnhancedHomeScreen(modifier = Modifier.padding(padding), viewModel = viewModel)
                            "calendar" -> SimpleCalendarScreen(modifier = Modifier.padding(padding))
                            "analytics" -> SimpleAnalyticsScreen(modifier = Modifier.padding(padding))
                            "settings" -> SimpleSettingsScreen(modifier = Modifier.padding(padding))
                            else -> EnhancedHomeScreen(modifier = Modifier.padding(padding), viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EnhancedHomeScreen(modifier: Modifier = Modifier, viewModel: CycleViewModel) {
    val scrollState = rememberScrollState()
    val currentCycleDay = remember { mutableStateOf(14) }
    val cycleLength = remember { mutableStateOf(28) }

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

        // Ultra Modern Header with Glass Effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x806366F1),
                            Color(0x6014B8A6)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✨ CYCLETRACKER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = Shadow(
                            color = Color(0xFF6366F1),
                            offset = Offset(0f, 0f),
                            blurRadius = 8f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ULTRA MODERN HEALTH AI",
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ultra Modern Cycle Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🧬 ТЕКУЩАЯ ФАЗА ЦИКЛА",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Modern Progress Ring (Simplified)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF16213E)),
                    contentAlignment = Alignment.Center
                ) {
                    val progress = currentCycleDay.value.toFloat() / cycleLength.value.toFloat()

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "День ${currentCycleDay.value}",
                            fontSize = 12.sp,
                            color = Color(0xFF00F5FF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phase indicator with neon glow
                val phase = when {
                    currentCycleDay.value <= 5 -> "МЕНСТРУАЦИЯ"
                    currentCycleDay.value <= 14 -> "ФОЛЛИКУЛЯРНАЯ"
                    currentCycleDay.value <= 16 -> "ОВУЛЯЦИЯ"
                    else -> "ЛЮТЕИНОВАЯ"
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "⚡ $phase",
                        fontSize = 12.sp,
                        color = Color(0xFFFF006E),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Ultra Modern Quick Actions
        Text(
            text = "⚡ QUICK ACTIONS",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00F5FF),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UltraModernQuickActionButton(
                "📅 Календарь",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6366F1).copy(alpha = 0.8f),
                        Color(0xFF14B8A6).copy(alpha = 0.6f)
                    )
                )
            )
            UltraModernQuickActionButton(
                "📊 Аналитика",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF006E).copy(alpha = 0.8f),
                        Color(0xFFE91E63).copy(alpha = 0.6f)
                    )
                )
            )
            UltraModernQuickActionButton(
                "⚙️ Настройки",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00F5FF).copy(alpha = 0.8f),
                        Color(0xFF6366F1).copy(alpha = 0.6f)
                    )
                )
            )
        }

        // Ultra Modern Today's Insights
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "📊",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "СЕГОДНЯШНИЕ ПОКАЗАТЕЛИ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5FF),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                UltraModernInsightRow("Настроение", "8/10", Color(0xFF00FF88))
                UltraModernInsightRow("Энергия", "7/10", Color(0xFFFFAA00))
                UltraModernInsightRow("Боль", "2/10", Color(0xFF00FF88))
            }
        }

        // Ultra Modern Recent Activities
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "📝",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "НЕДАВНИЕ ЗАПИСИ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF006E),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                UltraModernActivityItem("Прием витаминов D", "09:00", "💊")
                UltraModernActivityItem("Запись настроения", "14:30", "😊")
                UltraModernActivityItem("Легкая усталость", "16:45", "😴")
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}


@Composable
private fun UltraModernQuickActionButton(text: String, gradient: Brush) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6366F1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text.split(" ")[0],
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text.split(" ")[1],
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun UltraModernInsightRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            color = Color(0xFFCAC4D0),
            letterSpacing = 1.sp
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color.copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun UltraModernActivityItem(title: String, time: String, icon: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6366F1)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE6E1E5),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = time,
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Medium
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00FF88).copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "✓",
                    fontSize = 10.sp,
                    color = Color(0xFF00FF88),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun SimpleCalendarScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📅 Календарь цикла",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Октябрь 2024", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Функция календаря будет реализована в следующей версии",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SimpleAnalyticsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📊 Аналитика здоровья",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Статистика цикла", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Подробная аналитика будет доступна в полной версии приложения",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SimpleSettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚙️ Настройки",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Настройки приложения", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Персонализация, уведомления и другие настройки будут доступны в полной версии",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}