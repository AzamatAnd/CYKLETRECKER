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
import com.example.cycletracker.ar.ARManager
import com.example.cycletracker.ar.ARFeature
import com.example.cycletracker.ar.ARMode
import com.example.cycletracker.ar.EducationalTopic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARVRScreen(
    arManager: ARManager
) {
    val arState by arManager.arState.collectAsState()
    var selectedMode by remember { mutableIntStateOf(0) }
    
    val arFeatures = arManager.getAvailableARFeatures()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "🥽 AR/VR Технологии",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Status Card
        ARStatusCard(arState = arState)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // AR Features
        Text(
            text = "Доступные функции",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(arFeatures) { feature ->
                ARFeatureCard(
                    feature = feature,
                    isSelected = selectedMode == arFeatures.indexOf(feature),
                    onClick = { 
                        selectedMode = arFeatures.indexOf(feature)
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action Buttons
        if (selectedMode >= 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { 
                        // Запуск AR функции
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00E5FF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Запустить",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                OutlinedButton(
                    onClick = { 
                        selectedMode = -1
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF00E5FF)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Отмена",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ARStatusCard(arState: com.example.cycletracker.ar.ARState) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ViewInAr,
                    contentDescription = null,
                    tint = if (arState.isARSupported) Color(0xFF00E5FF) else Color(0xFF666666),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = "AR/VR Статус",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            StatusRow(
                label = "AR Поддержка",
                isActive = arState.isARSupported,
                icon = Icons.Default.CameraAlt
            )
            
            StatusRow(
                label = "AR Сессия",
                isActive = arState.isARSessionActive,
                icon = Icons.Default.PlayArrow
            )
            
            StatusRow(
                label = "Визуализация",
                isActive = arState.isVisualizationActive,
                icon = Icons.Default.Visibility
            )
            
            StatusRow(
                label = "Консультация",
                isActive = arState.isConsultationActive,
                icon = Icons.Default.Person
            )
        }
    }
}

@Composable
fun StatusRow(
    label: String,
    isActive: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (isActive) Color(0xFF4CAF50) else Color(0xFF666666),
            modifier = Modifier.size(16.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Icon(
            if (isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isActive) Color(0xFF4CAF50) else Color(0xFF666666),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ARFeatureCard(
    feature: ARFeature,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF1A3A3A) else Color(0xFF1A1A1A)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Feature Icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF00E5FF),
                                Color(0xFF00E5FF).copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = feature.icon,
                    fontSize = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Feature Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = feature.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = feature.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            // Status Indicator
            Icon(
                if (feature.isAvailable) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                tint = if (feature.isAvailable) Color(0xFF4CAF50) else Color(0xFF666666),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun EducationalVRContent() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "VR Образовательные материалы",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        items(EducationalTopic.values().toList()) { topic ->
            EducationalTopicCard(topic = topic)
        }
    }
}

@Composable
fun EducationalTopicCard(topic: EducationalTopic) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.School,
                    contentDescription = null,
                    tint = Color(0xFF00E5FF),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Text(
                    text = topic.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = topic.description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00E5FF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Начать VR сессию",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VirtualConsultationContent() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Виртуальные консультации",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            DoctorCard(
                name = "Др. Анна Петрова",
                specialty = "Гинеколог",
                rating = 4.9f,
                experience = "15 лет опыта",
                isAvailable = true
            )
        }
        
        item {
            DoctorCard(
                name = "Др. Мария Сидорова",
                specialty = "Эндокринолог",
                rating = 4.8f,
                experience = "12 лет опыта",
                isAvailable = false
            )
        }
        
        item {
            DoctorCard(
                name = "Др. Елена Козлова",
                specialty = "Репродуктолог",
                rating = 4.9f,
                experience = "18 лет опыта",
                isAvailable = true
            )
        }
    }
}

@Composable
fun DoctorCard(
    name: String,
    specialty: String,
    rating: Float,
    experience: String,
    isAvailable: Boolean
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
            // Doctor Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFF00E5FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Doctor Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = specialty,
                    color = Color(0xFF00E5FF),
                    fontSize = 14.sp
                )
                Text(
                    text = experience,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = rating.toString(),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            
            // Status and Action
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (isAvailable) "Доступен" else "Занят",
                    color = if (isAvailable) Color(0xFF4CAF50) else Color(0xFF666666),
                    fontSize = 12.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Button(
                    onClick = { },
                    enabled = isAvailable,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAvailable) Color(0xFF00E5FF) else Color(0xFF666666)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Консультация",
                        color = if (isAvailable) Color.Black else Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
