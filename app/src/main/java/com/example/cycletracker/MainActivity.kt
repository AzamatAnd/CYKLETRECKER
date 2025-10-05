package com.example.cycletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.cycletracker.data.AppDatabase
import com.example.cycletracker.data.CycleRepository
import com.example.cycletracker.ui.CycleViewModel
import com.example.cycletracker.ui.calendar.CalendarScreen
import com.example.cycletracker.ui.home.HomeScreen
import com.example.cycletracker.ui.theme.CycleTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CycleRepository(database.cycleDao())
        val viewModel = CycleViewModel(repository)
        
        setContent {
            CycleTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }
                    
                    when (currentScreen) {
                        "home" -> HomeScreen(
                            viewModel = viewModel,
                            onNavigateToCalendar = { currentScreen = "calendar" }
                        )
                        "calendar" -> CalendarScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}