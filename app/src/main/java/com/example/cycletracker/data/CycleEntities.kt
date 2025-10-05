package com.example.cycletracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cycles")
data class Cycle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startDate: LocalDate,
    val endDate: LocalDate? = null,
    val cycleLength: Int? = null
)

@Entity(tableName = "period_days")
data class PeriodDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val flow: String = "medium" // light, medium, heavy
)

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: LocalDate,
    val score: Int, // 1..10
    val tagsCsv: String = "", // comma-separated tags
    val note: String = "",
    val energy: Int? = null, // 1..10
    val sleepHours: Float? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)

@Entity(tableName = "mood_settings")
data class MoodSettings(
    @PrimaryKey
    val id: Long = 1,
    val reminderEnabled: Boolean = false,
    val reminderHour: Int = 20, // 20:00 по умолчанию
    val enabledTagsCsv: String = "радость,усталость,стресс,спокойствие,раздражительность"
)

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val dosage: String = "",
    val timesCsv: String = "", // e.g. "09:00,14:00,21:00"
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val notes: String = ""
)

@Entity(tableName = "medication_intakes")
data class MedicationIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val medicationId: Long,
    val date: LocalDate,
    val time: String, // "HH:mm"
    val taken: Boolean = false,
    val takenAtEpochMs: Long? = null
)