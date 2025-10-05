package com.example.cycletracker.ui.calendar

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Календарь") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
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
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Previous",
                    tint = Color(0xFFE91E63)
                )
            }
            
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )
            
            IconButton(onClick = onNextMonth) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
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
    val calendarMarks = remember(cycles) { buildCalendarMarks(cycles) }
    val periodDates = calendarMarks.periodDates
    val fertileDates = calendarMarks.fertileDates
    val ovulationDates = calendarMarks.ovulationDates
    
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
                listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                    Text(
                        text = day,
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val dayCells = remember(currentMonth) { buildDayCells(currentMonth) }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier.fillMaxWidth(),
                userScrollEnabled = false,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(dayCells) { maybeDate ->
                    if (maybeDate != null) {
                        DayCell(
                            date = maybeDate,
                            isPeriod = periodDates.contains(maybeDate),
                            isFertile = fertileDates.contains(maybeDate) && !periodDates.contains(maybeDate),
                            isOvulation = ovulationDates.contains(maybeDate),
                            isToday = maybeDate == LocalDate.now(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(4.dp),
                            onClick = { viewModel.togglePeriodDay(maybeDate) }
                        )
                    } else {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class CalendarMarks(
    val periodDates: Set<LocalDate>,
    val fertileDates: Set<LocalDate>,
    val ovulationDates: Set<LocalDate>
)

private fun buildCalendarMarks(cycles: List<com.example.cycletracker.data.Cycle>): CalendarMarks {
    if (cycles.isEmpty()) {
        return CalendarMarks(emptySet(), emptySet(), emptySet())
    }

    val periodDates = cycles.flatMap { cycle ->
        val dates = mutableListOf<LocalDate>()
        var date = cycle.startDate
        val endDate = cycle.endDate ?: cycle.startDate.plusDays(5)
        while (!date.isAfter(endDate)) {
            dates.add(date)
            date = date.plusDays(1)
        }
        dates
    }.toMutableSet()

    val recentCycles = cycles.take(6)
    val averageCycleLength = recentCycles.mapNotNull { it.cycleLength }
        .takeIf { it.isNotEmpty() }
        ?.average()?.toInt() ?: 28

    val latestCycle = cycles.first()
    val baseCycleLength = latestCycle.cycleLength ?: averageCycleLength
    val ovulationOffset = (baseCycleLength - 14).coerceIn(10, baseCycleLength)
    val ovulationDate = latestCycle.startDate.plusDays(ovulationOffset.toLong())

    val ovulationDates = mutableSetOf<LocalDate>()
    val fertileDates = mutableSetOf<LocalDate>()

    if (!periodDates.contains(ovulationDate)) {
        ovulationDates.add(ovulationDate)
    }

    val fertileWindowStart = ovulationDate.minusDays(4)
    val fertileWindowEnd = ovulationDate.plusDays(1)
    var current = fertileWindowStart
    while (!current.isAfter(fertileWindowEnd)) {
        if (!periodDates.contains(current)) {
            fertileDates.add(current)
        }
        current = current.plusDays(1)
    }

    return CalendarMarks(periodDates, fertileDates, ovulationDates)
}

private fun buildDayCells(month: YearMonth): List<LocalDate?> {
    val firstDay = month.atDay(1)
    val offset = (firstDay.dayOfWeek.value + 6) % 7 // convert Monday=0
    val totalDays = month.lengthOfMonth()

    val cells = MutableList(offset) { null as LocalDate? }
    for (day in 1..totalDays) {
        cells.add(month.atDay(day))
    }
    while (cells.size % 7 != 0) {
        cells.add(null)
    }
    return cells
}

@Composable
fun DayCell(
    date: LocalDate,
    isPeriod: Boolean,
    isFertile: Boolean,
    isOvulation: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    val backgroundBrush = when {
        isPeriod -> Brush.radialGradient(
            colors = listOf(Color(0xFFFF6B9D), Color(0xFFE91E63))
        )
        isOvulation -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF176), Color(0xFFFFC107))
        )
        isFertile -> Brush.radialGradient(
            colors = listOf(Color(0xFFDCEDC8), Color(0xFFA5D6A7))
        )
        else -> Brush.radialGradient(
            colors = listOf(Color(0xFFFFF0F5), Color(0xFFFFE4E1))
        )
    }
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(backgroundBrush)
            .clickable {
                isPressed = true
                onClick()
                isPressed = false
            },
        contentAlignment = Alignment.Center
    ) {
        val baseTextColor = when {
            isPeriod -> Color.White
            isOvulation -> Color(0xFF3E2723)
            isFertile -> Color(0xFF1B5E20)
            else -> Color.Black
        }

        Text(
            text = date.dayOfMonth.toString(),
            fontSize = 16.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = baseTextColor
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
                text = "Легенда",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LegendItem(
                color = Color(0xFFE91E63),
                label = "День менструации"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LegendItem(
                color = Color(0xFFA5D6A7),
                label = "Фертильное окно"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LegendItem(
                color = Color(0xFFFFC107),
                label = "Овуляция"
            )

            Spacer(modifier = Modifier.height(8.dp))

            LegendItem(
                color = Color(0xFF4CAF50),
                label = "Сегодня (зелёное свечение)"
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
