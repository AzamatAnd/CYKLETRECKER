<<<<<<< HEAD
package com.example.cycletracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cycles")
data class CycleEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val startDate: LocalDate,
	val endDate: LocalDate?,
	val averageLengthDays: Int?,
	val averageLutealDays: Int?
)

@Entity(tableName = "symptoms")
data class SymptomEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val type: String,
	val intensity: Int?,
	val note: String?
)

@Entity(tableName = "mood_entries")
data class MoodEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val mood: Int, // 1-10 scale
	val note: String?
)

@Entity(tableName = "weight_entries")
data class WeightEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val weight: Float, // in kg
	val note: String?
)

@Entity(tableName = "sleep_entries")
data class SleepEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val sleepHours: Float,
	val quality: Int?, // 1-10 scale
	val note: String?
)

@Entity(tableName = "medication_entries")
data class MedicationEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val medication: String,
	val dosage: String?,
	val note: String?
)


=======
package com.example.cycletracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cycles")
data class CycleEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val startDate: LocalDate,
	val endDate: LocalDate?,
	val averageLengthDays: Int?,
	val averageLutealDays: Int?
)

@Entity(tableName = "symptoms")
data class SymptomEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val type: String,
	val intensity: Int?,
	val note: String?
)

@Entity(tableName = "mood_entries")
data class MoodEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val mood: Int, // 1-10 scale
	val note: String?
)

@Entity(tableName = "weight_entries")
data class WeightEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val weight: Float, // in kg
	val note: String?
)

@Entity(tableName = "sleep_entries")
data class SleepEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val sleepHours: Float,
	val quality: Int?, // 1-10 scale
	val note: String?
)

@Entity(tableName = "medication_entries")
data class MedicationEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val date: LocalDate,
	val medication: String,
	val dosage: String?,
	val note: String?
)


>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
