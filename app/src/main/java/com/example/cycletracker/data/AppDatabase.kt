package com.example.cycletracker.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.time.LocalDate

@Database(
	entities = [
		CycleEntity::class, 
		SymptomEntity::class,
		MoodEntity::class,
		WeightEntity::class,
		SleepEntity::class,
		MedicationEntity::class
	],
	version = 2,
	exportSchema = true
)
@TypeConverters(LocalDateConverters::class)
abstract class AppDatabase : RoomDatabase() {
	abstract fun cycleDao(): CycleDao
	abstract fun symptomDao(): SymptomDao
	abstract fun moodDao(): MoodDao
	abstract fun weightDao(): WeightDao
	abstract fun sleepDao(): SleepDao
	abstract fun medicationDao(): MedicationDao
}

class LocalDateConverters {
	@TypeConverter
	fun fromEpochDay(value: Long?): LocalDate? = value?.let(LocalDate::ofEpochDay)

	@TypeConverter
	fun localDateToEpochDay(date: LocalDate?): Long? = date?.toEpochDay()
}

val MIGRATION_1_2 = object : Migration(1, 2) {
	override fun migrate(database: SupportSQLiteDatabase) {
		// Создаем новые таблицы для версии 2
		database.execSQL("""
			CREATE TABLE IF NOT EXISTS mood_entries (
				id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
				date INTEGER NOT NULL,
				mood INTEGER NOT NULL,
				note TEXT
			)
		""")
		
		database.execSQL("""
			CREATE TABLE IF NOT EXISTS weight_entries (
				id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
				date INTEGER NOT NULL,
				weight REAL NOT NULL,
				note TEXT
			)
		""")
		
		database.execSQL("""
			CREATE TABLE IF NOT EXISTS sleep_entries (
				id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
				date INTEGER NOT NULL,
				sleepHours REAL NOT NULL,
				quality INTEGER,
				note TEXT
			)
		""")
		
		database.execSQL("""
			CREATE TABLE IF NOT EXISTS medication_entries (
				id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
				date INTEGER NOT NULL,
				medication TEXT NOT NULL,
				dosage TEXT,
				note TEXT
			)
		""")
	}
}


