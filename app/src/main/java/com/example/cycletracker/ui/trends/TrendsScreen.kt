package com.example.cycletracker.ui.trends

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import kotlin.math.max
import kotlin.math.min
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendsScreen(
    viewModel: CycleViewModel,
    onNavigateBack: () -> Unit
) {
    val cycles by viewModel.cycles.collectAsState()
    val moodEntries by viewModel.getMoodEntries(
        LocalDate.now().minusDays(30),
        LocalDate.now()
    ).collectAsState(initial = emptyList())
    
    val trendsData = remember(cycles, moodEntries) {
        calculateTrendsData(cycles, moodEntries)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Анализ трендов",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F5FF)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Настроение за 30 дней
            TrendCard(
                title = "Настроение за 30 дней",
                icon = Icons.AutoMirrored.Filled.TrendingUp,
                color = Color(0xFFE91E63)
            ) {
                MoodTrendChart(
                    data = trendsData.moodTrend,
                    modifier = Modifier.height(200.dp)
                )
            }
            
            // Длина циклов
             TrendCard(
                 title = "Длина циклов",
                 icon = Icons.AutoMirrored.Filled.TrendingUp,
                 color = Color(0xFF9C27B0)
             ) {
                CycleLengthChart(
                    data = trendsData.cycleLengths,
                    modifier = Modifier.height(200.dp)
                )
            }
            
            // Энергия и сон
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TrendCard(
                     title = "Энергия",
                     icon = Icons.AutoMirrored.Filled.TrendingUp,
                     color = Color(0xFFFF9800),
                     modifier = Modifier.weight(1f)
                 ) {
                    EnergyChart(
                        data = trendsData.energyTrend,
                        modifier = Modifier.height(150.dp)
                    )
                }
                
                TrendCard(
                    title = "Сон",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                ) {
                    SleepChart(
                        data = trendsData.sleepTrend,
                        modifier = Modifier.height(150.dp)
                    )
                }
            }
            
            // Статистика
            TrendStatsCard(trendsData = trendsData)
        }
    }
}

@Composable
fun TrendCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun MoodTrendChart(
    data: List<MoodDataPoint>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Недостаточно данных",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "chart_animation"
    )
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 40.dp.toPx()
        
        val chartWidth = canvasWidth - 2 * padding
        val chartHeight = canvasHeight - 2 * padding
        
        val maxMood = data.maxOfOrNull { it.score }?.toFloat() ?: 10f
        val minMood = data.minOfOrNull { it.score }?.toFloat() ?: 1f
        val moodRange = maxMood - minMood
        
        val points = data.mapIndexed { index, point ->
            val x = padding + (index.toFloat() / (data.size - 1)) * chartWidth
            val y = padding + ((maxMood - point.score) / moodRange) * chartHeight
            Offset(x, y)
        }
        
        // Рисуем линию настроения
        if (points.isNotEmpty()) {
            val path = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { point ->
                    lineTo(point.x, point.y)
                }
            }
            drawPath(
                path = path,
                color = Color(0xFFE91E63),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        // Рисуем точки
        points.forEach { point ->
            drawCircle(
                color = Color(0xFFE91E63),
                radius = 6.dp.toPx(),
                center = point
            )
        }
        
        // Рисуем сетку
        drawGridLines(
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight,
            padding = padding,
            chartWidth = chartWidth,
            chartHeight = chartHeight,
            maxValue = maxMood,
            minValue = minMood
        )
    }
}

@Composable
fun CycleLengthChart(
    data: List<Int>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Недостаточно данных",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        return
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 40.dp.toPx()
        
        val chartWidth = canvasWidth - 2 * padding
        val chartHeight = canvasHeight - 2 * padding
        val barWidth = chartWidth / data.size
        
        val maxLength = data.maxOrNull() ?: 35
        val minLength = data.minOrNull() ?: 20
        
        data.forEachIndexed { index, length ->
            val barHeight = ((length - minLength).toFloat() / (maxLength - minLength)) * chartHeight
            val x = padding + index * barWidth
            val y = padding + chartHeight - barHeight
            
            drawRect(
                color = Color(0xFF9C27B0),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.8f, barHeight)
            )
        }
    }
}

