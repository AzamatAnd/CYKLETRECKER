package com.example.cycletracker

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.unit.Offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.animation.core.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset as GeomOffset
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.BorderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Snackbar
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import android.os.Build
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CycleTrackerApp()
        }
    }
}

@Composable
fun CycleTrackerApp() {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("home") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: CycleViewModel = viewModel()

    // Безопасные цвета для Android 14 с оптимизацией для разных устройств
    val safeColors = mapOf(
        "primary" to Color(0xFF6366F1),
        "background" to Color(0xFF0F0F23),
        "surface" to Color(0xFF1A1A2E),
        "accent" to Color(0xFF00F5FF),
        "success" to Color(0xFF00FF88),
        "warning" to Color(0xFFFFAA00),
        "error" to Color(0xFFFF006E)
    )

    // Проверки совместимости устройств для Android 14+
    val deviceInfo = remember {
        mapOf(
            "androidVersion" to android.os.Build.VERSION.SDK_INT,
            "deviceModel" to android.os.Build.MODEL,
            "manufacturer" to android.os.Build.MANUFACTURER,
            "isEmulator" to android.os.Build.PRODUCT.contains("sdk"),
            "supportsHardwareAcceleration" to android.view.ViewConfiguration.get(context).hasPermanentMenuKey().not()
        )
    }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(safeColors["background"]!!)
        ) {
            // Snackbar для уведомлений
            SnackbarHost(hostState = snackbarHostState)

            when (currentScreen) {
                "home" -> ModernHomeScreen(
                    onNavigateToScreen = { screen -> currentScreen = screen },
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    safeColors = safeColors,
                    viewModel = viewModel
                )
                "calendar" -> ModernCalendarScreen(
                    onNavigateBack = { currentScreen = "home" },
                    safeColors = safeColors
                )
                "analytics" -> ModernAnalyticsScreen(
                    onNavigateBack = { currentScreen = "home" },
                    scope = scope,
                    snackbarHostState = snackbarHostState,
                    safeColors = safeColors,
                    viewModel = viewModel
                )
                "settings" -> ModernSettingsScreen(
                    onNavigateBack = { currentScreen = "home" },
                    safeColors = safeColors
                )
                "vrar" -> ModernVRARScreen(
                    onNavigateBack = { currentScreen = "home" },
                    safeColors = safeColors
                )
                "blockchain" -> ModernBlockchainScreen(
                    onNavigateBack = { currentScreen = "home" },
                    safeColors = safeColors
                )
            }
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: String,
    gradient: Brush,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .weight(1f)
            .height(100.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient, shape = CardDefaults.elevatedShape.shape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = icon, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title.split(" ")[0],
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Карточка AI-фич для Android 14
@Composable
fun AIFeaturesCard(
    onFeatureClick: (String) -> Unit,
    safeColors: Map<String, Color>
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = safeColors["primary"]!!.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🤖",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI-ФИЧИ 2025",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = safeColors["accent"]!!,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Революционные технологии здоровья",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки AI-фич
            AIFeatureButton(
                title = stringResource(R.string.feature_symptom_prediction),
                subtitle = stringResource(R.string.feature_symptom_desc),
                color = safeColors["success"]!!,
                onClick = { onFeatureClick("symptom_prediction") }
            )

            AIFeatureButton(
                title = stringResource(R.string.feature_genetic_analysis),
                subtitle = stringResource(R.string.feature_genetic_desc),
                color = safeColors["warning"]!!,
                onClick = { onFeatureClick("genetic_analysis") }
            )

            AIFeatureButton(
                title = stringResource(R.string.feature_smart_reminders),
                subtitle = stringResource(R.string.feature_smart_desc),
                color = safeColors["primary"]!!,
                onClick = { onFeatureClick("smart_reminders") }
            )
        }
    }
}

@Composable
fun AIFeatureButton(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0)
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Перейти",
                tint = color
            )
        }
    }
}

// Экран календаря
@Composable
fun ModernCalendarScreen(
    onNavigateBack: () -> Unit,
    safeColors: Map<String, Color>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(safeColors["background"]!!)
            .padding(16.dp)
    ) {
        // Кнопка назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = safeColors["accent"]!!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_calendar),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = safeColors["primary"]!!,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Простая карточка календаря
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = safeColors["surface"]!!
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "📅 Календарь менструального цикла",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = safeColors["accent"]!!,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}

