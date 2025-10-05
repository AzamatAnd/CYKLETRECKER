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

    // Mood diary
    fun getMoodEntries(from: LocalDate, to: LocalDate): Flow<List<MoodEntry>> =
        cycleDao.getMoodEntriesRange(from, to)

    suspend fun upsertMoodEntry(entry: MoodEntry): Long = cycleDao.upsertMoodEntry(entry)

    suspend fun getMoodSettingsOrDefault(): MoodSettings =
        cycleDao.getMoodSettings() ?: MoodSettings()

    suspend fun saveMoodSettings(settings: MoodSettings) = cycleDao.upsertMoodSettings(settings)

    // Medications
    fun getMedications(): Flow<List<Medication>> = cycleDao.getMedications()

    suspend fun upsertMedication(med: Medication): Long = cycleDao.upsertMedication(med)

    suspend fun deleteMedication(id: Long) = cycleDao.deleteMedication(id)

    fun getMedicationIntakesByDate(date: LocalDate): Flow<List<MedicationIntake>> =
        cycleDao.getMedicationIntakesByDate(date)

    suspend fun upsertMedicationIntake(intake: MedicationIntake): Long =
        cycleDao.upsertMedicationIntake(intake)

    suspend fun markMedicationIntake(id: Long, taken: Boolean, takenAt: Long?) =
        cycleDao.markMedicationIntake(id, taken, takenAt)
}