package com.example.cycletracker.ui.goals

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    viewModel: CycleViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedGoal by remember { mutableStateOf(GoalType.PREGNANCY_PLANNING) }
    var showGoalDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Мои цели",
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { showGoalDialog = true }) {
                        Icon(Icons.Default.Add, "Добавить цель")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2E7D32),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF1F8E9)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Выбор типа цели
            GoalTypeSelector(
                selectedGoal = selectedGoal,
                onGoalSelected = { selectedGoal = it }
            )
            
            // Контент в зависимости от выбранной цели
            when (selectedGoal) {
                GoalType.PREGNANCY_PLANNING -> PregnancyPlanningContent(viewModel = viewModel)
                GoalType.CONTRACEPTION -> ContraceptionContent(viewModel = viewModel)
                GoalType.HEALTH_MONITORING -> HealthMonitoringContent(viewModel = viewModel)
                GoalType.SYMPTOM_TRACKING -> SymptomTrackingContent(viewModel = viewModel)
            }
        }
    }
    
    if (showGoalDialog) {
        GoalDialog(
            onDismiss = { showGoalDialog = false },
            onGoalAdded = { goal ->
                // Сохранить цель
                showGoalDialog = false
            }
        )
    }
}

@Composable
fun GoalTypeSelector(
    selectedGoal: GoalType,
    onGoalSelected: (GoalType) -> Unit
) {
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
                text = "Выберите цель",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            GoalType.values().forEach { goalType ->
                GoalTypeItem(
                    goalType = goalType,
                    isSelected = selectedGoal == goalType,
                    onClick = { onGoalSelected(goalType) }
                )
                if (goalType != GoalType.values().last()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun GoalTypeItem(
    goalType: GoalType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = "background_animation"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            goalType.color.copy(alpha = 0.1f * backgroundColor),
                            goalType.color.copy(alpha = 0.05f * backgroundColor)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(goalType.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = goalType.icon,
                    contentDescription = null,
                    tint = goalType.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goalType.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) goalType.color else Color.Black
                )
                Text(
                    text = goalType.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выбрано",
                    tint = goalType.color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun PregnancyPlanningContent(viewModel: CycleViewModel) {
    val cycles by viewModel.cycles.collectAsState()
    val nextOvulation = calculateNextOvulation(cycles)
    val fertilityWindow = calculateFertilityWindow(nextOvulation)
    
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Карточка с информацией о фертильности
        FertilityInfoCard(
            nextOvulation = nextOvulation,
            fertilityWindow = fertilityWindow
        )
        
        // Советы для планирования беременности
        PregnancyTipsCard()
        
        // Трекер овуляции
        OvulationTrackerCard(viewModel = viewModel)
    }
}

@Composable
fun ContraceptionContent(viewModel: CycleViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Информация о безопасных днях
        SafeDaysCard(viewModel = viewModel)
        
        // Методы контрацепции
        ContraceptionMethodsCard()
        
        // Напоминания о контрацепции
        ContraceptionRemindersCard()
    }
}

@Composable
fun HealthMonitoringContent(viewModel: CycleViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Мониторинг веса
        WeightTrackingCard()
        
        // Мониторинг температуры
        TemperatureTrackingCard()
        
        // Общие показатели здоровья
        HealthMetricsCard()
    }
}

@Composable
fun SymptomTrackingContent(viewModel: CycleViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Трекер симптомов
        SymptomTrackerCard()
        
        // Анализ паттернов
        SymptomPatternsCard()
        
        // Рекомендации
        SymptomRecommendationsCard()
    }
}