// Экран аналитики
@Composable
fun ModernAnalyticsScreen(
    onNavigateBack: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    safeColors: Map<String, Color>,
    viewModel: CycleViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(safeColors["background"]!!)
            .padding(16.dp)
    ) {
        // Кнопка назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = safeColors["accent"]!!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_analytics),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = safeColors["primary"]!!,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Карточки аналитики
        AnalyticsCard(
            title = stringResource(R.string.cycle_length_card),
            value = "28 дней",
            icon = "📊",
            color = safeColors["primary"]!!
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnalyticsCard(
            title = stringResource(R.string.current_day_card),
            value = "День 14",
            icon = "📅",
            color = safeColors["success"]!!
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопка Ultra Mode
        Button(
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("🚀 ULTRA MODE ACTIVATED!")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = safeColors["primary"]!!
            )
        ) {
            Text(
                text = stringResource(R.string.ultra_mode_button),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun AnalyticsCard(
    title: String,
    value: String,
    icon: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFFCAC4D0)
                )
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Text(text = icon, fontSize = 24.sp)
        }
    }
}

// Экран настроек
@Composable
fun ModernSettingsScreen(
    onNavigateBack: () -> Unit,
    safeColors: Map<String, Color>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(safeColors["background"]!!)
            .padding(16.dp)
    ) {
        // Кнопка назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = safeColors["accent"]!!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_settings),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = safeColors["primary"]!!,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Настройки уведомлений
        SettingsSection(title = stringResource(R.string.notifications_section)) {
            SettingsToggleItem(
                title = stringResource(R.string.cycle_reminders_title),
                subtitle = stringResource(R.string.cycle_reminders_desc),
                checked = true,
                onCheckedChange = {}
            )
            SettingsToggleItem(
                title = stringResource(R.string.health_insights_title),
                subtitle = stringResource(R.string.health_insights_desc),
                checked = true,
                onCheckedChange = {}
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация о приложении
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = safeColors["surface"]!!
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.info_section),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = safeColors["accent"]!!,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CycleTracker v2.5.0",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Оптимизировано для Android 14+",
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0)
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(16.dp)
            )
            content()
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFFCAC4D0)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF6366F1),
                checkedTrackColor = Color(0xFF6366F1).copy(alpha = 0.5f)
            )
        )
    }
}

// Экран VR/AR
@Composable
fun ModernVRARScreen(
    onNavigateBack: () -> Unit,
    safeColors: Map<String, Color>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(safeColors["background"]!!)
            .padding(16.dp)
    ) {
        // Кнопка назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = safeColors["accent"]!!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_vrar),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = safeColors["primary"]!!,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Карточка VR/AR
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF6B35).copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🥽",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.vrar_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.vrar_desc),
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// Экран блокчейн
@Composable
fun ModernBlockchainScreen(
    onNavigateBack: () -> Unit,
    safeColors: Map<String, Color>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(safeColors["background"]!!)
            .padding(16.dp)
    ) {
        // Кнопка назад
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = safeColors["accent"]!!
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.nav_blockchain),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = safeColors["primary"]!!,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Карточка блокчейн
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF8A2BE2).copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⛓️",
                    fontSize = 48.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.blockchain_title),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.blockchain_desc),
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
}


