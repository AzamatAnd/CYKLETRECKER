package com.example.cycletracker.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(viewModel: CycleViewModel) {
    val cycles by viewModel.cycles.collectAsState()
    val lastCycle = cycles.firstOrNull()
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.startNewCycle() }) {
                Icon(Icons.Default.Add, contentDescription = "Start Cycle")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Cycle Tracker",
                style = MaterialTheme.typography.headlineLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            if (lastCycle != null) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Last Cycle",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Started: ${lastCycle.startDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (lastCycle.endDate != null) {
                            Text(
                                text = "Ended: ${lastCycle.endDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Length: ${lastCycle.cycleLength} days",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "No cycles tracked yet.\nTap + to start tracking!",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(32.dp)
                )
            }
        }
    }
}