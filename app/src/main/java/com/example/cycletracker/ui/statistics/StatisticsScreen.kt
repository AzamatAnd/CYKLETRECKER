package com.example.cycletracker.ui.statistics

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import kotlinx.coroutines.launch
import java.time.Period
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    val cycles by viewModel.cycles.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var exportResult by remember { mutableStateOf<StatisticsExporter.ExportResult?>(null) }
    var showShareSheet by remember { mutableStateOf(false) }
    
    // Calculate statistics
    val stats = remember(cycles) {
        calculateStatistics(cycles)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    if (cycles.isNotEmpty()) {
                        IconButton(onClick = {
                            scope.launch {
                                val result = StatisticsExporter.export(context, cycles, stats)
                                exportResult = result
                                showShareSheet = true
                            }
                        }) {
                            Icon(Icons.Default.Download, contentDescription = "Сохранить PDF", tint = Color.White)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE91E63),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFFFF0F5)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (cycles.isEmpty()) {
                EmptyStateCard()
            } else {
                // Main stats cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Средний цикл",
                        value = stats.avgCycleLength?.toString() ?: "—",
                        unit = "дней",
                        gradient = Brush.linearGradient(
                            colors = listOf(Color(0xFFE91E63), Color(0xFFF06292))
                        ),
                        progress = (stats.avgCycleLength?.toFloat() ?: 0f) / 35f
                    )
                    
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Всего циклов",
                        value = stats.totalCycles.toString(),
                        unit = "отслежено",
                        gradient = Brush.linearGradient(
                            colors = listOf(Color(0xFF9C27B0), Color(0xFFBA68C8))
                        ),
                        progress = (stats.totalCycles.toFloat() / 12f).coerceAtMost(1f)
                    )
                }
                
                // Period length card
                AnimatedStatCard(
                    title = "Средняя длина периода",
                    value = stats.avgPeriodLength ?: 0,
                    maxValue = 7,
                    unit = "дней",
                    color = Color(0xFFE91E63)
                )
                
                // Tracking duration
                BigStatCard(
                    title = "📅 Отслеживание с",
                    value = stats.trackingDays.toString(),
                    subtitle = "дней данных",
                    gradient = Brush.linearGradient(
                        colors = listOf(Color(0xFFFF9800), Color(0xFFFFB74D))
                    )
                )
                
                // Cycle range
                if (stats.shortestCycle != null && stats.longestCycle != null) {
                    CycleRangeCard(
                        shortest = stats.shortestCycle,
                        longest = stats.longestCycle,
                        average = stats.avgCycleLength ?: 28
                    )
                }
                
                // Regularity indicator
                RegularityCard(
                    regularity = stats.regularity
                )
            }
        }
    }
    if (showShareSheet && exportResult != null) {
        LaunchedEffect(exportResult) {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, exportResult!!.uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Поделиться отчётом"))
            showShareSheet = false
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    gradient: Brush,
    progress: Float
) {
    var animatedProgress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(progress) {
        animatedProgress = progress
    }
    
    val animatedValue by animateFloatAsState(
        targetValue = animatedProgress,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "progress"
    )
    
    Card(
        modifier = modifier.height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Column {
                    Text(
                        text = value,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = unit,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
                
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedValue)
                            .fillMaxHeight()
                            .background(Color.White)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedStatCard(
    title: String,
    value: Int,
    maxValue: Int,
    unit: String,
    color: Color
) {
    var animatedValue by remember { mutableStateOf(0) }
    
    LaunchedEffect(value) {
        var current = 0
        while (current < value) {
            current++
            animatedValue = current
            kotlinx.coroutines.delay(50)
        }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = animatedValue.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            
            // Circular progress
            CircularProgressIndicator(
                value = animatedValue.toFloat() / maxValue,
                progress = value.toFloat() / maxValue,
                color = color
            )
        }
    }
}

@Composable
fun CircularProgressIndicator(
    value: Float,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "circular"
    )
    
    Box(
        modifier = modifier.size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(80.dp)) {
            val strokeWidth = 8.dp.toPx()
            
            // Background circle
            drawArc(
                color = Color.Gray.copy(alpha = 0.2f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
            
            // Progress arc
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
        
        Text(
            text = "${(animatedProgress * 100).roundToInt()}%",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun BigStatCard(
    title: String,
    value: String,
    subtitle: String,
    gradient: Brush
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.95f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = value,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
fun CycleRangeCard(
    shortest: Int,
    longest: Int,
    average: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Диапазон длины цикла",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                RangeItem("Короткий", shortest, Color(0xFF4CAF50))
                RangeItem("Средний", average, Color(0xFFE91E63))
                RangeItem("Длинный", longest, Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun RangeItem(label: String, value: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun RegularityCard(regularity: String) {
    val regularityRu = when (regularity) {
        "Excellent" -> "Отличная"
        "Good" -> "Хорошая"
        "Fair" -> "Средняя"
        "Variable" -> "Переменная"
        else -> "Недостаточно данных"
    }
    
    val (color, emoji) = when (regularity) {
        "Excellent" -> Color(0xFF4CAF50) to "🌟"
        "Good" -> Color(0xFF8BC34A) to "✅"
        "Fair" -> Color(0xFFFF9800) to "⚠️"
        else -> Color(0xFFE91E63) to "📊"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.copy(alpha = 0.1f))
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Регулярность цикла",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = regularityRu,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            
            Text(
                text = emoji,
                fontSize = 48.sp
            )
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊",
                fontSize = 64.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Пока нет данных",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Начните отслеживать циклы, чтобы увидеть статистику",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class CycleStatistics(
    val totalCycles: Int,
    val avgCycleLength: Int?,
    val avgPeriodLength: Int?,
    val shortestCycle: Int?,
    val longestCycle: Int?,
    val trackingDays: Long,
    val regularity: String
)

fun calculateStatistics(cycles: List<com.example.cycletracker.data.Cycle>): CycleStatistics {
    if (cycles.isEmpty()) {
        return CycleStatistics(0, null, null, null, null, 0, "Not enough data")
    }
    
    val completedCycles = cycles.filter { it.cycleLength != null }
    
    val avgCycleLength = if (completedCycles.isNotEmpty()) {
        completedCycles.mapNotNull { it.cycleLength }.average().roundToInt()
    } else null
    
    val shortestCycle = completedCycles.mapNotNull { it.cycleLength }.minOrNull()
    val longestCycle = completedCycles.mapNotNull { it.cycleLength }.maxOrNull()
    
    val trackingDays = if (cycles.isNotEmpty()) {
        ChronoUnit.DAYS.between(cycles.last().startDate, cycles.first().startDate)
    } else 0L
    
    val regularity = if (completedCycles.size >= 3) {
        val variance = completedCycles.mapNotNull { it.cycleLength }
            .map { (it - (avgCycleLength ?: 28)).toDouble() }
            .map { it * it }
            .average()
        
        when {
            variance < 4 -> "Excellent"
            variance < 9 -> "Good"
            variance < 16 -> "Fair"
            else -> "Variable"
        }
    } else "Not enough data"
    
    return CycleStatistics(
        totalCycles = cycles.size,
        avgCycleLength = avgCycleLength,
        avgPeriodLength = 5, // Default for now
        shortestCycle = shortestCycle,
        longestCycle = longestCycle,
        trackingDays = trackingDays,
        regularity = regularity
    )
}

fun Brush.copy(alpha: Float): Brush {
    return this
}
