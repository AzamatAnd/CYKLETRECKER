package com.example.cycletracker.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CycleDao {
    @Query("SELECT * FROM cycles ORDER BY startDate DESC")
    fun getAllCycles(): Flow<List<Cycle>>
    
    @Query("SELECT * FROM cycles ORDER BY startDate DESC LIMIT 1")
    suspend fun getLastCycle(): Cycle?

    @Query("SELECT * FROM cycles ORDER BY startDate DESC")
    suspend fun getAllCyclesSnapshot(): List<Cycle>
    
    @Insert
    suspend fun insertCycle(cycle: Cycle): Long
    
    @Update
    suspend fun updateCycle(cycle: Cycle)
    
    @Query("SELECT * FROM period_days WHERE date = :date")
    suspend fun getPeriodDay(date: LocalDate): PeriodDay?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeriodDay(periodDay: PeriodDay)
    
    @Query("DELETE FROM period_days WHERE date = :date")
    suspend fun deletePeriodDay(date: LocalDate)

    // Mood diary
    @Query("SELECT * FROM mood_entries WHERE date = :date")
    suspend fun getMoodEntryByDate(date: LocalDate): MoodEntry?

    @Query("SELECT * FROM mood_entries WHERE date BETWEEN :from AND :to ORDER BY date DESC")
    fun getMoodEntriesRange(from: LocalDate, to: LocalDate): Flow<List<MoodEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMoodEntry(entry: MoodEntry): Long

    @Query("SELECT * FROM mood_settings WHERE id = 1")
    suspend fun getMoodSettings(): MoodSettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMoodSettings(settings: MoodSettings)

    // Medications
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getMedications(): Flow<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMedication(med: Medication): Long

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun deleteMedication(id: Long)

    @Query("SELECT * FROM medication_intakes WHERE date = :date ORDER BY time ASC")
    fun getMedicationIntakesByDate(date: LocalDate): Flow<List<MedicationIntake>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMedicationIntake(intake: MedicationIntake): Long

    @Query("UPDATE medication_intakes SET taken = :taken, takenAtEpochMs = :takenAt WHERE id = :id")
    suspend fun markMedicationIntake(id: Long, taken: Boolean, takenAt: Long?)
}