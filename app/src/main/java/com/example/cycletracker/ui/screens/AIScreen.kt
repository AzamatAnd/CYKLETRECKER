package com.example.cycletracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.cycletracker.ai.AdvancedAIManager
import com.example.cycletracker.ai.AIState
import com.example.cycletracker.ai.NeuralNetwork
import com.example.cycletracker.ai.NetworkType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(
    aiManager: AdvancedAIManager
) {
    val aiState by aiManager.aiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var chatInput by remember { mutableStateOf("") }
    
    val tabs = listOf("Нейросети", "Анализ", "Чат", "Настройки")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "🤖 AI Ассистент",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Tab Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tabs.size) { index ->
                AITabButton(
                    text = tabs[index],
                    isSelected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content
        when (selectedTab) {
            0 -> NeuralNetworksTab(aiState)
            1 -> AnalysisTab(aiState)
            2 -> ChatTab(aiState, chatInput, onInputChange = { chatInput = it })
            3 -> AISettingsTab(aiState)
        }
    }
}

@Composable
fun AITabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF00E5FF) else Color(0xFF1A1A1A)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp
        )
    }
}

@Composable
fun NeuralNetworksTab(aiState: AIState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // AI Stats
        item {
            Text(
                text = "AI Статистика",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AIStatCard("Анализов", aiState.totalAnalyses.toString(), "📊")
                AIStatCard("Прогнозов", aiState.totalPredictions.toString(), "🔮")
                AIStatCard("Точность", "${(aiState.averageAccuracy * 100).toInt()}%", "🎯")
            }
        }
        
        // Neural Networks
        item {
            Text(
                text = "Нейронные сети",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            NeuralNetworkCard(
                name = "Прогнозирование цикла",
                type = "LSTM",
                accuracy = 0.94f,
                isTrained = true,
                description = "Предсказывает даты менструации и овуляции"
            )
        }
        
        item {
            NeuralNetworkCard(
                name = "Анализ симптомов",
                type = "CNN",
                accuracy = 0.89f,
                isTrained = true,
                description = "Анализирует симптомы и дает рекомендации"
            )
        }
        
        item {
            NeuralNetworkCard(
                name = "Прогнозирование настроения",
                type = "Transformer",
                accuracy = 0.91f,
                isTrained = true,
                description = "Предсказывает изменения настроения"
            )
        }
        
        item {
            NeuralNetworkCard(
                name = "Рекомендательная система",
                type = "GAN",
                accuracy = 0.87f,
                isTrained = false,
                description = "Генерирует персонализированные советы"
            )
        }
    }
}

