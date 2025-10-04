<<<<<<< HEAD
package com.example.cycletracker.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.CycleEntity
import com.example.cycletracker.data.SymptomEntity
import com.example.cycletracker.ui.animations.*
import com.example.cycletracker.ui.components.*
import com.example.cycletracker.ui.icons.AppEmojis
import com.example.cycletracker.ui.icons.AppIcons
import com.example.cycletracker.ui.icons.CycleIcons
import com.example.cycletracker.ui.icons.getCyclePhaseIcon
import com.example.cycletracker.ui.icons.getSymptomIcon
import com.example.cycletracker.ui.icons.getMoodIcon
import androidx.compose.ui.res.painterResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    averageCycleDays: Int,
    onAddSymptom: () -> Unit,
    onViewCalendar: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lastCycle = cycles.maxByOrNull { it.startDate }
    val cycleInfo = calculateCycleInfo(lastCycle, averageCycleDays)
    val recentSymptoms = symptoms.take(5)
    
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard(
                cycleInfo = cycleInfo,
                onAddSymptom = onAddSymptom
            )
        }
        
        item {
            CycleStatusCard(
                cycleInfo = cycleInfo,
                onViewCalendar = onViewCalendar
            )
        }
        
        if (recentSymptoms.isNotEmpty()) {
            item {
                RecentSymptomsCard(
                    symptoms = recentSymptoms,
                    onViewHistory = onViewHistory
                )
            }
        }
        
        item {
            QuickActionsCard(
                onAddSymptom = onAddSymptom,
                onViewCalendar = onViewCalendar,
                onViewHistory = onViewHistory
            )
        }
        
        item {
            StatisticsCard(cycles = cycles)
        }
    }
}

@Composable
private fun WelcomeCard(
    cycleInfo: CycleInfo,
    onAddSymptom: () -> Unit,
    modifier: Modifier = Modifier
) {
    FadeInAnimation(modifier = modifier, delay = 200) {
        GradientCard(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👋 Добро пожаловать!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        cycleInfo.isPeriodPredicted -> "📅 Ожидается начало цикла через ${cycleInfo.daysUntilNext} дн."
                        cycleInfo.isInPeriod -> "🩸 День цикла: ${cycleInfo.currentDay}"
                        else -> "🌸 Следующий цикл через ${cycleInfo.daysUntilNext} дн."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(20.dp))
                PulseAnimation {
                    Button(
                        onClick = onAddSymptom,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Icon(AppIcons.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить симптом")
                    }
                }
            }
        }
    }
}