@Composable
fun EnergyChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет данных",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        return
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 20.dp.toPx()
        
        val chartWidth = canvasWidth - 2 * padding
        val chartHeight = canvasHeight - 2 * padding
        val barWidth = chartWidth / data.size
        
        val maxEnergy = data.maxOrNull() ?: 10f
        
        data.forEachIndexed { index, energy ->
            val barHeight = (energy / maxEnergy) * chartHeight
            val x = padding + index * barWidth
            val y = padding + chartHeight - barHeight
            
            drawRect(
                color = Color(0xFFFF9800),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.8f, barHeight)
            )
        }
    }
}

@Composable
fun SleepChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Нет данных",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        return
    }
    
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val padding = 20.dp.toPx()
        
        val chartWidth = canvasWidth - 2 * padding
        val chartHeight = canvasHeight - 2 * padding
        val barWidth = chartWidth / data.size
        
        val maxSleep = data.maxOrNull() ?: 10f
        
        data.forEachIndexed { index, sleep ->
            val barHeight = (sleep / maxSleep) * chartHeight
            val x = padding + index * barWidth
            val y = padding + chartHeight - barHeight
            
            drawRect(
                color = Color(0xFF4CAF50),
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth * 0.8f, barHeight)
            )
        }
    }
}

@Composable
fun TrendStatsCard(trendsData: TrendsData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Статистика трендов",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Среднее настроение",
                    value = String.format("%.1f", trendsData.avgMood),
                    color = Color(0xFFE91E63)
                )
                StatItem(
                    label = "Средняя энергия",
                    value = String.format("%.1f", trendsData.avgEnergy),
                    color = Color(0xFFFF9800)
                )
                StatItem(
                    label = "Средний сон",
                    value = String.format("%.1f ч", trendsData.avgSleep),
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
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

private fun DrawScope.drawGridLines(
    canvasWidth: Float,
    canvasHeight: Float,
    padding: Float,
    chartWidth: Float,
    chartHeight: Float,
    maxValue: Float,
    minValue: Float
) {
    val gridColor = Color.Gray.copy(alpha = 0.3f)
    val strokeWidth = 1.dp.toPx()
    
    // Горизонтальные линии
    for (i in 0..4) {
        val y = padding + (i * chartHeight / 4)
        drawLine(
            color = gridColor,
            start = Offset(padding, y),
            end = Offset(padding + chartWidth, y),
            strokeWidth = strokeWidth
        )
    }
    
    // Вертикальные линии
    for (i in 0..6) {
        val x = padding + (i * chartWidth / 6)
        drawLine(
            color = gridColor,
            start = Offset(x, padding),
            end = Offset(x, padding + chartHeight),
            strokeWidth = strokeWidth
        )
    }
}

data class MoodDataPoint(
    val date: LocalDate,
    val score: Int
)

data class TrendsData(
    val moodTrend: List<MoodDataPoint>,
    val cycleLengths: List<Int>,
    val energyTrend: List<Float>,
    val sleepTrend: List<Float>,
    val avgMood: Float,
    val avgEnergy: Float,
    val avgSleep: Float
)

private fun calculateTrendsData(
    cycles: List<com.example.cycletracker.data.Cycle>,
    moodEntries: List<com.example.cycletracker.data.MoodEntry>
): TrendsData {
    val moodTrend = moodEntries.map { MoodDataPoint(it.date, it.score) }
        .sortedBy { it.date }
    
    val cycleLengths = cycles.mapNotNull { it.cycleLength }
    
    val energyTrend = moodEntries.mapNotNull { it.energy?.toFloat() }
    val sleepTrend = moodEntries.mapNotNull { it.sleepHours }
    
    return TrendsData(
        moodTrend = moodTrend,
        cycleLengths = cycleLengths,
        energyTrend = energyTrend,
        sleepTrend = sleepTrend,
        avgMood = moodEntries.map { it.score }.average().toFloat(),
        avgEnergy = energyTrend.average().toFloat(),
        avgSleep = sleepTrend.average().toFloat()
    )
}
