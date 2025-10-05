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