@Composable
fun AIStatCard(title: String, value: String, icon: String) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp
            )
            Text(
                text = value,
                color = Color(0xFF00E5FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun NeuralNetworkCard(
    name: String,
    type: String,
    accuracy: Float,
    isTrained: Boolean,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = type,
                    color = Color(0xFF00E5FF),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Accuracy
                Column {
                    Text(
                        text = "Точность",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${(accuracy * 100).toInt()}%",
                        color = Color(0xFF4CAF50),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.width(24.dp))
                
                // Status
                Column {
                    Text(
                        text = "Статус",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = if (isTrained) "Обучена" else "Обучение",
                        color = if (isTrained) Color(0xFF4CAF50) else Color(0xFFFF9800),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Progress Bar
                if (!isTrained) {
                    LinearProgressIndicator(
                        progress = 0.7f,
                        modifier = Modifier
                            .width(60.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color(0xFF00E5FF),
                        trackColor = Color(0xFF333333)
                    )
                }
            }
        }
    }
}

@Composable
fun AnalysisTab(aiState: AIState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "AI Анализ",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        // Last Analysis
        aiState.lastAnalysis?.let { analysis ->
            item {
                AnalysisResultCard(
                    title = "Последний анализ цикла",
                    confidence = analysis.confidence,
                    recommendations = analysis.recommendations,
                    healthScore = analysis.healthScore
                )
            }
        }
        
        // Last Predictions
        aiState.lastPredictions?.let { predictions ->
            item {
                PredictionResultCard(
                    title = "Прогнозы здоровья",
                    nextPeriod = predictions.nextPeriodDate,
                    ovulation = predictions.ovulationDate,
                    fertilityScore = predictions.fertilityScore,
                    tips = predictions.personalizedTips
                )
            }
        }
        
        // Analysis Types
        item {
            Text(
                text = "Типы анализа",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            AnalysisTypeCard(
                icon = "📊",
                title = "Анализ цикла",
                description = "Глубокий анализ менструального цикла",
                isAvailable = true
            )
        }
        
        item {
            AnalysisTypeCard(
                icon = "🔍",
                title = "Анализ симптомов",
                description = "AI анализ симптомов и рекомендации",
                isAvailable = true
            )
        }
        
        item {
            AnalysisTypeCard(
                icon = "🎯",
                title = "Предиктивная аналитика",
                description = "Прогнозирование будущих событий",
                isAvailable = true
            )
        }
        
        item {
            AnalysisTypeCard(
                icon = "🧠",
                title = "Анализ настроения",
                description = "Анализ эмоционального состояния",
                isAvailable = false
            )
        }
    }
}

@Composable
fun AnalysisResultCard(
    title: String,
    confidence: Float,
    recommendations: List<String>,
    healthScore: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Уверенность: ${(confidence * 100).toInt()}%",
                    color = Color(0xFF00E5FF),
                    fontSize = 14.sp
                )
                Text(
                    text = "Здоровье: ${(healthScore * 100).toInt()}%",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Рекомендации:",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            
            recommendations.take(3).forEach { recommendation ->
                Text(
                    text = "• $recommendation",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun PredictionResultCard(
    title: String,
    nextPeriod: String,
    ovulation: String,
    fertilityScore: Float,
    tips: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "След. менструация",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = nextPeriod,
                        color = Color(0xFF00E5FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column {
                    Text(
                        text = "Овуляция",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = ovulation,
                        color = Color(0xFFFF9800),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Column {
                    Text(
                        text = "Фертильность",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${(fertilityScore * 100).toInt()}%",
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AnalysisTypeCard(
    icon: String,
    title: String,
    description: String,
    isAvailable: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAvailable) Color(0xFF1A1A1A) else Color(0xFF0A0A0A)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = if (isAvailable) Color.White else Color.White.copy(alpha = 0.5f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = if (isAvailable) Color.White.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.3f),
                    fontSize = 14.sp
                )
            }
            
            if (isAvailable) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E5FF)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Запустить",
                        color = Color.Black,
                        fontSize = 12.sp
                    )
                }
            } else {
                Text(
                    text = "Скоро",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ChatTab(
    aiState: AIState,
    chatInput: String,
    onInputChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat Messages
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ChatMessage(
                    text = "Привет! Я ваш AI ассистент по женскому здоровью. Как дела?",
                    isUser = false
                )
            }
            
            item {
                ChatMessage(
                    text = "Расскажите о вашем цикле или задайте любой вопрос!",
                    isUser = false
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Chat Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = onInputChange,
                placeholder = {
                    Text(
                        text = "Введите сообщение...",
                        color = Color.White.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00E5FF),
                    unfocusedBorderColor = Color(0xFF333333),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { },
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                )
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Отправить",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ChatMessage(
    text: String,
    isUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFF00E5FF) else Color(0xFF1A1A1A)
            )
        ) {
            Text(
                text = text,
                color = if (isUser) Color.Black else Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun AISettingsTab(aiState: AIState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "AI Настройки",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            SettingsToggleItem(
                title = "Автоматический анализ",
                description = "AI автоматически анализирует ваши данные",
                isEnabled = true
            )
        }
        
        item {
            SettingsToggleItem(
                title = "Уведомления AI",
                description = "Получать уведомления от AI ассистента",
                isEnabled = true
            )
        }
        
        item {
            SettingsToggleItem(
                title = "Обучение на данных",
                description = "Позволить AI обучаться на ваших данных",
                isEnabled = false
            )
        }
        
        item {
            SettingsToggleItem(
                title = "Анонимная аналитика",
                description = "Отправлять анонимные данные для улучшения AI",
                isEnabled = true
            )
        }
        
        item {
            Text(
                text = "Статистика обучения",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            StatItem(
                label = "Итераций обучения",
                value = aiState.learningIterations.toString()
            )
        }
        
        item {
            StatItem(
                label = "Средняя точность",
                value = "${(aiState.averageAccuracy * 100).toInt()}%"
            )
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    description: String,
    isEnabled: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            Switch(
                checked = isEnabled,
                onCheckedChange = { },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF00E5FF),
                    checkedTrackColor = Color(0xFF00E5FF).copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp
            )
            Text(
                text = value,
                color = Color(0xFF00E5FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
