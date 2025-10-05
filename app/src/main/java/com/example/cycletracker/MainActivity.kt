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
import com.example.cycletracker.ui.assistant.AssistantScreen
import com.example.cycletracker.ui.calendar.CalendarScreen
import com.example.cycletracker.ui.home.HomeScreen
import com.example.cycletracker.ui.notes.NotesScreen
import com.example.cycletracker.ui.settings.SettingsScreen
import com.example.cycletracker.ui.statistics.StatisticsScreen
import com.example.cycletracker.ui.theme.CycleTrackerTheme
import com.example.cycletracker.ui.mood.MoodScreen
import com.example.cycletracker.ui.meds.MedicationsScreen
import com.example.cycletracker.ui.phases.PhasesScreen

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
                            onNavigateToCalendar = { currentScreen = "calendar" },
                            onNavigateToStatistics = { currentScreen = "statistics" },
                            onNavigateToNotes = { currentScreen = "notes" },
                            onNavigateToAssistant = { currentScreen = "assistant" },
                            onNavigateToSettings = { currentScreen = "settings" },
                            onNavigateToMood = { currentScreen = "mood" },
                            onNavigateToMeds = { currentScreen = "meds" },
                            onNavigateToPhases = { currentScreen = "phases" }
                        )
                        "calendar" -> CalendarScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "statistics" -> StatisticsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "notes" -> NotesScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "assistant" -> AssistantScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "settings" -> SettingsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "phases" -> PhasesScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "mood" -> MoodScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                        "meds" -> MedicationsScreen(
                            viewModel = viewModel,
                            onNavigateBack = { currentScreen = "home" }
                        )
                    }
                }
            }
        }
    }
}