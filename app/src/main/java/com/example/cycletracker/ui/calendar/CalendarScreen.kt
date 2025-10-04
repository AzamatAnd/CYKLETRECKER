package com.example.cycletracker.ui.calendar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.CycleEntity
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CalendarScreen(
	cycles: List<CycleEntity>,
	averageCycleDays: Int,
	onDateClick: (LocalDate) -> Unit,
	onAddSymptom: (LocalDate) -> Unit = {},
	modifier: Modifier = Modifier
) {
	var currentMonth by remember { mutableStateOf(YearMonth.now()) }
	val days = remember(currentMonth) { generateMonthDays(currentMonth) }

	val lastCycleStart = cycles.maxByOrNull { it.startDate }?.startDate
	val predictedPeriod = remember(lastCycleStart, averageCycleDays) {
		if (lastCycleStart != null) {
			val nextStart = lastCycleStart.plusDays(averageCycleDays.toLong())
			(nextStart..nextStart.plusDays(5))
		} else emptyList()
	}

	Column(modifier = modifier.padding(16.dp)) {
		Header(currentMonth = currentMonth, onPrev = { currentMonth = currentMonth.minusMonths(1) }, onNext = { currentMonth = currentMonth.plusMonths(1) })
		Spacer(Modifier.height(8.dp))
		WeekDaysRow()
		Spacer(Modifier.height(4.dp))
		AnimatedContent(
			targetState = days,
			transitionSpec = { fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200)) },
			label = "monthSwitch"
		) { daysState ->
			for (week in daysState.chunked(7)) {
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
					for (day in week) {
						val isInCycle = day?.let { date ->
							cycles.any { cycle ->
								cycle.endDate?.let { endDate ->
									!date.isBefore(cycle.startDate) && !date.isAfter(endDate)
								} ?: (!date.isBefore(cycle.startDate) && date.isBefore(cycle.startDate.plusDays(7)))
							}
						} ?: false
						
						val isCycleStart = day?.let { date ->
							cycles.any { it.startDate == date }
						} ?: false
						
						val isCycleEnd = day?.let { date ->
							cycles.any { it.endDate == date }
						} ?: false
						
						DayCell(
							date = day,
							isCurrentMonth = day?.month == currentMonth.month,
							isPredicted = day in predictedPeriod,
							isInCycle = isInCycle,
							isCycleStart = isCycleStart,
							isCycleEnd = isCycleEnd,
							onClick = { day?.let(onDateClick) },
							onLongClick = { day?.let(onAddSymptom) },
							modifier = Modifier.weight(1f).padding(4.dp)
						)
					}
				}
			}
		}
	}
}

@Composable private fun Header(currentMonth: YearMonth, onPrev: () -> Unit, onNext: () -> Unit) {
	Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
		Text("<", modifier = Modifier.clickable { onPrev() }, style = MaterialTheme.typography.headlineSmall)
		Text(
			"${currentMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, java.util.Locale.getDefault())} ${currentMonth.year}",
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Bold
		)
		Text(">", modifier = Modifier.clickable { onNext() }, style = MaterialTheme.typography.headlineSmall)
	}
}

@Composable private fun WeekDaysRow() {
	val labels = listOf("Пн","Вт","Ср","Чт","Пт","Сб","Вс")
	Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
		labels.forEach { label ->
			Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
				Text(label, style = MaterialTheme.typography.bodySmall)
			}
		}
	}
}

@Composable private fun DayCell(
	date: LocalDate?,
	isCurrentMonth: Boolean,
	isPredicted: Boolean,
	isInCycle: Boolean = false,
	isCycleStart: Boolean = false,
	isCycleEnd: Boolean = false,
	onClick: () -> Unit,
	onLongClick: () -> Unit = {},
	modifier: Modifier = Modifier
) {
	val bgColor by animateColorAsState(
		targetValue = when {
			!isCurrentMonth || date == null -> MaterialTheme.colorScheme.surface
			isCycleStart -> MaterialTheme.colorScheme.error
			isCycleEnd -> MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
			isInCycle -> MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
			isPredicted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
			else -> MaterialTheme.colorScheme.surface
		},
		label = "dayBg"
	)
	
	val textColor = when {
		!isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
		isCycleStart || isCycleEnd -> MaterialTheme.colorScheme.onError
		isInCycle -> MaterialTheme.colorScheme.onError.copy(alpha = 0.8f)
		else -> MaterialTheme.colorScheme.onSurface
	}
	
	Box(
		modifier = modifier
			.aspectRatio(1f)
			.clip(CircleShape)
			.background(bgColor)
			.clickable(enabled = date != null && isCurrentMonth) { onClick() }
			.clickable(enabled = date != null && isCurrentMonth) { onLongClick() },
		contentAlignment = Alignment.Center
	) {
		Text(
			text = date?.dayOfMonth?.toString() ?: "",
			color = textColor,
			fontWeight = if (isCycleStart || isCycleEnd) FontWeight.Bold else FontWeight.Normal
		)
	}
}

private fun generateMonthDays(month: YearMonth): List<LocalDate?> {
	val firstDay = month.atDay(1)
	val firstWeekday = (firstDay.dayOfWeek.value + 6) % 7 // make Monday=0
	val daysInMonth = month.lengthOfMonth()
	val result = mutableListOf<LocalDate?>()
	repeat(firstWeekday) { result.add(null) }
	for (d in 1..daysInMonth) result.add(month.atDay(d))
	while (result.size % 7 != 0) result.add(null)
	return result
}

private operator fun LocalDate.rangeTo(other: LocalDate): List<LocalDate> {
	val days = mutableListOf<LocalDate>()
	var d = this
	while (!d.isAfter(other)) {
		days.add(d)
		d = d.plusDays(1)
	}
	return days
}
