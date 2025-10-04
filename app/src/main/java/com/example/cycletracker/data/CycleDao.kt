package com.example.cycletracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CycleDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(cycle: CycleEntity): Long

	@Query("SELECT * FROM cycles ORDER BY startDate DESC")
	fun observeCycles(): Flow<List<CycleEntity>>

	@Query("SELECT * FROM cycles WHERE :date BETWEEN startDate AND IFNULL(endDate, startDate) LIMIT 1")
	suspend fun findCycleByDate(date: LocalDate): CycleEntity?

	@Delete
	suspend fun delete(cycle: CycleEntity)
}

@Dao
interface SymptomDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(symptom: SymptomEntity): Long

	@Query("SELECT * FROM symptoms WHERE date = :date ORDER BY id DESC")
	fun observeSymptomsByDate(date: LocalDate): Flow<List<SymptomEntity>>

	@Query("SELECT * FROM symptoms WHERE date BETWEEN :from AND :to ORDER BY date DESC")
	fun observeSymptomsRange(from: LocalDate, to: LocalDate): Flow<List<SymptomEntity>>

	@Delete
	suspend fun delete(symptom: SymptomEntity)

	@Update
	suspend fun update(symptom: SymptomEntity)
}

@Dao
interface MoodDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(mood: MoodEntity): Long

	@Query("SELECT * FROM mood_entries WHERE date = :date")
	fun observeMood(date: LocalDate): Flow<MoodEntity?>

	@Query("SELECT * FROM mood_entries WHERE date >= :from AND date <= :to ORDER BY date DESC")
	fun observeMoods(from: LocalDate, to: LocalDate): Flow<List<MoodEntity>>

	@Delete
	suspend fun delete(mood: MoodEntity)
}

@Dao
interface WeightDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(weight: WeightEntity): Long

	@Query("SELECT * FROM weight_entries WHERE date = :date")
	fun observeWeight(date: LocalDate): Flow<WeightEntity?>

	@Query("SELECT * FROM weight_entries WHERE date >= :from AND date <= :to ORDER BY date DESC")
	fun observeWeights(from: LocalDate, to: LocalDate): Flow<List<WeightEntity>>

	@Delete
	suspend fun delete(weight: WeightEntity)
}

@Dao
interface SleepDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(sleep: SleepEntity): Long

	@Query("SELECT * FROM sleep_entries WHERE date = :date")
	fun observeSleep(date: LocalDate): Flow<SleepEntity?>

	@Query("SELECT * FROM sleep_entries WHERE date >= :from AND date <= :to ORDER BY date DESC")
	fun observeSleeps(from: LocalDate, to: LocalDate): Flow<List<SleepEntity>>

	@Delete
	suspend fun delete(sleep: SleepEntity)
}

@Dao
interface MedicationDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(medication: MedicationEntity): Long

	@Query("SELECT * FROM medication_entries WHERE date = :date ORDER BY medication")
	fun observeMedications(date: LocalDate): Flow<List<MedicationEntity>>

	@Query("SELECT * FROM medication_entries WHERE date >= :from AND date <= :to ORDER BY date DESC")
	fun observeMedications(from: LocalDate, to: LocalDate): Flow<List<MedicationEntity>>

	@Delete
	suspend fun delete(medication: MedicationEntity)
}


