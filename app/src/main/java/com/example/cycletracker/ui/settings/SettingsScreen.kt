package com.example.cycletracker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cycletracker.ui.CycleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: CycleViewModel, onNavigateBack: () -> Unit) {
    var cycleStartNotification by remember { mutableStateOf(true) }
    var cycleSoonNotification by remember { mutableStateOf(true) }
    var shoppingReminder by remember { mutableStateOf(true) }
    var healthTips by remember { mutableStateOf(true) }
    var motivationalMessages by remember { mutableStateOf(false) }
    
    var shoppingReminderDays by remember { mutableStateOf(3) }
    var cycleSoonDays by remember { mutableStateOf(2) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки уведомлений") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE91E63)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Умные уведомления",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Настройте напоминания под себя",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
            
            // Main notifications
            item {
                Text(
                    text = "Основные уведомления",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
            }
            
            item {
                SettingItem(
                    icon = Icons.Default.Circle,
                    title = "Начало цикла",
                    description = "Уведомление в день начала цикла",
                    checked = cycleStartNotification,
                    onCheckedChange = { cycleStartNotification = it },
                    iconColor = Color(0xFFE91E63)
                )
            }
            
            item {
                SettingItem(
                    icon = Icons.Default.Schedule,
                    title = "Скоро начало цикла",
                    description = "За $cycleSoonDays ${getDaysWord(cycleSoonDays)} до начала",
                    checked = cycleSoonNotification,
                    onCheckedChange = { cycleSoonNotification = it },
                    iconColor = Color(0xFF9C27B0)
                )
            }
            
            if (cycleSoonNotification) {
                item {
                    DaysPickerCard(
                        title = "За сколько дней уведомлять",
                        days = cycleSoonDays,
                        onDaysChange = { cycleSoonDays = it }
                    )
                }
            }
            
            // Shopping reminder
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🛒 Напоминание о покупках",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
            }
            
            item {
                SettingItem(
                    icon = Icons.Default.ShoppingCart,
                    title = "Напоминание о прокладках",
                    description = "За $shoppingReminderDays ${getDaysWord(shoppingReminderDays)} до начала цикла",
                    checked = shoppingReminder,
                    onCheckedChange = { shoppingReminder = it },
                    iconColor = Color(0xFFFF9800)
                )
            }
            
            if (shoppingReminder) {
                item {
                    DaysPickerCard(
                        title = "За сколько дней напомнить",
                        days = shoppingReminderDays,
                        onDaysChange = { shoppingReminderDays = it }
                    )
                }
                
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFF9800).copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "💡 Что напомним купить:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF9800)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• Прокладки/тампоны\n" +
                                        "• Обезболивающие\n" +
                                        "• Грелка\n" +
                                        "• Любимые снеки 🍫",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
            
            // Additional notifications
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Дополнительно",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
            }
            
            item {
                SettingItem(
                    icon = Icons.Default.Lightbulb,
                    title = "Советы от Марии",
                    description = "Полезные советы на основе ваших данных",
                    checked = healthTips,
                    onCheckedChange = { healthTips = it },
                    iconColor = Color(0xFF4CAF50)
                )
            }
            
            item {
                SettingItem(
                    icon = Icons.Default.FavoriteBorder,
                    title = "Мотивационные сообщения",
                    description = "Позитивные напоминания каждый день",
                    checked = motivationalMessages,
                    onCheckedChange = { motivationalMessages = it },
                    iconColor = Color(0xFFE91E63)
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    iconColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = iconColor,
                    checkedTrackColor = iconColor.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
fun DaysPickerCard(
    title: String,
    days: Int,
    onDaysChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(1, 2, 3, 5, 7).forEach { day ->
                    DayButton(
                        day = day,
                        selected = days == day,
                        onClick = { onDaysChange(day) }
                    )
                }
            }
        }
    }
}

@Composable
fun DayButton(
    day: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(
                if (selected) Color(0xFFE91E63) else Color(0xFFF5F5F5),
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = day.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color.Black
            )
            Text(
                text = getDaysWord(day),
                fontSize = 10.sp,
                color = if (selected) Color.White else Color.Gray
            )
        }
    }
}

private fun getDaysWord(days: Int): String {
    return when {
        days % 10 == 1 && days % 100 != 11 -> "день"
        days % 10 in 2..4 && days % 100 !in 12..14 -> "дня"
        else -> "дней"
    }
}