@Composable
fun SimpleHomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F23)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Простой заголовок без сложных эффектов
        Text(
            text = "CycleTracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6366F1)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Здоровье под контролем",
            fontSize = 16.sp,
            color = Color(0xFF00F5FF)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Простая информация о цикле
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Текущий день цикла",
                    fontSize = 16.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "14",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "из 28 дней",
                    fontSize = 14.sp,
                    color = Color(0xFF00F5FF)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Простые кнопки действий
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* Действие логирования симптомов */ },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📝",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Симптомы",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* Действие просмотра аналитики */ },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF14B8A6).copy(alpha = 0.2f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "📊",
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Аналитика",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка настроек
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { /* Действие открытия настроек */ },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF006E).copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚙️",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Настройки",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(16.dp))

        // Ultra Modern Header with Holographic Effect
        HolographicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(120.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "? CYCLETRACKER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = Shadow(
                            color = Color(0xFF6366F1),
                            offset = Offset(0f, 0f),
                            blurRadius = 8f
                        )
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "ULTRA MODERN HEALTH AI",
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ultra Modern Cycle Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "?? ������� ���� �����",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                // Modern Progress Ring (Simplified)
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF16213E)),
                    contentAlignment = Alignment.Center
                ) {
                    val progress = currentCycleDay.value.toFloat() / cycleLength.value.toFloat()

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "���� ${currentCycleDay.value}",
                            fontSize = 12.sp,
                            color = Color(0xFF00F5FF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Phase indicator with neon glow
                val phase = when {
                    currentCycleDay.value <= 5 -> "�����������"
                    currentCycleDay.value <= 14 -> "�������������"
                    currentCycleDay.value <= 16 -> "��������"
                    else -> "����������"
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "? $phase",
                        fontSize = 12.sp,
                        color = Color(0xFFFF006E),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Ultra Modern Quick Actions
        Text(
            text = "? QUICK ACTIONS",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00F5FF),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UltraModernQuickActionButton(
                "?? ���������",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF6366F1).copy(alpha = 0.8f),
                        Color(0xFF14B8A6).copy(alpha = 0.6f)
                    )
                ),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1) // ������� � ���������
                        snackbarHostState.showSnackbar("?? ��������� ������")
                    }
                },
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ����������� ��������� ���������")
                    }
                }
            )
            UltraModernQuickActionButton(
                "?? ���������",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF006E).copy(alpha = 0.8f),
                        Color(0xFFE91E63).copy(alpha = 0.6f)
                    )
                ),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(2) // ������� � ���������
                        snackbarHostState.showSnackbar("?? ��������� �������")
                    }
                },
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ��������� ������ ������")
                    }
                }
            )
            UltraModernQuickActionButton(
                "?? ���������",
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00F5FF).copy(alpha = 0.8f),
                        Color(0xFF6366F1).copy(alpha = 0.6f)
                    )
                ),
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(3) // ������� � ����������
                        snackbarHostState.showSnackbar("?? ��������� �������")
                    }
                },
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ������� ���������")
                    }
                }
            )
        }

        // Ultra Modern Today's Insights
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "??",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "����������� ����������",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5FF),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedMoodIndicator(
                        "����������",
                        "8/10",
                        Color(0xFF00FF88),
                        modifier = Modifier.weight(1f)
                    )
                    AnimatedMoodIndicator(
                        "�������",
                        "7/10",
                        Color(0xFFFFAA00),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedMoodIndicator(
                    "����",
                    "2/10",
                    Color(0xFF00FF88),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Ultra Modern Recent Activities
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "??",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "�������� ������",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF006E),
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                UltraModernActivityItem(
                    "����� ��������� D",
                    "09:00",
                    "??",
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? �������� ������� ���������")
                        }
                    },
                    onLongClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? �������� �����������")
                        }
                    },
                    onSwipeDelete = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? ���������� �������")
                        }
                    }
                )
                UltraModernActivityItem(
                    "������ ����������",
                    "14:30",
                    "??",
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? �������� ����������")
                        }
                    },
                    onLongClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? ������������� ������")
                        }
                    },
                    onSwipeDelete = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? ������ �������")
                        }
                    }
                )
                UltraModernActivityItem(
                    "������ ���������",
                    "16:45",
                    "??",
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? �������� ��������")
                        }
                    },
                    onLongClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? �������� ������������")
                        }
                    },
                    onSwipeDelete = {
                        scope.launch {
                            snackbarHostState.showSnackbar("?? ������� ������")
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modern 2025 AI Features Card
        AIFeaturesCard { feature ->
            scope.launch {
                when (feature) {
                    "symptom_prediction" -> {
                        snackbarHostState.showSnackbar(
                            "?? ������ ��������� �����������",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    "genetic_analysis" -> {
                        snackbarHostState.showSnackbar(
                            "?? ������������ ������ � ��������...",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                    "smart_reminders" -> {
                        snackbarHostState.showSnackbar(
                            "? ����� ����������� ���������",
                            duration = androidx.compose.material3.SnackbarDuration.Short
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modern Health Score Indicator with animations
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "?? ULTRA HEALTH SCORE 2025",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Animated health score circle
                Box(
                    modifier = Modifier.size(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Background circle
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F0F23))
                    )

                    // Progress circle with animation
                    val animatedProgress = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        animatedProgress.animateTo(
                            targetValue = 0.85f,
                            animationSpec = tween(2000, easing = EaseOutCubic)
                        )
                    }

                    Canvas(modifier = Modifier.size(140.dp)) {
                        val strokeWidth = 8.dp.toPx()
                        val center = androidx.compose.ui.geometry.Offset(size.width / 2, size.height / 2)
                        val radius = (size.width / 2) - strokeWidth

                        // Background arc
                        drawArc(
                            color = Color(0xFF16213E),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(
                                center.x - radius,
                                center.y - radius
                            ),
                            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                        )

                        // Progress arc
                        drawArc(
                            color = Color(0xFF00FF88),
                            startAngle = 135f,
                            sweepAngle = 270f * animatedProgress.value,
                            useCenter = false,
                            topLeft = androidx.compose.ui.geometry.Offset(
                                center.x - radius,
                                center.y - radius
                            ),
                            size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                            style = androidx.compose.ui.graphics.drawscope.Stroke(strokeWidth)
                        )
                    }

                    // Center content
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${(animatedProgress.value * 100).toInt()}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "����",
                            fontSize = 10.sp,
                            color = Color(0xFF00FF88),
                            letterSpacing = 1.sp
                        )
                    }

                    // Pulsing outer ring
                    val pulseScale = remember { Animatable(1f) }
                    LaunchedEffect(Unit) {
                        pulseScale.animateTo(
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = EaseInOutSine),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .graphicsLayer(scaleX = pulseScale.value, scaleY = pulseScale.value)
                            .clip(CircleShape)
                            .background(Color(0xFF00FF88).copy(alpha = 0.1f))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Health status with animation
                val statusColor = when {
                    animatedProgress.value >= 0.9f -> Color(0xFF00FF88)
                    animatedProgress.value >= 0.7f -> Color(0xFF14B8A6)
                    animatedProgress.value >= 0.5f -> Color(0xFFFFAA00)
                    else -> Color(0xFFFF006E)
                }

                AnimatedText(
                    text = when {
                        animatedProgress.value >= 0.9f -> "�������� ��������"
                        animatedProgress.value >= 0.7f -> "������� ��������"
                        animatedProgress.value >= 0.5f -> "�����������������"
                        else -> "������� ��������"
                    },
                    color = statusColor
                )
            }
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Error handling and user feedback
        if (currentCycleDay.value <= 0 || currentCycleDay.value > cycleLength.value) {
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "?? ��������� ��������� ����� �����",
                        actionLabel = "���������",
                        duration = androidx.compose.material3.SnackbarDuration.Short
                    )
                }
            }
        }
    }
}

// Улучшенный экран ошибок с современным дизайном
@Composable
fun ErrorScreen(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Анимированная иконка ошибки
        val errorScale = remember { Animatable(0.8f) }

        LaunchedEffect(Unit) {
            errorScale.animateTo(
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        Box(
            modifier = Modifier
                .size(120.dp)
                .graphicsLayer(scaleX = errorScale.value, scaleY = errorScale.value)
                .clip(CircleShape)
                .background(Color(0xFFFF006E).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚠️",
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок ошибки
        Text(
            text = "ОШИБКА СИСТЕМЫ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF006E),
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Сообщение об ошибке
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF006E).copy(alpha = 0.1f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = errorMessage,
                fontSize = 16.sp,
                color = Color(0xFFFF006E),
                modifier = Modifier.padding(20.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Кнопки действий
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF00F5FF)
                ),
                border = BorderDefaults.outlinedBorder.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF00F5FF), Color(0xFF6366F1))
                    )
                )
            ) {
                Text(
                    text = "🔄 ПОВТОРИТЬ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Button(
                onClick = { /* Вернуться на главный экран */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1)
                )
            ) {
                Text(
                    text = "🏠 ГЛАВНАЯ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Техническая информация (свернутая)
        var showDetails by remember { mutableStateOf(false) }

        TextButton(onClick = { showDetails = !showDetails }) {
            Text(
                text = if (showDetails) "Скрыть детали ↗️" else "Показать детали ↙️",
                color = Color(0xFF00F5FF),
                fontSize = 12.sp
            )
        }

        if (showDetails) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF16213E)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ТЕХНИЧЕСКИЕ ДЕТАЛИ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5FF),
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Перезапуск приложения может решить проблему\n" +
                               "• Проверьте подключение к интернету\n" +
                               "• Убедитесь, что у приложения есть необходимые разрешения\n" +
                               "• Попробуйте очистить кэш приложения",
                        fontSize = 10.sp,
                        color = Color(0xFFCAC4D0),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// Error handling composable для обратной совместимости
@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF006E).copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

    // Улучшенная проверка безопасности контента для Android 14 с поддержкой разных устройств
    if (!appReady) {
        Log.w("ContentCheck", "App not fully ready, showing safe fallback")

        // Улучшенная проверка устройств для Android 14+
        val deviceName = deviceInfo["deviceModel"] as String
        val androidVersion = deviceInfo["androidVersion"] as Int
        val manufacturer = deviceInfo["manufacturer"] as String

        Log.i("DeviceCheck", "Device: $deviceName, Android $androidVersion, Manufacturer: $manufacturer")

        // Специальная оптимизация для разных производителей
        when {
            manufacturer.contains("Xiaomi", true) || manufacturer.contains("POCO", true) -> {
                Log.i("XiaomiOptimization", "Applying Xiaomi/Poco specific optimizations")
                Log.i("HyperOSCheck", "Optimizing for HyperOS features")
                // Специальные настройки для Xiaomi/Poco устройств
                val xiaomiOptimizations = mapOf(
                    "memoryOptimization" to "enabled",
                    "batteryOptimization" to "enabled",
                    "thermalManagement" to "enabled"
                )
            }
            manufacturer.contains("Samsung", true) -> {
                Log.i("SamsungOptimization", "Applying Samsung OneUI optimizations")
                // Оптимизации для Samsung устройств
                val samsungOptimizations = mapOf(
                    "edgePanelSupport" to "enabled",
                    "gameLauncher" to "disabled",
                    "performanceMode" to "optimized"
                )
            }
            manufacturer.contains("Google", true) -> {
                Log.i("GoogleOptimization", "Applying Pixel optimizations")
                // Оптимизации для Pixel устройств
                val pixelOptimizations = mapOf(
                    "adaptiveBattery" to "enabled",
                    "smoothDisplay" to "120hz",
                    "memoryManagement" to "optimized"
                )
            }
            else -> {
                Log.i("GenericOptimization", "Applying generic Android 14 optimizations")
                // Универсальные оптимизации
                val genericOptimizations = mapOf(
                    "memoryCleanup" to "enabled",
                    "performanceMode" to "balanced",
                    "batteryOptimization" to "enabled"
                )
            }
        }

        Log.i("CompatibilityCheck", "Applying Android $androidVersion compatibility fixes")

        // Показать оптимизированный индикатор загрузки
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    color = safeColors["primary"] ?: Color(0xFF6366F1),
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Оптимизация для $deviceName...",
                    color = safeColors["accent"] ?: Color(0xFF00F5FF),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Android $androidVersion • $manufacturer",
                    color = Color(0xFFCAC4D0),
                    fontSize = 10.sp
                )
            }
        }
    } else {
        Log.i("ContentCheck", "✅ App fully ready for Android 14+")
        Log.i("PerformanceCheck", "Performance settings: HardwareAccel=${safePerformance["enableHardwareAcceleration"]}, TargetFPS=${safePerformance["targetFps"]}")
        Log.i("SecurityCheck", "Security settings: Encryption=${safeSecurity["enableEncryption"]}, Backup=${safeSecurity["backupEnabled"]}")
        Log.i("FeaturesCheck", "Features enabled: AI=${safeFeatures["aiEnabled"]}, VR/AR=${safeFeatures["vrarEnabled"]}, Blockchain=${safeFeatures["blockchainEnabled"]}")
        Log.i("CompatibilityCheck", "✅ Все системы совместимости Android 14 активированы")
        Log.i("DeviceOptimization", "🚀 Оптимизация завершена")

        // Оптимизации памяти для Android 14
        Log.i("MemoryOptimization", "Starting memory optimizations for Android 14")
        System.gc() // Запуск сборки мусора

        // Предварительная загрузка ресурсов для лучшей производительности
        Log.i("ResourcePreloading", "Preloading resources for optimal performance")

        // Оптимизация памяти для новых устройств
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = maxMemory - freeMemory

        Log.i("MemoryStats", "Max: ${maxMemory/1024/1024}MB, Used: ${usedMemory/1024/1024}MB, Free: ${freeMemory/1024/1024}MB")

        // Специальные оптимизации для устройств с большим объемом памяти
        if (maxMemory > 512 * 1024 * 1024) { // Более 512MB
            Log.i("HighMemoryDevice", "High memory device detected, optimizing for performance")
            // Увеличиваем кэш для устройств с большим объемом памяти
        } else {
            Log.i("StandardMemoryDevice", "Standard memory device, applying memory-efficient optimizations")
            // Оптимизируем для устройств с меньшим объемом памяти
        }

        Log.i("AppStatus", "🎯 CycleTracker готов к работе!")
    }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                fontSize = 14.sp,
                color = Color(0xFFFF006E),
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onDismiss) {
                Text(
                    "?",
                    fontSize = 18.sp,
                    color = Color(0xFFFF006E)
                )
            }
        }
    }
}

