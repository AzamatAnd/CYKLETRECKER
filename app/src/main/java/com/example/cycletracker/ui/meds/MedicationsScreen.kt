package com.example.cycletracker.ui.meds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.Medication
import com.example.cycletracker.data.MedicationIntake
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsScreen(
    viewModel: CycleViewModel,
    onNavigateBack: () -> Unit
) {
    val meds by viewModel.medications().collectAsState(initial = emptyList())
    val today = LocalDate.now()
    val intakes by viewModel.medicationIntakesByDate(today).collectAsState(initial = emptyList())

    var dialogOpen by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var times by remember { mutableStateOf("09:00,21:00") }

    val gradient = Brush.verticalGradient(listOf(Color(0xFFE0F7FA), Color(0xFFF1F8E9)))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Лекарства") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0097A7),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogOpen = true }, containerColor = Color(0xFF0097A7)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        },
        containerColor = Color.Transparent,
        modifier = Modifier.background(gradient)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Сегодняшние приёмы", color = Color(0xFF0097A7), style = MaterialTheme.typography.titleMedium)
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(intakes) { intake ->
                            MedicationIntakeRow(intake = intake, onToggle = {
                                viewModel.markMedicationIntake(intake.id, !intake.taken)
                            })
                        }
                    }
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Мои лекарства", color = Color(0xFF0097A7), style = MaterialTheme.typography.titleMedium)
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(meds) { med -> MedicationRow(med = med, onDelete = { viewModel.deleteMedication(med.id) }) }
                    }
                }
            }
        }
    }

    if (dialogOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen = false },
            title = { Text("Добавить лекарство") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Название") })
                    OutlinedTextField(value = dosage, onValueChange = { dosage = it }, label = { Text("Дозировка") })
                    OutlinedTextField(value = times, onValueChange = { times = it }, label = { Text("Время (через запятую)") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveMedication(Medication(name = name, dosage = dosage, timesCsv = times))
                    dialogOpen = false
                }) { Text("Сохранить") }
            },
            dismissButton = { TextButton(onClick = { dialogOpen = false }) { Text("Отмена") } }
        )
    }
}

@Composable
private fun MedicationRow(med: Medication, onDelete: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Text(med.name, style = MaterialTheme.typography.titleMedium)
            val subtitle = buildString {
                if (med.dosage.isNotBlank()) append(med.dosage).append(" • ")
                if (med.timesCsv.isNotBlank()) append(med.timesCsv)
            }
            if (subtitle.isNotBlank()) Text(subtitle, color = Color.Gray)
        }
        TextButton(onClick = onDelete) { Text("Удалить", color = Color(0xFFD32F2F)) }
    }
}

@Composable
private fun MedicationIntakeRow(intake: MedicationIntake, onToggle: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Text("${intake.time}")
            Text(if (intake.taken) "Принято" else "Не принято", color = if (intake.taken) Color(0xFF388E3C) else Color(0xFFF57C00))
        }
        Switch(checked = intake.taken, onCheckedChange = { onToggle() })
    }
}


