package com.example.cycletracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cycletracker.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CycleViewModel(private val repository: CycleRepository) : ViewModel() {
    
    val cycles: StateFlow<List<Cycle>> = repository.allCycles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun startNewCycle() {
        viewModelScope.launch {
            repository.startNewCycle(LocalDate.now())
        }
    }
    
    fun togglePeriodDay(date: LocalDate) {
        viewModelScope.launch {
            repository.togglePeriodDay(date)
        }
    }

    // Mood diary
    fun getMoodEntries(from: LocalDate, to: LocalDate) = repository.getMoodEntries(from, to)

    fun saveMoodEntry(
        date: LocalDate,
        score: Int,
        tagsCsv: String,
        note: String,
        energy: Int?,
        sleepHours: Float?
    ) {
        viewModelScope.launch {
            repository.upsertMoodEntry(
                MoodEntry(
                    date = date,
                    score = score,
                    tagsCsv = tagsCsv,
                    note = note,
                    energy = energy,
                    sleepHours = sleepHours
                )
            )
        }
    }

    suspend fun getMoodSettings(): MoodSettings = repository.getMoodSettingsOrDefault()

    fun saveMoodSettings(settings: MoodSettings) {
        viewModelScope.launch { repository.saveMoodSettings(settings) }
    }

    // Medications
    fun medications() = repository.getMedications()

    fun saveMedication(med: Medication) {
        viewModelScope.launch { repository.upsertMedication(med) }
    }

    fun deleteMedication(id: Long) {
        viewModelScope.launch { repository.deleteMedication(id) }
    }

    fun medicationIntakesByDate(date: LocalDate) = repository.getMedicationIntakesByDate(date)

    fun markMedicationIntake(id: Long, taken: Boolean) {
        viewModelScope.launch {
            repository.markMedicationIntake(
                id = id,
                taken = taken,
                takenAt = if (taken) System.currentTimeMillis() else null
            )
        }
    }
}