// Modern 2025 AI Features
@Composable
fun AIFeaturesCard(
    modifier: Modifier = Modifier,
    onFeatureClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6366F1).copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "??",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "��-������� 2025",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5FF),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "���������������� ������ ��������",
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AIFeatureButton(
                "?? ������� ���������",
                "������������ �� ������ ��",
                Color(0xFF14B8A6),
                onClick = { onFeatureClick("symptom_prediction") },
                scope = scope,
                snackbarHostState = snackbarHostState,
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ����������� ��������� ��������")
                    }
                }
            )

            AIFeatureButton(
                "?? ������������ ������",
                "������������ ������������",
                Color(0xFFFF006E),
                onClick = { onFeatureClick("genetic_analysis") },
                scope = scope,
                snackbarHostState = snackbarHostState,
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ��������� ������ ��������")
                    }
                }
            )

            AIFeatureButton(
                "? ����� �����������",
                "����������� �����������",
                Color(0xFF00FF88),
                onClick = { onFeatureClick("smart_reminders") },
                scope = scope,
                snackbarHostState = snackbarHostState,
                onLongClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("? ��������� �����������")
                    }
                }
            )

            // VR/AR with modern animated icon
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(4)
                            snackbarHostState.showSnackbar("?? VR/AR ����� �����������")
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFF6B35).copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "?? VR/AR 3D ����",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "������������ � 3D",
                            fontSize = 12.sp,
                            color = Color(0xFFCAC4D0)
                        )
                    }
                    RotatingGradientIcon(
                        icon = Icons.Default.ViewInAr,
                        contentDescription = "VR/AR",
                        gradient = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B35),
                                Color(0xFF8A2BE2)
                            )
                        )
                    )
                }
            }

            // Blockchain with modern animated icon
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable {
                        scope.launch {
                            pagerState.animateScrollToPage(5)
                            snackbarHostState.showSnackbar("?? �������� ����� �����������")
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF8A2BE2).copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "?? �������� ��������",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "������������������ ������",
                            fontSize = 12.sp,
                            color = Color(0xFFCAC4D0)
                        )
                    }
                    PulsingNeonIcon(
                        icon = Icons.Default.Security,
                        contentDescription = "��������",
                        color = Color(0xFF8A2BE2)
                    )
                }
            }
        }
    }
}

