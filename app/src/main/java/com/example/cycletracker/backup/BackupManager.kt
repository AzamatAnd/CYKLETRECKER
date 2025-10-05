package com.example.cycletracker.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.example.cycletracker.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BackupManager(private val context: Context) {
    
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
    
    suspend fun createBackup(): BackupData = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(context)
        val cycles = database.cycleDao().getAllCyclesSnapshot()
        val periodDays = cycles.flatMap { cycle ->
            val days = mutableListOf<PeriodDay>()
            var date = cycle.startDate
            val endDate = cycle.endDate ?: cycle.startDate.plusDays(5)
            while (!date.isAfter(endDate)) {
                val periodDay = database.cycleDao().getPeriodDay(date)
                if (periodDay != null) {
                    days.add(periodDay)
                }
                date = date.plusDays(1)
            }
            days
        }
        val moodEntries = database.cycleDao().getMoodEntriesRange(
            LocalDate.now().minusYears(1),
            LocalDate.now()
        ).let { flow ->
            // Convert Flow to List for backup
            runBlocking { flow.first() }
        }
        val moodSettings = database.cycleDao().getMoodSettings()
        val medications = runBlocking { database.cycleDao().getMedications().first() }
        val notes = runBlocking { database.cycleDao().getAllNotes().first() }
        
        BackupData(
            cycles = cycles,
            periodDays = periodDays,
            moodEntries = moodEntries,
            moodSettings = moodSettings,
            medications = medications,
            notes = notes,
            backupDate = System.currentTimeMillis(),
            appVersion = "1.0.0"
        )
    }
    
    suspend fun exportToFile(backupData: BackupData): Uri = withContext(Dispatchers.IO) {
        val fileName = "cycle_tracker_backup_${dateFormat.format(Date())}.json"
        val file = File(context.getExternalFilesDir(null), fileName)
        
        val json = JSONObject().apply {
            put("appVersion", backupData.appVersion)
            put("backupDate", backupData.backupDate)
            put("cycles", backupData.cycles.map { cycle ->
                JSONObject().apply {
                    put("id", cycle.id)
                    put("startDate", cycle.startDate.toString())
                    put("endDate", cycle.endDate?.toString())
                    put("cycleLength", cycle.cycleLength)
                    put("notes", cycle.notes)
                }
            })
            put("periodDays", backupData.periodDays.map { day ->
                JSONObject().apply {
                    put("date", day.date.toString())
                    put("flow", day.flow)
                    put("symptoms", day.symptoms)
                }
            })
            put("moodEntries", backupData.moodEntries.map { entry ->
                JSONObject().apply {
                    put("id", entry.id)
                    put("date", entry.date.toString())
                    put("score", entry.score)
                    put("energy", entry.energy)
                    put("sleepHours", entry.sleepHours)
                    put("notes", entry.notes)
                }
            })
            put("medications", backupData.medications.map { med ->
                JSONObject().apply {
                    put("id", med.id)
                    put("name", med.name)
                    put("dosage", med.dosage)
                    put("frequency", med.frequency)
                    put("startDate", med.startDate.toString())
                    put("endDate", med.endDate?.toString())
                    put("isActive", med.isActive)
                }
            })
            put("notes", backupData.notes.map { note ->
                JSONObject().apply {
                    put("id", note.id)
                    put("date", note.date.toString())
                    put("title", note.title)
                    put("content", note.content)
                    put("createdAt", note.createdAt)
                }
            })
        }
        
        FileOutputStream(file).use { fos ->
            fos.write(json.toString().toByteArray())
        }
        
        Uri.fromFile(file)
    }
    
    suspend fun importFromFile(uri: Uri): BackupData = withContext(Dispatchers.IO) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val jsonString = inputStream?.bufferedReader()?.use { it.readText() } ?: ""
        val json = JSONObject(jsonString)
        
        val cycles = json.getJSONArray("cycles").let { array ->
            (0 until array.length()).map { i ->
                val cycleJson = array.getJSONObject(i)
                Cycle(
                    id = cycleJson.getLong("id"),
                    startDate = LocalDate.parse(cycleJson.getString("startDate")),
                    endDate = cycleJson.optString("endDate")?.let { LocalDate.parse(it) },
                    cycleLength = cycleJson.optInt("cycleLength").takeIf { it > 0 },
                    notes = cycleJson.optString("notes")
                )
            }
        }
        
        val periodDays = json.getJSONArray("periodDays").let { array ->
            (0 until array.length()).map { i ->
                val dayJson = array.getJSONObject(i)
                PeriodDay(
                    date = LocalDate.parse(dayJson.getString("date")),
                    flow = dayJson.optInt("flow"),
                    symptoms = dayJson.optString("symptoms")
                )
            }
        }
        
        val moodEntries = json.getJSONArray("moodEntries").let { array ->
            (0 until array.length()).map { i ->
                val entryJson = array.getJSONObject(i)
                MoodEntry(
                    id = entryJson.getLong("id"),
                    date = LocalDate.parse(entryJson.getString("date")),
                    score = entryJson.getInt("score"),
                    energy = entryJson.optInt("energy").takeIf { it > 0 },
                    sleepHours = entryJson.optDouble("sleepHours").takeIf { it > 0 },
                    notes = entryJson.optString("notes")
                )
            }
        }
        
        val medications = json.getJSONArray("medications").let { array ->
            (0 until array.length()).map { i ->
                val medJson = array.getJSONObject(i)
                Medication(
                    id = medJson.getLong("id"),
                    name = medJson.getString("name"),
                    dosage = medJson.getString("dosage"),
                    frequency = medJson.getString("frequency"),
                    startDate = LocalDate.parse(medJson.getString("startDate")),
                    endDate = medJson.optString("endDate")?.let { LocalDate.parse(it) },
                    isActive = medJson.getBoolean("isActive")
                )
            }
        }
        
        val notes = json.getJSONArray("notes").let { array ->
            (0 until array.length()).map { i ->
                val noteJson = array.getJSONObject(i)
                Note(
                    id = noteJson.getLong("id"),
                    date = LocalDate.parse(noteJson.getString("date")),
                    title = noteJson.getString("title"),
                    content = noteJson.getString("content"),
                    createdAt = noteJson.getLong("createdAt")
                )
            }
        }
        
        BackupData(
            cycles = cycles,
            periodDays = periodDays,
            moodEntries = moodEntries,
            moodSettings = null, // Will be restored separately
            medications = medications,
            notes = notes,
            backupDate = json.getLong("backupDate"),
            appVersion = json.getString("appVersion")
        )
    }
    
    suspend fun restoreBackup(backupData: BackupData) = withContext(Dispatchers.IO) {
        val database = AppDatabase.getDatabase(context)
        
        // Clear existing data
        database.cycleDao().deleteAllCycles()
        database.cycleDao().deleteAllPeriodDays()
        database.cycleDao().deleteAllMoodEntries()
        database.cycleDao().deleteAllMedications()
        database.cycleDao().deleteAllNotes()
        
        // Restore data
        cycles.forEach { cycle ->
            database.cycleDao().insertCycle(cycle)
        }
        
        periodDays.forEach { day ->
            database.cycleDao().insertPeriodDay(day)
        }
        
        moodEntries.forEach { entry ->
            database.cycleDao().upsertMoodEntry(entry)
        }
        
        medications.forEach { med ->
            database.cycleDao().upsertMedication(med)
        }
        
        notes.forEach { note ->
            database.cycleDao().insertNote(note)
        }
    }
}

data class BackupData(
    val cycles: List<Cycle>,
    val periodDays: List<PeriodDay>,
    val moodEntries: List<MoodEntry>,
    val moodSettings: MoodSettings?,
    val medications: List<Medication>,
    val notes: List<Note>,
    val backupDate: Long,
    val appVersion: String
)