@Composable
fun FertilityInfoCard(
    nextOvulation: LocalDate?,
    fertilityWindow: Pair<LocalDate, LocalDate>?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "Фертильное окно",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (nextOvulation != null && fertilityWindow != null) {
                Text(
                    text = "Следующая овуляция: ${nextOvulation.format(DateTimeFormatter.ofPattern("d MMMM"))}",
                    fontSize = 16.sp,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Фертильное окно: ${fertilityWindow.first.format(DateTimeFormatter.ofPattern("d MMM"))} - ${fertilityWindow.second.format(DateTimeFormatter.ofPattern("d MMM"))}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Недостаточно данных для расчёта",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PregnancyTipsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Советы для планирования",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val tips = listOf(
                "Принимайте фолиевую кислоту за 3 месяца до зачатия",
                "Откажитесь от алкоголя и курения",
                "Поддерживайте здоровый вес",
                "Регулярно занимайтесь спортом",
                "Избегайте стресса"
            )
            
            tips.forEach { tip ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = tip,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SafeDaysCard(viewModel: CycleViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Безопасные дни",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Следующие 7 дней после окончания менструации считаются относительно безопасными для незащищённого секса.",
                fontSize = 14.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "⚠️ Помните: календарный метод не является надёжным способом контрацепции!",
                fontSize = 12.sp,
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ContraceptionMethodsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Методы контрацепции",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            val methods = listOf(
                "Презервативы" to "95% эффективности",
                "Оральные контрацептивы" to "99% эффективности",
                "ВМС" to "99% эффективности",
                "Пластырь" to "99% эффективности"
            )
            
            methods.forEach { (method, effectiveness) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = method,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Text(
                        text = effectiveness,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun WeightTrackingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Отслеживание веса",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Регулярное взвешивание поможет отследить изменения веса в течение цикла.",
                fontSize = 14.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { /* Открыть трекер веса */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Добавить вес", color = Color.White)
            }
        }
    }
}

@Composable
fun TemperatureTrackingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Базальная температура",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Измерение базальной температуры поможет точнее определить овуляцию.",
                fontSize = 14.sp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { /* Открыть трекер температуры */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Добавить температуру", color = Color.White)
            }
        }
    }
}

@Composable
fun SymptomTrackerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Трекер симптомов",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Отмечайте симптомы для выявления паттернов и улучшения прогнозов.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun HealthMetricsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Показатели здоровья",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Отслеживайте общие показатели здоровья для комплексного анализа.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SymptomPatternsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Анализ паттернов",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Анализируйте повторяющиеся симптомы для лучшего понимания своего цикла.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun SymptomRecommendationsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Рекомендации",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Получайте персонализированные рекомендации на основе ваших данных.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun ContraceptionRemindersCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Напоминания",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Настройте напоминания о приёме контрацептивов.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun OvulationTrackerCard(viewModel: CycleViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Трекер овуляции",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Отмечайте симптомы овуляции для более точного прогноза.",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun GoalDialog(
    onDismiss: () -> Unit,
    onGoalAdded: (Goal) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Добавить цель") },
        text = { Text("Выберите тип цели для отслеживания") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Добавить")
            }
        }
    )
}

enum class GoalType(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
) {
    PREGNANCY_PLANNING(
        title = "Планирование беременности",
        description = "Отслеживание фертильности и овуляции",
        icon = Icons.Default.Favorite,
        color = Color(0xFFE91E63)
    ),
    CONTRACEPTION(
        title = "Контрацепция",
        description = "Отслеживание безопасных дней",
        icon = Icons.Default.Security,
        color = Color(0xFF2196F3)
    ),
    HEALTH_MONITORING(
        title = "Мониторинг здоровья",
        description = "Отслеживание веса, температуры",
        icon = Icons.Default.MonitorHeart,
        color = Color(0xFF4CAF50)
    ),
    SYMPTOM_TRACKING(
        title = "Отслеживание симптомов",
        description = "Анализ симптомов и паттернов",
        icon = Icons.Default.Analytics,
        color = Color(0xFFFF9800)
    )
}

data class Goal(
    val type: GoalType,
    val title: String,
    val description: String,
    val targetDate: LocalDate?,
    val isCompleted: Boolean = false
)

private fun calculateNextOvulation(cycles: List<com.example.cycletracker.data.Cycle>): LocalDate? {
    if (cycles.isEmpty()) return null
    
    val latestCycle = cycles.first()
    val cycleLength = latestCycle.cycleLength ?: 28
    val ovulationDay = cycleLength - 14
    
    return latestCycle.startDate.plusDays(ovulationDay.toLong())
}

private fun calculateFertilityWindow(ovulationDate: LocalDate?): Pair<LocalDate, LocalDate>? {
    if (ovulationDate == null) return null
    
    val windowStart = ovulationDate.minusDays(4)
    val windowEnd = ovulationDate.plusDays(1)
    
    return Pair(windowStart, windowEnd)
}