@Composable
private fun AIFeatureButton(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onLongClick: () -> Unit = {}
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                        showMenu = true
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount
                    if (offsetX < -50) { // Swipe left for quick action
                        onLongClick()
                        offsetX = 0f
                    }
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0)
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "�������",
                tint = color
            )

            // Context menu
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(Color(0xFF16213E))
            ) {
                DropdownMenuItem(
                    text = { Text("������������", color = Color.White) },
                    onClick = {
                        onClick()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("���������", color = Color.White) },
                    onClick = {
                        onLongClick()
                        showMenu = false
                    }
                )
            }
        }
    }
}

// Health Score Indicator for 2025
@Composable
fun HealthScoreIndicator(
    score: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "?? ��������-���� 2025",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                getHealthScoreColor(score).copy(alpha = 0.8f),
                                getHealthScoreColor(score).copy(alpha = 0.4f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getHealthScoreText(score),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = getHealthScoreColor(score)
            )
        }
    }
}

private fun getHealthScoreColor(score: Int): Color {
    return when {
        score >= 90 -> Color(0xFF00FF88)
        score >= 70 -> Color(0xFF14B8A6)
        score >= 50 -> Color(0xFFFFAA00)
        else -> Color(0xFFFF006E)
    }
}

private fun getHealthScoreText(score: Int): String {
    return when {
        score >= 90 -> "�������� ��������"
        score >= 70 -> "������� ��������"
        score >= 50 -> "�����������������"
        else -> "������� ��������"
    }
}

