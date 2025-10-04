package com.example.cycletracker.ui.symptoms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddSymptomDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onSave: (String, Int?, String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf("") }
    var intensity by remember { mutableStateOf(5) }
    var note by remember { mutableStateOf("") }
    var showIntensity by remember { mutableStateOf(false) }
    
    val symptomTypes = listOf(
        "Боль в животе" to Icons.Default.LocalHospital,
        "Головная боль" to Icons.Default.Psychology,
        "Усталость" to Icons.Default.Bedtime,
        "Перепады настроения" to Icons.Default.Mood,
        "Вздутие" to Icons.Default.Water,
        "Тошнота" to Icons.Default.Sick,
        "Болезненность груди" to Icons.Default.Favorite,
        "Спазмы" to Icons.Default.Warning,
        "Другое" to Icons.Default.MoreHoriz
    )
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Добавить симптом",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                // Выбор типа симптома
                Text(
                    text = "Тип симптома",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(symptomTypes) { (type, icon) ->
                        SymptomTypeItem(
                            type = type,
                            icon = icon,
                            isSelected = selectedType == type,
                            onSelect = { selectedType = type }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Интенсивность
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Интенсивность:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "$intensity/10",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Slider(
                    value = intensity.toFloat(),
                    onValueChange = { intensity = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Заметка
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Заметка (необязательно)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Кнопки
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (selectedType.isNotEmpty()) {
                                onSave(selectedType, intensity, note.takeIf { it.isNotEmpty() })
                                onDismiss()
                            }
                        },
                        enabled = selectedType.isNotEmpty()
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomTypeItem(
    type: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelect
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = type,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            if (isSelected) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