@Composable
private fun CycleStatusCard(
    cycleInfo: CycleInfo,
    onViewCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    SlideInFromBottom(modifier = modifier, delay = 400) {
        val status = when {
            cycleInfo.isInPeriod -> CycleStatus.PERIOD
            cycleInfo.isPeriodPredicted -> CycleStatus.PREDICTED
            else -> CycleStatus.FOLLICULAR
        }
        
        StatusCard(
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewCalendar() }
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Статус цикла",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(AppIcons.Calendar, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                when {
                    cycleInfo.isInPeriod -> {
                        Text(
                            text = "🩸 Месячные",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "День ${cycleInfo.currentDay} из ${cycleInfo.estimatedLength}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    cycleInfo.isPeriodPredicted -> {
                        Text(
                            text = "📅 Ожидается начало",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Через ${cycleInfo.daysUntilNext} дней",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    else -> {
                        Text(
                            text = "🌸 Фолликулярная фаза",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "День ${cycleInfo.currentDay} цикла",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentSymptomsCard(
    symptoms: List<SymptomEntity>,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScaleInAnimation(modifier = modifier, delay = 600) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewHistory() }
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 Последние симптомы",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(AppIcons.History, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                if (symptoms.isEmpty()) {
                    Text(
                        text = "📭 Пока нет записей",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    symptoms.take(3).forEach { symptom ->
                        SymptomItem(symptom = symptom)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomItem(
    symptom: SymptomEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = getSymptomIcon(symptom.type)),
            contentDescription = symptom.type,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = symptom.type,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = symptom.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (symptom.intensity != null) {
            Text(
                text = "${symptom.intensity}/10",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun QuickActionsCard(
    onAddSymptom: () -> Unit,
    onViewCalendar: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Быстрые действия",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.Add,
                    label = "Симптом",
                    onClick = onAddSymptom
                )
                QuickActionButton(
                    icon = Icons.Default.CalendarToday,
                    label = "Календарь",
                    onClick = onViewCalendar
                )
                QuickActionButton(
                    icon = Icons.Default.History,
                    label = "История",
                    onClick = onViewHistory
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatisticsCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val avgLength = if (cycles.isNotEmpty()) {
        cycles.mapNotNull { cycle ->
            cycle.endDate?.let { end ->
                ChronoUnit.DAYS.between(cycle.startDate, end).toInt()
            }
        }.average().toInt()
    } else 0
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Всего циклов",
                    value = cycles.size.toString()
                )
                StatItem(
                    label = "Средняя длина",
                    value = if (avgLength > 0) "${avgLength} дн." else "—"
                )
                StatItem(
                    label = "Последний",
                    value = cycles.maxByOrNull { it.startDate }?.startDate?.format(
                        DateTimeFormatter.ofPattern("dd.MM")
                    ) ?: "—"
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

data class CycleInfo(
    val isInPeriod: Boolean,
    val isPeriodPredicted: Boolean,
    val currentDay: Int,
    val daysUntilNext: Int,
    val estimatedLength: Int
)

private fun calculateCycleInfo(lastCycle: CycleEntity?, averageCycleDays: Int): CycleInfo {
    val today = LocalDate.now()
    
    if (lastCycle == null) {
        return CycleInfo(
            isInPeriod = false,
            isPeriodPredicted = false,
            currentDay = 0,
            daysUntilNext = 0,
            estimatedLength = averageCycleDays
        )
    }
    
    val daysSinceStart = ChronoUnit.DAYS.between(lastCycle.startDate, today).toInt()
    val isInPeriod = lastCycle.endDate == null && daysSinceStart >= 0 && daysSinceStart < 7
    val nextCycleStart = lastCycle.startDate.plusDays(averageCycleDays.toLong())
    val daysUntilNext = ChronoUnit.DAYS.between(today, nextCycleStart).toInt()
    val isPeriodPredicted = daysUntilNext <= 3 && daysUntilNext >= 0
    
    return CycleInfo(
        isInPeriod = isInPeriod,
        isPeriodPredicted = isPeriodPredicted,
        currentDay = daysSinceStart + 1,
        daysUntilNext = maxOf(0, daysUntilNext),
        estimatedLength = averageCycleDays
    )
}
=======
package com.example.cycletracker.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.CycleEntity
import com.example.cycletracker.data.SymptomEntity
import com.example.cycletracker.ui.animations.*
import com.example.cycletracker.ui.components.*
import com.example.cycletracker.ui.icons.AppEmojis
import com.example.cycletracker.ui.icons.AppIcons
import com.example.cycletracker.ui.icons.CycleIcons
import com.example.cycletracker.ui.icons.getCyclePhaseIcon
import com.example.cycletracker.ui.icons.getSymptomIcon
import com.example.cycletracker.ui.icons.getMoodIcon
import androidx.compose.ui.res.painterResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    averageCycleDays: Int,
    onAddSymptom: () -> Unit,
    onViewCalendar: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lastCycle = cycles.maxByOrNull { it.startDate }
    val cycleInfo = calculateCycleInfo(lastCycle, averageCycleDays)
    val recentSymptoms = symptoms.take(5)
    
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            WelcomeCard(
                cycleInfo = cycleInfo,
                onAddSymptom = onAddSymptom
            )
        }
        
        item {
            CycleStatusCard(
                cycleInfo = cycleInfo,
                onViewCalendar = onViewCalendar
            )
        }
        
        if (recentSymptoms.isNotEmpty()) {
            item {
                RecentSymptomsCard(
                    symptoms = recentSymptoms,
                    onViewHistory = onViewHistory
                )
            }
        }
        
        item {
            QuickActionsCard(
                onAddSymptom = onAddSymptom,
                onViewCalendar = onViewCalendar,
                onViewHistory = onViewHistory
            )
        }
        
        item {
            StatisticsCard(cycles = cycles)
        }
    }
}

@Composable
private fun WelcomeCard(
    cycleInfo: CycleInfo,
    onAddSymptom: () -> Unit,
    modifier: Modifier = Modifier
) {
    FadeInAnimation(modifier = modifier, delay = 200) {
        GradientCard(
            colors = listOf(
                MaterialTheme.colorScheme.primaryContainer,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👋 Добро пожаловать!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when {
                        cycleInfo.isPeriodPredicted -> "📅 Ожидается начало цикла через ${cycleInfo.daysUntilNext} дн."
                        cycleInfo.isInPeriod -> "🩸 День цикла: ${cycleInfo.currentDay}"
                        else -> "🌸 Следующий цикл через ${cycleInfo.daysUntilNext} дн."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(20.dp))
                PulseAnimation {
                    Button(
                        onClick = onAddSymptom,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Icon(AppIcons.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Добавить симптом")
                    }
                }
            }
        }
    }
}

@Composable
private fun CycleStatusCard(
    cycleInfo: CycleInfo,
    onViewCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    SlideInFromBottom(modifier = modifier, delay = 400) {
        val status = when {
            cycleInfo.isInPeriod -> CycleStatus.PERIOD
            cycleInfo.isPeriodPredicted -> CycleStatus.PREDICTED
            else -> CycleStatus.FOLLICULAR
        }
        
        StatusCard(
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewCalendar() }
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Статус цикла",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Icon(AppIcons.Calendar, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                when {
                    cycleInfo.isInPeriod -> {
                        Text(
                            text = "🩸 Месячные",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "День ${cycleInfo.currentDay} из ${cycleInfo.estimatedLength}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    cycleInfo.isPeriodPredicted -> {
                        Text(
                            text = "📅 Ожидается начало",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Через ${cycleInfo.daysUntilNext} дней",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    else -> {
                        Text(
                            text = "🌸 Фолликулярная фаза",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "День ${cycleInfo.currentDay} цикла",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentSymptomsCard(
    symptoms: List<SymptomEntity>,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    ScaleInAnimation(modifier = modifier, delay = 600) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewHistory() }
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 Последние симптомы",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(AppIcons.History, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                if (symptoms.isEmpty()) {
                    Text(
                        text = "📭 Пока нет записей",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    symptoms.take(3).forEach { symptom ->
                        SymptomItem(symptom = symptom)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomItem(
    symptom: SymptomEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = getSymptomIcon(symptom.type)),
            contentDescription = symptom.type,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = symptom.type,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = symptom.date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (symptom.intensity != null) {
            Text(
                text = "${symptom.intensity}/10",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun QuickActionsCard(
    onAddSymptom: () -> Unit,
    onViewCalendar: () -> Unit,
    onViewHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Быстрые действия",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickActionButton(
                    icon = Icons.Default.Add,
                    label = "Симптом",
                    onClick = onAddSymptom
                )
                QuickActionButton(
                    icon = Icons.Default.CalendarToday,
                    label = "Календарь",
                    onClick = onViewCalendar
                )
                QuickActionButton(
                    icon = Icons.Default.History,
                    label = "История",
                    onClick = onViewHistory
                )
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StatisticsCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val avgLength = if (cycles.isNotEmpty()) {
        cycles.mapNotNull { cycle ->
            cycle.endDate?.let { end ->
                ChronoUnit.DAYS.between(cycle.startDate, end).toInt()
            }
        }.average().toInt()
    } else 0
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Всего циклов",
                    value = cycles.size.toString()
                )
                StatItem(
                    label = "Средняя длина",
                    value = if (avgLength > 0) "${avgLength} дн." else "—"
                )
                StatItem(
                    label = "Последний",
                    value = cycles.maxByOrNull { it.startDate }?.startDate?.format(
                        DateTimeFormatter.ofPattern("dd.MM")
                    ) ?: "—"
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

data class CycleInfo(
    val isInPeriod: Boolean,
    val isPeriodPredicted: Boolean,
    val currentDay: Int,
    val daysUntilNext: Int,
    val estimatedLength: Int
)

private fun calculateCycleInfo(lastCycle: CycleEntity?, averageCycleDays: Int): CycleInfo {
    val today = LocalDate.now()
    
    if (lastCycle == null) {
        return CycleInfo(
            isInPeriod = false,
            isPeriodPredicted = false,
            currentDay = 0,
            daysUntilNext = 0,
            estimatedLength = averageCycleDays
        )
    }
    
    val daysSinceStart = ChronoUnit.DAYS.between(lastCycle.startDate, today).toInt()
    val isInPeriod = lastCycle.endDate == null && daysSinceStart >= 0 && daysSinceStart < 7
    val nextCycleStart = lastCycle.startDate.plusDays(averageCycleDays.toLong())
    val daysUntilNext = ChronoUnit.DAYS.between(today, nextCycleStart).toInt()
    val isPeriodPredicted = daysUntilNext <= 3 && daysUntilNext >= 0
    
    return CycleInfo(
        isInPeriod = isInPeriod,
        isPeriodPredicted = isPeriodPredicted,
        currentDay = daysSinceStart + 1,
        daysUntilNext = maxOf(0, daysUntilNext),
        estimatedLength = averageCycleDays
    )
}
>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
