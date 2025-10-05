package com.example.cycletracker.ui.assistant

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    var isThinking by remember { mutableStateOf(false) }
    var showInsights by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        delay(500)
        showInsights = true
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🤖 Умный помощник") },
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
        containerColor = Color(0xFFFFF0F5)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // AI Avatar Card
            item {
                AnimatedVisibility(
                    visible = showInsights,
                    enter = fadeIn() + slideInVertically()
                ) {
                    AIAvatarCard()
                }
            }
            
            // Quick Stats
            item {
                AnimatedVisibility(
                    visible = showInsights,
                    enter = fadeIn() + slideInVertically()
                ) {
                    QuickStatsCard()
                }
            }
            
            // Personalized Insights
            item {
                Text(
                    text = "Персональные инсайты",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )
            }
            
            items(getPersonalizedInsights()) { insight ->
                AnimatedVisibility(
                    visible = showInsights,
                    enter = fadeIn() + slideInVertically()
                ) {
                    InsightCard(insight)
                }
            }
            
            // Recommendations
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Рекомендации на сегодня",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )
            }
            
            items(getTodayRecommendations()) { recommendation ->
                AnimatedVisibility(
                    visible = showInsights,
                    enter = fadeIn() + slideInVertically()
                ) {
                    RecommendationCard(recommendation)
                }
            }
        }
    }
}

@Composable
fun AIAvatarCard() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6A1B9A),
                            Color(0xFF9C27B0),
                            Color(0xFFBA68C8)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🤖",
                        fontSize = 48.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Привет! Я Мария",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Ваш персональный AI-помощник",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Я анализирую ваши данные и даю умные советы",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun QuickStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
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
                    text = "Ваш профиль",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )
                
                Text(
                    text = "Точность: 87%",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem("📊", "3", "Цикла")
                QuickStatItem("📝", "12", "Записей")
                QuickStatItem("🎯", "87%", "Точность")
            }
        }
    }
}

@Composable
fun QuickStatItem(emoji: String, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = emoji, fontSize = 32.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A1B9A)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun InsightCard(insight: Insight) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(insight.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = insight.icon,
                    contentDescription = null,
                    tint = insight.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = insight.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = insight.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                
                if (insight.tip != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                insight.color.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "💡 ${insight.tip}",
                            fontSize = 13.sp,
                            color = insight.color,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(recommendation: Recommendation) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = recommendation.color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = recommendation.emoji,
                fontSize = 32.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recommendation.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = recommendation.color
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recommendation.description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

data class Insight(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val tip: String?,
    val color: Color
)

data class Recommendation(
    val emoji: String,
    val title: String,
    val description: String,
    val color: Color
)

fun getPersonalizedInsights(): List<Insight> {
    return listOf(
        Insight(
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            title = "Паттерн обнаружен",
            description = "Вы часто чувствуете усталость на 3-4 день цикла. Это нормально из-за гормональных изменений.",
            tip = "Планируйте меньше дел в эти дни и больше отдыхайте",
            color = Color(0xFF2196F3)
        ),
        Insight(
            icon = Icons.Default.Favorite,
            title = "Настроение улучшается",
            description = "После прогулок ваше настроение стабильно лучше на 30%. Физическая активность помогает!",
            tip = "Старайтесь гулять 20-30 минут каждый день",
            color = Color(0xFFE91E63)
        ),
        Insight(
            icon = Icons.Default.Warning,
            title = "Прогноз симптомов",
            description = "Через 2-3 дня возможны головные боли (вероятность 75% на основе ваших данных).",
            tip = "Подготовьте обезболивающее и избегайте стресса",
            color = Color(0xFFFF9800)
        ),
        Insight(
            icon = Icons.Default.Star,
            title = "Лучшие дни впереди",
            description = "С 10 по 15 день цикла у вас обычно отличное самочувствие и высокая энергия!",
            tip = "Планируйте важные дела на эти дни",
            color = Color(0xFF4CAF50)
        )
    )
}

fun getTodayRecommendations(): List<Recommendation> {
    return listOf(
        Recommendation(
            emoji = "💧",
            title = "Пейте больше воды",
            description = "Сегодня важно поддерживать водный баланс",
            color = Color(0xFF2196F3)
        ),
        Recommendation(
            emoji = "🧘",
            title = "Лёгкая йога или растяжка",
            description = "Поможет снять напряжение и улучшить настроение",
            color = Color(0xFF9C27B0)
        ),
        Recommendation(
            emoji = "🥗",
            title = "Больше овощей и фруктов",
            description = "Витамины помогут чувствовать себя лучше",
            color = Color(0xFF4CAF50)
        ),
        Recommendation(
            emoji = "😴",
            title = "Спите 7-8 часов",
            description = "Полноценный сон особенно важен сейчас",
            color = Color(0xFFFF9800)
        )
    )
}
