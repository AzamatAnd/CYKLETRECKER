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
}