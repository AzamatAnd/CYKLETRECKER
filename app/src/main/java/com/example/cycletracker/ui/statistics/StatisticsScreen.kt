<<<<<<< HEAD
package com.example.cycletracker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.CycleEntity
import com.example.cycletracker.data.SymptomEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            CycleStatisticsCard(cycles = cycles)
        }
        
        item {
            SymptomStatisticsCard(symptoms = symptoms)
        }
        
        item {
            RegularityCard(cycles = cycles)
        }
        
        item {
            RecentTrendsCard(cycles = cycles, symptoms = symptoms)
        }
    }
}

@Composable
private fun CycleStatisticsCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val completedCycles = cycles.filter { it.endDate != null }
    val avgLength = if (completedCycles.isNotEmpty()) {
        completedCycles.map { cycle ->
            ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
        }.average().toInt()
    } else 0
    
    val shortestCycle = completedCycles.minOfOrNull { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    } ?: 0
    
    val longestCycle = completedCycles.maxOfOrNull { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    } ?: 0
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Статистика циклов",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Всего циклов",
                    value = completedCycles.size.toString(),
                    icon = Icons.Default.Repeat
                )
                StatItem(
                    label = "Средняя длина",
                    value = if (avgLength > 0) "${avgLength} дн." else "—",
                    icon = Icons.Default.Analytics
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Короткий",
                    value = if (shortestCycle > 0) "${shortestCycle} дн." else "—",
                    icon = Icons.Default.KeyboardArrowDown
                )
                StatItem(
                    label = "Длинный",
                    value = if (longestCycle > 0) "${longestCycle} дн." else "—",
                    icon = Icons.Default.KeyboardArrowUp
                )
            }
        }
    }
}