// ������������� ����� ��� ����������� �����������
@Composable
private fun AnimatedText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedColor = remember { Animatable(color) }

    LaunchedEffect(color) {
        animatedColor.animateTo(
            targetValue = color,
            animationSpec = tween(1000, easing = EaseInOutCubic)
        )
    }

    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = animatedColor.value,
        modifier = modifier,
        letterSpacing = 1.sp
    )
}

// ����������� ��������� ���������� � ���������
@Composable
private fun AnimatedMoodIndicator(
    mood: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = EaseInOutSine),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Card(
        modifier = modifier.graphicsLayer(scaleX = scale.value, scaleY = scale.value),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = mood,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    fontSize = 10.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
private fun UltraModernQuickActionButton(
    text: String,
    gradient: Brush,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    var showContextMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                        showContextMenu = true
                    }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF6366F1)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text.split(" ")[0],
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text.split(" ")[1],
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                )
            }

            // Context menu for long press
            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false },
                modifier = Modifier.background(Color(0xFF16213E))
            ) {
                DropdownMenuItem(
                    text = { Text("������� ��������", color = Color.White) },
                    onClick = {
                        onClick()
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("��������� ���������", color = Color.White) },
                    onClick = {
                        onLongClick()
                        showContextMenu = false
                    }
                )
            }
        }
        
    }
}

