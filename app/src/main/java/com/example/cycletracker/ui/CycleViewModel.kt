package com.example.cycletracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cycletracker.data.Cycle
import com.example.cycletracker.data.CycleRepository
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
}