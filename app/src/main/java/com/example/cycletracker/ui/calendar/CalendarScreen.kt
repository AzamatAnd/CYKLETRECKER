package com.example.cycletracker.ui.calendar

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Calendar") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
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
                .padding(16.dp)
        ) {
            // Month navigation
            MonthNavigationBar(
                currentMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Calendar grid
            CalendarGrid(
                currentMonth = currentMonth,
                viewModel = viewModel
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Legend
            CalendarLegend()
        }
    }
}

@Composable
fun MonthNavigationBar(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousMonth) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Previous",
                    tint = Color(0xFFE91E63)
                )
            }
            
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            IconButton(onClick = onNextMonth) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = "Next",
                    tint = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: YearMonth,
    viewModel: CycleViewModel
) {
    val cycles by viewModel.cycles.collectAsState()
    val periodDates = remember(cycles) {
        cycles.flatMap { cycle ->
            val dates = mutableListOf<LocalDate>()
            var date = cycle.startDate
            val endDate = cycle.endDate ?: cycle.startDate.plusDays(5)
            while (!date.isAfter(endDate)) {
                dates.add(date)
                date = date.plusDays(1)
            }
            dates
        }.toSet()
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Week day headers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Calendar days
            val firstDayOfMonth = currentMonth.atDay(1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value - 1
            val daysInMonth = currentMonth.lengthOfMonth()
            
            var dayCounter = 1 - firstDayOfWeek
            
            repeat(6) { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) { dayOfWeek ->
                        if (dayCounter in 1..daysInMonth) {
                            val date = currentMonth.atDay(dayCounter)
                            DayCell(
                                date = date,
                                isPeriod = periodDates.contains(date),
                                isToday = date == LocalDate.now(),
                                onClick = { viewModel.togglePeriodDay(date) }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                        dayCounter++
                    }
                }
                if (week < 5) Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    isPeriod: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    
    val backgroundColor = when {
        isPeriod -> Brush.radialGradient(
            colors = listOf(Color(0xFFFF6B9D), Color(0xFFE91E63))
        )
        else -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFE4E1))
        )
    }
    
    Box(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f)
            .padding(4.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 16.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (isPeriod) Color.White else Color.Black
        )
        
        if (isToday) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(Color.Transparent)
                    .then(
                        Modifier.background(
                            brush = Brush.radialGradient(
                                colors = listOf(Color.Transparent, Color(0xFF4CAF50).copy(alpha = 0.3f))
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun CalendarLegend() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Legend",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LegendItem(
                color = Color(0xFFE91E63),
                label = "Period day"
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LegendItem(
                color = Color(0xFF4CAF50),
                label = "Today (green glow)"
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