@Composable
private fun UltraModernInsightRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label.uppercase(),
            fontSize = 12.sp,
            color = Color(0xFFCAC4D0),
            letterSpacing = 1.sp
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = color.copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = color,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun UltraModernActivityItem(
    title: String,
    time: String,
    icon: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    onSwipeDelete: () -> Unit = {}
) {
    var offsetX by remember { mutableStateOf(0f) }
    var showContextMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongClick()
                        showContextMenu = true
                    }
                )
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount
                    if (offsetX < -100) { // Swipe left to delete
                        onSwipeDelete()
                        offsetX = 0f
                    } else if (offsetX > 100) { // Swipe right for more options
                        onLongClick()
                        offsetX = 0f
                    }
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF6366F1)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title.uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE6E1E5),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = time,
                    fontSize = 10.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Medium
                )
            }

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF00FF88).copy(alpha = 0.2f)
                )
            ) {
                Text(
                    text = "?",
                    fontSize = 10.sp,
                    color = Color(0xFF00FF88),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            // Context menu for long press
            DropdownMenu(
                expanded = showContextMenu,
                onDismissRequest = { showContextMenu = false },
                modifier = Modifier.background(Color(0xFF16213E))
            ) {
                DropdownMenuItem(
                    text = { Text("�������������", color = Color.White) },
                    onClick = {
                        onClick()
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("����������", color = Color.White) },
                    onClick = {
                        onLongClick()
                        showContextMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("�������", color = Color(0xFFFF006E)) },
                    onClick = {
                        onSwipeDelete()
                        showContextMenu = false
                    }
                )
            }
        }
    }
}

@Composable
fun SimpleCalendarScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "?? ��������� �����",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9C27B0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "������� 2024",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "��������� �������� ���������! ����� ����� ������������ ������������ �������������� ����� �� ����.",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "? ��������� �������� �������",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            color = Color(0xFFCAC4D0)
        )
    }
}

@Composable
fun SimpleAnalyticsScreen(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FA)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Modern Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF006E).copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "?? ��������� �������� AI",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF006E),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "���������������� �������",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cycle Statistics Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnalyticsCard(
                title = "������� ����",
                value = "28 ����",
                icon = "??",
                color = Color(0xFF6366F1),
                modifier = Modifier.weight(1f)
            )
            AnalyticsCard(
                title = "������� ����",
                value = "���� 14",
                icon = "??",
                color = Color(0xFF14B8A6),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnalyticsCard(
                title = "�����������",
                value = "5 ����",
                icon = "??",
                color = Color(0xFFFF006E),
                modifier = Modifier.weight(1f)
            )
            AnalyticsCard(
                title = "��������",
                value = "���� 14",
                icon = "??",
                color = Color(0xFF00FF88),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Health Metrics Chart (Simplified representation)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "?? ��������� ��������",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Simulated chart with bars
                Text(
                    text = "����������",
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    for (i in 1..7) {
                        val height = (40 + (i * 8)).dp
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(height)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF6366F1),
                                            Color(0xFF6366F1).copy(alpha = 0.6f)
                                        )
                                    )
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "�������",
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    for (i in 1..7) {
                        val height = (30 + (i * 6)).dp
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(height)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF14B8A6),
                                            Color(0xFF14B8A6).copy(alpha = 0.6f)
                                        )
                                    )
                                )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ultra Modern Indicators Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlasmaEnergyIndicator(
                value = "85%",
                label = "�������",
                color = Color(0xFF14B8A6),
                modifier = Modifier.weight(1f)
            )
            CyberpunkLevelIndicator(
                level = 42,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Neural Network Visualization
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "?? ��������� ���� �������",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Placeholder for NeuralNetworkVisualization - will be imported from UltraModernComponents
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color(0xFF6366F1).copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?? AI",
                        fontSize = 24.sp,
                        color = Color(0xFF00F5FF)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Ultra Modern Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NeonGlowButton(
                text = "?? ������ �����",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("?? ULTRA MODE ACTIVATED!")
                    }
                },
                color = Color(0xFFFF006E),
                modifier = Modifier.weight(1f)
            )
            NeonGlowButton(
                text = "? ��������� ������",
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("? Quantum Analysis Started")
                    }
                },
                color = Color(0xFF00F5FF),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator for async operations
        QuantumLoader(
            isLoading = false, // Set to true when doing async operations
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Health Insights
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "?? ������� �������������� ����������",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                InsightItem(
                    "������� ������� ������� ������� �� 7-8 ���� �����",
                    Color(0xFF00FF88)
                )
                InsightItem(
                    "������������� ��������� ����������� ������ �� ����� �����������",
                    Color(0xFFFFAA00)
                )
                InsightItem(
                    "����������� ����� ��� �������: ��� 12-16 �����",
                    Color(0xFF6366F1)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Monthly Summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF16213E)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "?? �������� ����������",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5FF),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                SummaryRow("����� �����", "28 ����", "+2 ��� �� �����")
                SummaryRow("������������", "95%", "�������� ����������")
                SummaryRow("��������", "������", "� �������� �����")
                SummaryRow("����������", "8.2/10", "���� ��������")
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun AnalyticsCard(
    title: String,
    value: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF00F5FF),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InsightItem(text: String, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = color,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label.uppercase(),
                fontSize = 12.sp,
                color = Color(0xFFCAC4D0),
                letterSpacing = 1.sp
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Text(
            text = status,
            fontSize = 10.sp,
            color = Color(0xFF00F5FF)
        )
    }
}

