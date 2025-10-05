package com.example.cycletracker.ui.mood

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen(
    viewModel: CycleViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var score by remember { mutableStateOf(7f) }
    var note by remember { mutableStateOf("") }
    var energy by remember { mutableStateOf(7f) }
    var sleep by remember { mutableStateOf(8f) }

    var settings by remember { mutableStateOf<com.example.cycletracker.data.MoodSettings?>(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        settings = viewModel.getMoodSettings()
    }

    val gradient = Brush.verticalGradient(
        listOf(Color(0xFFFFE4E1), Color(0xFFFFF0F5))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Дневник настроения") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE91E63),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
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
            // Уникальная иконка-настроение с анимацией кривизны улыбки
            MoodIcon(score = score)

            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Дата: ${selectedDate}", fontWeight = FontWeight.Medium)
                    Text("Настроение: ${score.toInt()} / 10")
                    Slider(value = score, onValueChange = { score = it }, valueRange = 1f..10f)

                    Text("Энергия: ${energy.toInt()} / 10")
                    Slider(value = energy, onValueChange = { energy = it }, valueRange = 1f..10f)

                    Text("Сон: ${sleep.toInt()} ч")
                    Slider(value = sleep, onValueChange = { sleep = it }, valueRange = 0f..14f)

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Заметка") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Напоминания", fontWeight = FontWeight.Bold)
                    val enabled = settings?.reminderEnabled ?: false
                    var localEnabled by remember(enabled) { mutableStateOf(enabled) }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Напоминать отмечать настроение")
                        Switch(checked = localEnabled, onCheckedChange = {
                            localEnabled = it
                            val s = (settings ?: com.example.cycletracker.data.MoodSettings()).copy(reminderEnabled = it)
                            settings = s
                            scope.launch { viewModel.saveMoodSettings(s) }
                        })
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.saveMoodEntry(
                        date = selectedDate,
                        score = score.toInt(),
                        tagsCsv = "", // позже добавим редактирование тегов
                        note = note,
                        energy = energy.toInt(),
                        sleepHours = sleep
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
            ) {
                Text("Сохранить", color = Color.White)
            }
        }
    }
}

@Composable
private fun MoodIcon(score: Float) {
    val animated by remember { mutableStateOf(Animatable(score)) }
    LaunchedEffect(score) {
        animated.animateTo(score, animationSpec = tween(400))
    }
    val color = when {
        animated.value >= 7 -> Color(0xFF4CAF50)
        animated.value >= 4 -> Color(0xFFFFA000)
        else -> Color(0xFFF44336)
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF0F5)),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(64.dp)) {
                    // лицо
                    drawCircle(color = color.copy(alpha = 0.15f))
                    drawCircle(color = color, style = Stroke(width = 4f))
                    // глаза
                    drawCircle(color = color, radius = 4f, center = center.copy(x = center.x - 14f, y = center.y - 8f))
                    drawCircle(color = color, radius = 4f, center = center.copy(x = center.x + 14f, y = center.y - 8f))
                    // улыбка/грусть — дуга меняется по score
                    val k = (animated.value - 5f) / 5f // -0.8..+1.0
                    val y = center.y + (10f - 10f * k)
                    drawLine(color = color, start = center.copy(x = center.x - 16f, y = y), end = center.copy(x = center.x + 16f, y = y), strokeWidth = 4f)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Сегодняшнее настроение", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))
                Text("Выберите уровень по шкале и добавьте заметку", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}


