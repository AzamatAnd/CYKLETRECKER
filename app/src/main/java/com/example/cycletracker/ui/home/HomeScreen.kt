package com.example.cycletracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import com.example.cycletracker.ui.components.SwipeHandler
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PredictionCard(
    nextCycleDate: LocalDate,
    daysUntilNext: Int,
    avgCycleLength: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9C27B0)
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🔮 Прогноз следующего цикла",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Days until next cycle
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = when {
                        daysUntilNext < 0 -> "Просрочен на ${-daysUntilNext}"
                        daysUntilNext == 0 -> "Сегодня"
                        daysUntilNext == 1 -> "Завтра"
                        else -> "Через $daysUntilNext"
                    },
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when {
                        daysUntilNext == 1 -> "день"
                        daysUntilNext in 2..4 -> "дня"
                        else -> "дней"
                    },
                    fontSize = 18.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Ожидаемая дата: ${nextCycleDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))}",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Средняя длина цикла: $avgCycleLength дней",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun HomeScreen(
    viewModel: CycleViewModel,
    onNavigateToCalendar: () -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToAssistant: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMood: () -> Unit = {},
    onNavigateToMeds: () -> Unit = {},
    onNavigateToPhases: () -> Unit = {},
    onNavigateToTrends: () -> Unit = {},
    onNavigateToGoals: () -> Unit = {},
    onNavigateToBackup: () -> Unit = {}
) {
    val cycles by viewModel.cycles.collectAsState()
    val lastCycle = cycles.firstOrNull()
    
    // Calculate predictions
    val avgCycleLength = remember(cycles) {
        val completedCycles = cycles.filter { it.cycleLength != null }
        if (completedCycles.isNotEmpty()) {
            completedCycles.mapNotNull { it.cycleLength }.average().toInt()
        } else 28
    }
    
    val nextCycleDate = remember(lastCycle, avgCycleLength) {
        lastCycle?.startDate?.plusDays(avgCycleLength.toLong())
    }
    
    val daysUntilNext = remember(nextCycleDate) {
        nextCycleDate?.let { ChronoUnit.DAYS.between(LocalDate.now(), it).toInt() }
    }
    
    Scaffold(
        containerColor = Color(0xFFFFF0F5),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.startNewCycle() },
                containerColor = Color(0xFFE91E63)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Start Cycle",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        SwipeHandler(
            onSwipeLeft = onNavigateToCalendar,
            onSwipeRight = onNavigateToStatistics,
            onSwipeUp = onNavigateToTrends,
            onSwipeDown = onNavigateToSettings
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
            Text(
                text = "🌸 Женский календарь",
                fontSize = 32.sp,
                color = Color(0xFFE91E63),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Calendar button
            Button(
                onClick = onNavigateToCalendar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                )
            ) {
                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Календарь", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Statistics button
            Button(
                onClick = onNavigateToStatistics,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Статистика", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Phases button
            Button(
                onClick = onNavigateToPhases,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6F61)
                )
            ) {
                Icon(Icons.Default.Spa, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Фазы цикла", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Trends button
            Button(
                onClick = onNavigateToTrends,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Анализ трендов", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Goals button
            Button(
                onClick = onNavigateToGoals,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Мои цели", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Backup button
            Button(
                onClick = onNavigateToBackup,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Icon(Icons.Default.Backup, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Резервное копирование", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Notes button
            Button(
                onClick = onNavigateToNotes,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800)
                )
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Заметки и симптомы", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Mood button
            Button(
                onClick = onNavigateToMood,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00BCD4)
                )
            ) {
                Text("Дневник настроения", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Medications button
            Button(
                onClick = onNavigateToMeds,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF009688)
                )
            ) {
                Text("Лекарства", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Assistant button
            Button(
                onClick = onNavigateToAssistant,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            ) {
                Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("🤖 Умный помощник", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Settings button
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF607D8B)
                )
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Настройки уведомлений", color = Color.White, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Prediction card
            if (nextCycleDate != null && daysUntilNext != null) {
                PredictionCard(
                    nextCycleDate = nextCycleDate,
                    daysUntilNext = daysUntilNext,
                    avgCycleLength = avgCycleLength
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (lastCycle != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Последний цикл",
                            fontSize = 20.sp,
                            color = Color(0xFFE91E63)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Начало: ${lastCycle.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        if (lastCycle.endDate != null) {
                            Text(
                                text = "Конец: ${lastCycle.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Длительность: ${lastCycle.cycleLength} дней",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Циклы ещё не отслеживаются",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Нажмите кнопку + чтобы начать!",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}}