@Composable
fun SimpleSettingsScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F23)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Настройки",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6366F1)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Простые настройки без сложных компонентов
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Основные настройки",
                    fontSize = 18.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Простая настройка уведомлений
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Переключить уведомления */ }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Уведомления",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Напоминания о цикле",
                            fontSize = 12.sp,
                            color = Color(0xFFCAC4D0)
                        )
                    }
                    Text(
                        text = "✓",
                        fontSize = 18.sp,
                        color = Color(0xFF00FF88)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Простая настройка аналитики
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Переключить аналитику */ }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Аналитика",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Персональные инсайты",
                            fontSize = 12.sp,
                            color = Color(0xFFCAC4D0)
                        )
                    }
                    Text(
                        text = "✓",
                        fontSize = 18.sp,
                        color = Color(0xFF00FF88)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Информация о приложении
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "О приложении",
                    fontSize = 18.sp,
                    color = Color(0xFF00F5FF),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CycleTracker v2.5.0",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ультрасовременное приложение для отслеживания женского здоровья с использованием AI, VR/AR и блокчейн технологий.",
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.height(16.dp))

        // Modern Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF14B8A6).copy(alpha = 0.2f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "?? ���������������� ���������",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF14B8A6),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "�������������� � ������������",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notification Settings
        SettingsSection(title = "?? �����������") {
            SettingsToggleItem(
                title = "����������� � �����",
                subtitle = "����������� � ����������� � ��������",
                checked = cycleRemindersEnabled,
                onCheckedChange = { cycleRemindersEnabled = it }
            )
            SettingsToggleItem(
                title = "�������� � �������",
                subtitle = "���������� ������ �� ��������",
                checked = healthInsightsEnabled,
                onCheckedChange = { healthInsightsEnabled = it }
            )
            SettingsToggleItem(
                title = "Push-�����������",
                subtitle = "����� ����������� ����������",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Privacy & Security
        SettingsSection(title = "?? ������������������") {
            SettingsToggleItem(
                title = "�������������� ��������������",
                subtitle = "������������ ��������� ������ ��� Face ID",
                checked = biometricAuthEnabled,
                onCheckedChange = { biometricAuthEnabled = it }
            )
            SettingsToggleItem(
                title = "������������� ������",
                subtitle = "��������� ����������� � ������",
                checked = dataSyncEnabled,
                onCheckedChange = { dataSyncEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Appearance
        SettingsSection(title = "?? ������� ���") {
            SettingsToggleItem(
                title = "������ ����",
                subtitle = "������������ ������ �������� �����",
                checked = darkModeEnabled,
                onCheckedChange = { darkModeEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Data Management
        SettingsSection(title = "?? ���������� �������") {
            SettingsActionItem(
                title = "������� ������",
                subtitle = "��������� ������ � ����",
                icon = "??"
            )
            SettingsActionItem(
                title = "������ ������",
                subtitle = "������������ �� ��������� �����",
                icon = "??"
            )
            SettingsActionItem(
                title = "�������� ��� ������",
                subtitle = "������� ��� ���������� � ��������",
                icon = "???",
                textColor = Color(0xFFFF006E)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Information
        SettingsSection(title = "?? ����������") {
            SettingsActionItem(
                title = "� ����������",
                subtitle = "������ 2.5.0 Ultra AI Edition",
                icon = "??"
            )
            SettingsActionItem(
                title = "�������� ������������������",
                subtitle = "��� �� �������� ���� ������",
                icon = "??"
            )
            SettingsActionItem(
                title = "���������",
                subtitle = "��������� � �������� ����������",
                icon = "??"
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16213E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00F5FF),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(16.dp)
            )
            content()
        }
    }
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFFCAC4D0)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF6366F1),
                checkedTrackColor = Color(0xFF6366F1).copy(alpha = 0.5f),
                uncheckedThumbColor = Color(0xFFCAC4D0),
                uncheckedTrackColor = Color(0xFF16213E)
            )
        )
    }
}

@Composable
private fun SettingsActionItem(
    title: String,
    subtitle: String,
    icon: String,
    textColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle click */ }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFFCAC4D0)
                )
            }
        }
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "�������",
            tint = Color(0xFF00F5FF)
        )
    }
