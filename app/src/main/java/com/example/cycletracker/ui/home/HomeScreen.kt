package com.example.cycletracker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun HomeScreen(viewModel: CycleViewModel) {
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
                text = "🌸 Cycle Tracker",
                fontSize = 32.sp,
                color = Color(0xFFE91E63),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
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
                            text = "Last Cycle",
                            fontSize = 20.sp,
                            color = Color(0xFFE91E63)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Started: ${lastCycle.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        if (lastCycle.endDate != null) {
                            Text(
                                text = "Ended: ${lastCycle.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Length: ${lastCycle.cycleLength} days",
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
                            text = "No cycles tracked yet",
                            fontSize = 18.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap + button to start tracking!",
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