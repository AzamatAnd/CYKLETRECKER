package com.example.cycletracker.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class CycleRepository(private val cycleDao: CycleDao) {
    
    val allCycles: Flow<List<Cycle>> = cycleDao.getAllCycles()
    
    suspend fun getLastCycle(): Cycle? = cycleDao.getLastCycle()
    
    suspend fun startNewCycle(startDate: LocalDate): Long {
        return cycleDao.insertCycle(Cycle(startDate = startDate))
    }
    
    suspend fun endCycle(cycle: Cycle, endDate: LocalDate) {
        val cycleLength = java.time.Period.between(cycle.startDate, endDate).days
        cycleDao.updateCycle(cycle.copy(endDate = endDate, cycleLength = cycleLength))
    }
    
    suspend fun togglePeriodDay(date: LocalDate) {
        val existing = cycleDao.getPeriodDay(date)
        if (existing != null) {
            cycleDao.deletePeriodDay(date)
        } else {
            cycleDao.insertPeriodDay(PeriodDay(date = date))
        }
    }
}