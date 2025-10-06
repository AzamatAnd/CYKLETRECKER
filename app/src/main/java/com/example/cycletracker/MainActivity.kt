package com.example.cycletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cycletracker.ui.theme.CycleTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CycleTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }

                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                currentRoute = currentScreen,
                                onNavigationSelected = { screen -> currentScreen = screen }
                            )
                        }
                    ) { padding ->
                        when (currentScreen) {
                            "home" -> HomeScreen(modifier = Modifier.padding(padding))
                            "calendar" -> CalendarScreen(modifier = Modifier.padding(padding))
                            else -> HomeScreen(modifier = Modifier.padding(padding))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigationSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onNavigationSelected("home") },
            icon = { Icon(Icons.Default.Home, "Главная") },
            label = { Text("Главная") }
        )
        NavigationBarItem(
            selected = currentRoute == "calendar",
            onClick = { onNavigationSelected("calendar") },
            icon = { Icon(Icons.Default.CalendarToday, "Календарь") },
            label = { Text("Календарь") }
        )
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "🌸 Женский календарь",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Приложение для отслеживания менструального цикла",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(
            text = "📅 Календарь",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Здесь будет календарь для отслеживания цикла",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}