package com.example.cycletracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: CycleViewModel,
    onNavigateToCalendar: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    val cycles by viewModel.cycles.collectAsState()
    val lastCycle = cycles.firstOrNull()
    
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
            
            Spacer(modifier = Modifier.height(24.dp))
            
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
}