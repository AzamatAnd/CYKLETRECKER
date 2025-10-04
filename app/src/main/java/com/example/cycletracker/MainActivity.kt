package com.example.cycletracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cycletracker.data.export.ExportImport
import com.example.cycletracker.data.preferences.UserPreferences
import com.example.cycletracker.di.ServiceLocator
import com.example.cycletracker.ui.CycleViewModel
import com.example.cycletracker.ui.calendar.CalendarScreen
import com.example.cycletracker.ui.history.HistoryScreen
import com.example.cycletracker.ui.home.HomeScreen
import com.example.cycletracker.ui.settings.SettingsScreen
import com.example.cycletracker.ui.statistics.StatisticsScreen
import com.example.cycletracker.ui.symptoms.AddSymptomDialog
import com.example.cycletracker.ui.theme.CycleTheme
import com.example.cycletracker.audio.CycleSoundManager
import com.example.cycletracker.audio.GlobalSoundManager
import kotlinx.coroutines.launch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope

class MainActivity : ComponentActivity() {
	private val viewModel: CycleViewModel by viewModels { ServiceLocator.provideViewModelFactory(this) }
	private lateinit var soundManager: CycleSoundManager

	private val createJson = registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
		uri ?: return@registerForActivityResult
		lifecycleScope.launch {
			val cycles = viewModel.cycles.value
			val symptoms = viewModel.recentSymptoms.value
			ExportImport.exportJson(contentResolver, uri, cycles, symptoms)
		}
	}
	private val openJson = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
		uri ?: return@registerForActivityResult
		lifecycleScope.launch {
			val (cycles, symptoms) = ExportImport.importJson(contentResolver, uri)
			cycles.forEach { viewModel.addCycle(it.startDate, it.endDate) }
			symptoms.forEach { viewModel.addSymptom(it.date, it.type, it.intensity, it.note) }
		}
	}
	private val createCsv = registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
		uri ?: return@registerForActivityResult
		lifecycleScope.launch {
			val symptoms = viewModel.recentSymptoms.value
			ExportImport.exportCsv(contentResolver, uri, symptoms)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		
		// Инициализируем звуковой менеджер
		soundManager = CycleSoundManager(this)
		GlobalSoundManager.initialize(this)
		
		setContent {
			CycleTheme {
				val prefs = UserPreferences(this)
				val avgCycle by prefs.averageCycleDays.collectAsState(initial = 28)
				val avgLuteal by prefs.averageLutealDays.collectAsState(initial = 14)
				AppScaffold(
					avgCycleDays = avgCycle,
					avgLutealDays = avgLuteal,
					onSavePrefs = { c, l -> lifecycleScope.launch { prefs.setAverageCycleDays(c); prefs.setAverageLutealDays(l) } },
					onExportJson = { createJson.launch("cycletracker-backup.json") },
					onImportJson = { openJson.launch(arrayOf("application/json")) },
					onExportCsv = { createCsv.launch("symptoms.csv") },
					vm = viewModel,
					soundManager = soundManager
				)
			}
		}
	}
}

private enum class AppScreen(val route: String, val label: String) {
	Home("home", "Главная"),
	Calendar("calendar", "Календарь"),
	History("history", "История"),
	Statistics("statistics", "Статистика"),
	Settings("settings", "Настройки")
}

@Composable
private fun AppScaffold(
	avgCycleDays: Int,
	avgLutealDays: Int,
	onSavePrefs: (Int, Int) -> Unit,
	onExportJson: () -> Unit,
	onImportJson: () -> Unit,
	onExportCsv: () -> Unit,
	vm: CycleViewModel,
	soundManager: CycleSoundManager
) {
	val navController = rememberNavController()
	val cycles by vm.cycles.collectAsState()
	val history by vm.recentSymptoms.collectAsState()
	val selectedDate by vm.selectedDate.collectAsState()
	
	var showAddSymptomDialog by remember { mutableStateOf(false) }

	Scaffold(
		bottomBar = {
			NavigationBar {
				val navBackStackEntry by navController.currentBackStackEntryAsState()
				val currentDestination = navBackStackEntry?.destination
				AppScreen.entries.forEach { screen ->
					NavigationBarItem(
						selected = currentDestination.isRouteSelected(screen.route),
						onClick = {
							soundManager.playTabSwitch()
							navController.navigate(screen.route) {
								popUpTo(navController.graph.findStartDestination().id) { saveState = true }
								launchSingleTop = true
								restoreState = true
							}
						},
						label = { Text(screen.label) },
						icon = { 
							Icon(
								when (screen) {
									AppScreen.Home -> Icons.Filled.Home
									AppScreen.Calendar -> Icons.Filled.CalendarToday
									AppScreen.History -> Icons.Filled.History
									AppScreen.Statistics -> Icons.Filled.Analytics
									AppScreen.Settings -> Icons.Filled.Settings
								},
								contentDescription = screen.label
							)
						}
					)
				}
			}
		}
		) { innerPadding ->
		NavHost(
			navController = navController,
			startDestination = AppScreen.Home.route,
			modifier = Modifier.padding(innerPadding)
		) {
			composable(AppScreen.Home.route) {
				HomeScreen(
					cycles = cycles,
					symptoms = history,
					averageCycleDays = avgCycleDays,
					onAddSymptom = { showAddSymptomDialog = true },
					onViewCalendar = { navController.navigate(AppScreen.Calendar.route) },
					onViewHistory = { navController.navigate(AppScreen.History.route) }
				)
			}
			composable(AppScreen.Calendar.route) {
				CalendarScreen(
					cycles = cycles,
					averageCycleDays = avgCycleDays,
					onDateClick = { vm.selectDate(it) },
					onAddSymptom = { date ->
						vm.selectDate(date)
						showAddSymptomDialog = true
					}
				)
			}
			composable(AppScreen.History.route) {
				HistoryScreen(
					items = history,
					onFilterChange = { type, days -> vm.setHistoryFilter(type, days) }
				)
			}
			composable(AppScreen.Statistics.route) {
				StatisticsScreen(
					cycles = cycles,
					symptoms = history
				)
			}
			composable(AppScreen.Settings.route) {
				SettingsScreen(
					avgCycleDays = avgCycleDays,
					avgLutealDays = avgLutealDays,
					onSave = onSavePrefs,
					onExportJson = onExportJson,
					onImportJson = onImportJson,
					onExportCsv = onExportCsv
				)
			}
		}
	}
	
	// Диалог добавления симптома
	if (showAddSymptomDialog) {
		AddSymptomDialog(
			selectedDate = selectedDate,
			onDismiss = { 
				soundManager.playButtonClick()
				showAddSymptomDialog = false 
			},
			onSave = { type, intensity, note ->
				soundManager.playSymptomAdded()
				vm.addSymptom(selectedDate, type, intensity, note)
			}
		)
	}
}

private fun NavDestination?.isRouteSelected(route: String): Boolean =
	this?.hierarchy?.any { it.route == route } == true
