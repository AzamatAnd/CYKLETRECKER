package com.example.cycletracker.ui.phases

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.data.Cycle
import com.example.cycletracker.ui.CycleViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhasesScreen(
    viewModel: CycleViewModel,
    onNavigateBack: () -> Unit
) {
    val cyclesState = viewModel.cycles
    val cycles by cyclesState.collectAsState()

    val scope = rememberCoroutineScope()
    var dayOfCycle by remember { mutableStateOf<Int?>(null) }
    var cycleLength by remember { mutableStateOf(28) }

    LaunchedEffect(cycles) {
        scope.launch {
            val latestCycle = cycles.firstOrNull()
            if (latestCycle != null) {
                val today = LocalDate.now()
                val days = ChronoUnit.DAYS.between(latestCycle.startDate, today).toInt() + 1
                dayOfCycle = if (days > 0) days else null
                cycleLength = latestCycle.cycleLength ?: cycleLength
            } else {
                dayOfCycle = null
            }
        }
    }

    val phases = remember { defaultPhases() }
    val currentPhase = remember(dayOfCycle) { dayOfCycle?.let { determinePhase(phases, it) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фазы цикла") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
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
            PhasesHeader(dayOfCycle = dayOfCycle, cycleLength = cycleLength, currentPhase = currentPhase)

            phases.forEach { phase ->
                PhaseCard(
                    phase = phase,
                    isActive = phase.id == currentPhase?.id,
                    dayOfCycle = dayOfCycle
                )
            }
        }
    }
}

@Composable
private fun PhasesHeader(dayOfCycle: Int?, cycleLength: Int, currentPhase: CyclePhase?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Сегодня",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )

            Text(
                text = when (dayOfCycle) {
                    null -> "Начните отслеживание цикла, чтобы видеть фазу"
                    else -> "${dayOfCycle}-й день цикла из ориентировочных $cycleLength"
                },
                fontSize = 14.sp,
                color = Color.Gray
            )

            if (currentPhase != null) {
                Surface(
                    color = currentPhase.gradient.first.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Текущая фаза: ${currentPhase.title}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = currentPhase.gradient.first
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentPhase.focus,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhaseCard(phase: CyclePhase, isActive: Boolean, dayOfCycle: Int?) {
    val backgroundBrush = remember(phase) {
        Brush.linearGradient(listOf(phase.gradient.first, phase.gradient.second))
    }
    val animatedElevation by animateFloatAsState(
        targetValue = if (isActive) 12f else 4f,
        animationSpec = spring(),
        label = "phaseElevation"
    )
    val badgeColor by animateColorAsState(
        targetValue = if (isActive) phase.gradient.first else Color(0xFFBDBDBD),
        label = "badgeColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(animatedElevation.dp, RoundedCornerShape(28.dp), clip = false),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBubble(phase.icon, backgroundBrush)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = phase.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = phase.gradient.first
                    )
                    Text(
                        text = "Дни ${phase.range.first} – ${phase.range.last}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Text(
                text = phase.description,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Surface(
                color = Color(0xFFF8F8F8),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Рекомендации",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = phase.gradient.first
                    )
                    phase.recommendations.forEach { tip ->
                        Text(text = "• $tip", fontSize = 13.sp, color = Color.DarkGray)
                    }
                }
            }

            if (isActive && dayOfCycle != null) {
                Surface(
                    color = badgeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Сейчас ${dayOfCycle}-й день — вы в этой фазе",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = 13.sp,
                        color = phase.gradient.first
                    )
                }
            }
        }
    }
}

@Composable
private fun IconBubble(icon: PhaseIcon, brush: Brush) {
    Surface(shape = CircleShape, color = Color.Transparent) {
        Surface(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(brush),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon.imageVector,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

private fun defaultPhases(): List<CyclePhase> = listOf(
    CyclePhase(
        id = "menstrual",
        title = "Менструальная фаза",
        description = "Организм освобождается от эндометрия. Возможны усталость и чувствительность, побалуйте себя заботой и теплом.",
        focus = "Главное — комфорт и мягкая поддержка организма",
        range = 1..5,
        recommendations = listOf(
            "Лёгкие растяжки и спокойные прогулки",
            "Тёплые напитки и продукты с железом",
            "Медитации, дневник благодарности, любимые фильмы"
        ),
        icon = PhaseIcon.Menstrual,
        gradient = Color(0xFFE91E63) to Color(0xFFFF758C)
    ),
    CyclePhase(
        id = "follicular",
        title = "Фолликулярная фаза",
        description = "Гормоны растут, энергии становится больше. Отличное время для планирования, обучения и новых идей.",
        focus = "Мозг работает ярко — используйте прилив вдохновения",
        range = 6..12,
        recommendations = listOf(
            "Добавьте силовые тренировки и танцы",
            "Ешьте больше зелени, белка и сезонных овощей",
            "Планируйте важные встречи и переговоры"
        ),
        icon = PhaseIcon.Follicular,
        gradient = Color(0xFF7C4DFF) to Color(0xFFB47CFF)
    ),
    CyclePhase(
        id = "ovulation",
        title = "Овуляция",
        description = "Пик энергии, уверенности и притягательности. Эмоции яркие, общение даётся легко.",
        focus = "Сияние, уверенность и смелость",
        range = 13..16,
        recommendations = listOf(
            "Лучшее время для выступлений и фотосессий",
            "Добавьте продукты с цинком и омега-3",
            "Берегите сон, чтобы поддержать гормональный баланс"
        ),
        icon = PhaseIcon.Ovulation,
        gradient = Color(0xFFFF9800) to Color(0xFFFFC107)
    ),
    CyclePhase(
        id = "luteal",
        title = "Лютеиновая фаза",
        description = "Энергия плавно снижается, может появляться чувствительность. Время мягко замедляться и переключаться на заботу о себе.",
        focus = "Баланс между делами и отдыхом",
        range = 17..28,
        recommendations = listOf(
            "Лёгкий йога-флоу, пилатес или расслабляющий массаж",
            "Добавьте магний, витамин B6 и тёмный шоколад",
            "Составьте уютный список фильмов и ритуалов ухода"
        ),
        icon = PhaseIcon.Luteal,
        gradient = Color(0xFF4CAF50) to Color(0xFF81C784)
    )
)

private fun determinePhase(phases: List<CyclePhase>, day: Int): CyclePhase? {
    return phases.firstOrNull { day in it.range }
        ?: phases.lastOrNull()?.takeIf { day > it.range.last }
        ?: phases.firstOrNull()
}

private data class CyclePhase(
    val id: String,
    val title: String,
    val description: String,
    val focus: String,
    val range: IntRange,
    val recommendations: List<String>,
    val icon: PhaseIcon,
    val gradient: Pair<Color, Color>
)

private enum class PhaseIcon(val imageVector: androidx.compose.ui.graphics.vector.ImageVector) {
    Menstrual(Icons.Filled.Spa),
    Follicular(Icons.Filled.Bolt),
    Ovulation(Icons.Filled.LocalFireDepartment),
    Luteal(Icons.Filled.SelfImprovement)
}