@Composable
private fun SymptomStatisticsCard(
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    val symptomCounts = symptoms.groupBy { it.type }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
        .take(5)
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Частые симптомы",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (symptomCounts.isEmpty()) {
                Text(
                    text = "Нет данных о симптомах",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                symptomCounts.forEach { (symptom, count) ->
                    SymptomItem(
                        symptom = symptom,
                        count = count,
                        totalSymptoms = symptoms.size
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SymptomItem(
    symptom: String,
    count: Int,
    totalSymptoms: Int,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalSymptoms > 0) (count * 100.0 / totalSymptoms).roundToInt() else 0
    
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = symptom,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$count раз",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RegularityCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val completedCycles = cycles.filter { it.endDate != null }
    val regularity = calculateRegularity(completedCycles)
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Регулярность",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = when {
                    regularity >= 80 -> "Очень регулярный"
                    regularity >= 60 -> "Регулярный"
                    regularity >= 40 -> "Умеренно регулярный"
                    regularity >= 20 -> "Нерегулярный"
                    else -> "Очень нерегулярный"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = when {
                    regularity >= 80 -> Color(0xFF4CAF50)
                    regularity >= 60 -> Color(0xFF8BC34A)
                    regularity >= 40 -> Color(0xFFFFC107)
                    regularity >= 20 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
            )
            
            Text(
                text = "Показатель: $regularity%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecentTrendsCard(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    val last3Cycles = cycles.sortedByDescending { it.startDate }.take(3)
    val recentSymptoms = symptoms.filter { 
        it.date.isAfter(LocalDate.now().minusDays(30))
    }
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Последние тренды",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (last3Cycles.isNotEmpty()) {
                Text(
                    text = "Последние 3 цикла:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                last3Cycles.forEach { cycle ->
                    val length = cycle.endDate?.let { endDate ->
                        ChronoUnit.DAYS.between(cycle.startDate, endDate).toInt()
                    } ?: "В процессе"
                    
                    Text(
                        text = "${cycle.startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} - $length дн.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            
            if (recentSymptoms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Симптомы за 30 дней: ${recentSymptoms.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

private fun calculateRegularity(cycles: List<CycleEntity>): Int {
    if (cycles.size < 2) return 0
    
    val lengths = cycles.map { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    }
    
    val avgLength = lengths.average()
    val variance = lengths.map { (it - avgLength) * (it - avgLength) }.average()
    val standardDeviation = kotlin.math.sqrt(variance)
    
    // Простая формула регулярности: чем меньше отклонение, тем выше регулярность
    val regularity = ((1 - standardDeviation / avgLength) * 100).coerceIn(0.0, 100.0)
    
    return regularity.roundToInt()
}
=======
package com.example.cycletracker.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cycletracker.data.CycleEntity
import com.example.cycletracker.data.SymptomEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@Composable
fun StatisticsScreen(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            CycleStatisticsCard(cycles = cycles)
        }
        
        item {
            SymptomStatisticsCard(symptoms = symptoms)
        }
        
        item {
            RegularityCard(cycles = cycles)
        }
        
        item {
            RecentTrendsCard(cycles = cycles, symptoms = symptoms)
        }
    }
}

@Composable
private fun CycleStatisticsCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val completedCycles = cycles.filter { it.endDate != null }
    val avgLength = if (completedCycles.isNotEmpty()) {
        completedCycles.map { cycle ->
            ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
        }.average().toInt()
    } else 0
    
    val shortestCycle = completedCycles.minOfOrNull { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    } ?: 0
    
    val longestCycle = completedCycles.maxOfOrNull { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    } ?: 0
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Статистика циклов",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Всего циклов",
                    value = completedCycles.size.toString(),
                    icon = Icons.Default.Repeat
                )
                StatItem(
                    label = "Средняя длина",
                    value = if (avgLength > 0) "${avgLength} дн." else "—",
                    icon = Icons.Default.Analytics
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Короткий",
                    value = if (shortestCycle > 0) "${shortestCycle} дн." else "—",
                    icon = Icons.Default.KeyboardArrowDown
                )
                StatItem(
                    label = "Длинный",
                    value = if (longestCycle > 0) "${longestCycle} дн." else "—",
                    icon = Icons.Default.KeyboardArrowUp
                )
            }
        }
    }
}

@Composable
private fun SymptomStatisticsCard(
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    val symptomCounts = symptoms.groupBy { it.type }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
        .take(5)
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Частые симптомы",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (symptomCounts.isEmpty()) {
                Text(
                    text = "Нет данных о симптомах",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                symptomCounts.forEach { (symptom, count) ->
                    SymptomItem(
                        symptom = symptom,
                        count = count,
                        totalSymptoms = symptoms.size
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SymptomItem(
    symptom: String,
    count: Int,
    totalSymptoms: Int,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalSymptoms > 0) (count * 100.0 / totalSymptoms).roundToInt() else 0
    
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = symptom,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$count раз",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun RegularityCard(
    cycles: List<CycleEntity>,
    modifier: Modifier = Modifier
) {
    val completedCycles = cycles.filter { it.endDate != null }
    val regularity = calculateRegularity(completedCycles)
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Регулярность",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = when {
                    regularity >= 80 -> "Очень регулярный"
                    regularity >= 60 -> "Регулярный"
                    regularity >= 40 -> "Умеренно регулярный"
                    regularity >= 20 -> "Нерегулярный"
                    else -> "Очень нерегулярный"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = when {
                    regularity >= 80 -> Color(0xFF4CAF50)
                    regularity >= 60 -> Color(0xFF8BC34A)
                    regularity >= 40 -> Color(0xFFFFC107)
                    regularity >= 20 -> Color(0xFFFF9800)
                    else -> Color(0xFFF44336)
                }
            )
            
            Text(
                text = "Показатель: $regularity%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecentTrendsCard(
    cycles: List<CycleEntity>,
    symptoms: List<SymptomEntity>,
    modifier: Modifier = Modifier
) {
    val last3Cycles = cycles.sortedByDescending { it.startDate }.take(3)
    val recentSymptoms = symptoms.filter { 
        it.date.isAfter(LocalDate.now().minusDays(30))
    }
    
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Последние тренды",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (last3Cycles.isNotEmpty()) {
                Text(
                    text = "Последние 3 цикла:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                last3Cycles.forEach { cycle ->
                    val length = cycle.endDate?.let { endDate ->
                        ChronoUnit.DAYS.between(cycle.startDate, endDate).toInt()
                    } ?: "В процессе"
                    
                    Text(
                        text = "${cycle.startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} - $length дн.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            
            if (recentSymptoms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Симптомы за 30 дней: ${recentSymptoms.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

private fun calculateRegularity(cycles: List<CycleEntity>): Int {
    if (cycles.size < 2) return 0
    
    val lengths = cycles.map { cycle ->
        ChronoUnit.DAYS.between(cycle.startDate, cycle.endDate!!).toInt()
    }
    
    val avgLength = lengths.average()
    val variance = lengths.map { (it - avgLength) * (it - avgLength) }.average()
    val standardDeviation = kotlin.math.sqrt(variance)
    
    // Простая формула регулярности: чем меньше отклонение, тем выше регулярность
    val regularity = ((1 - standardDeviation / avgLength) * 100).coerceIn(0.0, 100.0)
    
    return regularity.roundToInt()
}
>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
