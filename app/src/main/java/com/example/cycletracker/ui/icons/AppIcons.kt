<<<<<<< HEAD
package com.example.cycletracker.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object AppIcons {
    // Navigation icons
    val Home = Icons.Filled.Home
    val Calendar = Icons.Filled.CalendarToday
    val History = Icons.Filled.History
    val Statistics = Icons.Filled.Analytics
    val Settings = Icons.Filled.Settings
    
    // Cycle related icons
    val Cycle = Icons.Filled.Repeat
    val Period = Icons.Filled.Favorite
    val Prediction = Icons.Filled.Schedule
    val Ovulation = Icons.Filled.Star
    
    // Symptom icons
    val Pain = Icons.Filled.LocalHospital
    val Mood = Icons.Filled.Mood
    val Sleep = Icons.Filled.Bedtime
    val Weight = Icons.Filled.MonitorWeight
    val Medication = Icons.Filled.Medication
    
    // Action icons
    val Add = Icons.Filled.Add
    val Edit = Icons.Filled.Edit
    val Delete = Icons.Filled.Delete
    val Save = Icons.Filled.Save
    val Share = Icons.Filled.Share
    val Download = Icons.Filled.Download
    val Upload = Icons.Filled.Upload
    
    // Status icons
    val Check = Icons.Filled.Check
    val Warning = Icons.Filled.Warning
    val Info = Icons.Filled.Info
    val Error = Icons.Filled.Error
    val Success = Icons.Filled.CheckCircle
    
    // UI icons
    val ArrowBack = Icons.Filled.ArrowBack
    val ArrowForward = Icons.Filled.ArrowForward
    val ExpandMore = Icons.Filled.ExpandMore
    val ExpandLess = Icons.Filled.ExpandLess
    val Close = Icons.Filled.Close
    val Menu = Icons.Filled.Menu
    val MoreVert = Icons.Filled.MoreVert
    val MoreHoriz = Icons.Filled.MoreHoriz
}

// Оригинальные иконки CycleTracker
object CycleIcons {
    // Фазы цикла
    val Follicular = R.drawable.ic_cycle_follicular
    val Ovulation = R.drawable.ic_cycle_ovulation
    val Luteal = R.drawable.ic_cycle_luteal
    val Period = R.drawable.ic_cycle_period
    
    // Симптомы
    val Pain = R.drawable.ic_symptom_pain
    val Headache = R.drawable.ic_symptom_headache
    val Fatigue = R.drawable.ic_symptom_fatigue
    val Mood = R.drawable.ic_symptom_mood
    val Bloating = R.drawable.ic_symptom_bloating
    
    // Настроение
    val Mood1 = R.drawable.ic_mood_1
    val Mood5 = R.drawable.ic_mood_5
    val Mood10 = R.drawable.ic_mood_10
    
    // UI элементы
    val Logo = R.drawable.ic_cycle_tracker_logo
    val Chart = R.drawable.ic_statistics_chart
    val Heart = R.drawable.ic_health_heart
    
    // Статусы
    val Success = R.drawable.ic_status_success
    val Warning = R.drawable.ic_status_warning
}

// Функции для получения иконок по типу
fun getCyclePhaseIcon(phase: String): Int {
    return when (phase.lowercase()) {
        "фолликулярная" -> CycleIcons.Follicular
        "овуляция" -> CycleIcons.Ovulation
        "лютеиновая" -> CycleIcons.Luteal
        "месячные", "период" -> CycleIcons.Period
        else -> CycleIcons.Follicular
    }
}

fun getSymptomIcon(symptomType: String): Int {
    return when (symptomType.lowercase()) {
        "боль в животе", "спазмы" -> CycleIcons.Pain
        "головная боль" -> CycleIcons.Headache
        "усталость" -> CycleIcons.Fatigue
        "перепады настроения" -> CycleIcons.Mood
        "вздутие" -> CycleIcons.Bloating
        else -> CycleIcons.Pain
    }
}

fun getMoodIcon(moodLevel: Int): Int {
    return when (moodLevel) {
        in 1..3 -> CycleIcons.Mood1
        in 4..6 -> CycleIcons.Mood5
        in 7..10 -> CycleIcons.Mood10
        else -> CycleIcons.Mood5
    }
}

object AppEmojis {
    // Cycle phases
    const val PERIOD = "🩸"
    const val PREDICTED = "📅"
    const val FOLLICULAR = "🌸"
    const val LUTEAL = "🌙"
    const val OVULATION = "✨"
    
    // Symptoms
    const val PAIN = "😣"
    const val HEADACHE = "🤕"
    const val FATIGUE = "😴"
    const val MOOD_SWINGS = "😤"
    const val BLOATING = "💨"
    const val NAUSEA = "🤢"
    const val BREAST_TENDERNESS = "💕"
    const val CRAMPS = "⚡"
    const val OTHER = "📝"
    
    // Mood levels
    const val MOOD_1 = "😢" // Very sad
    const val MOOD_2 = "😔" // Sad
    const val MOOD_3 = "😕" // Slightly sad
    const val MOOD_4 = "😐" // Neutral
    const val MOOD_5 = "🙂" // Slightly happy
    const val MOOD_6 = "😊" // Happy
    const val MOOD_7 = "😄" // Very happy
    const val MOOD_8 = "🤩" // Excited
    const val MOOD_9 = "🥳" // Ecstatic
    const val MOOD_10 = "🎉" // Euphoric
    
    // Sleep quality
    const val SLEEP_1 = "😵" // Terrible
    const val SLEEP_2 = "😴" // Poor
    const val SLEEP_3 = "😑" // Fair
    const val SLEEP_4 = "😌" // Good
    const val SLEEP_5 = "😊" // Excellent
    
    // Statistics
    const val CHART = "📊"
    const val TREND_UP = "📈"
    const val TREND_DOWN = "📉"
    const val TREND_STABLE = "➡️"
    const val CALENDAR = "📅"
    const val CLOCK = "⏰"
    const val TARGET = "🎯"
    const val TROPHY = "🏆"
    
    // Actions
    const val ADD = "➕"
    const val EDIT = "✏️"
    const val DELETE = "🗑️"
    const val SAVE = "💾"
    const val SHARE = "📤"
    const val DOWNLOAD = "⬇️"
    const val UPLOAD = "⬆️"
    const val REFRESH = "🔄"
    const val SEARCH = "🔍"
    const val FILTER = "🔽"
    
    // Status
    const val SUCCESS = "✅"
    const val WARNING = "⚠️"
    const val ERROR = "❌"
    const val INFO = "ℹ️"
    const val QUESTION = "❓"
    const val LIGHTBULB = "💡"
    
    // Weather/Environment
    const val SUNNY = "☀️"
    const val CLOUDY = "☁️"
    const val RAINY = "🌧️"
    const val SNOWY = "❄️"
    const val STORMY = "⛈️"
    
    // Health
    const val HEART = "❤️"
    const val LUNGS = "🫁"
    const val BRAIN = "🧠"
    const val MUSCLE = "💪"
    const val BONE = "🦴"
    const val PILL = "💊"
    const val SYRINGE = "💉"
    const val THERMOMETER = "🌡️"
    
    // Time
    const val MORNING = "🌅"
    const val AFTERNOON = "☀️"
    const val EVENING = "🌆"
    const val NIGHT = "🌙"
    const val DAWN = "🌄"
    const val DUSK = "🌇"
    
    fun getMoodEmoji(level: Int): String {
        return when (level) {
            1 -> MOOD_1
            2 -> MOOD_2
            3 -> MOOD_3
            4 -> MOOD_4
            5 -> MOOD_5
            6 -> MOOD_6
            7 -> MOOD_7
            8 -> MOOD_8
            9 -> MOOD_9
            10 -> MOOD_10
            else -> MOOD_5
        }
    }
    
    fun getSleepEmoji(quality: Int): String {
        return when (quality) {
            1 -> SLEEP_1
            2 -> SLEEP_2
            3 -> SLEEP_3
            4 -> SLEEP_4
            5 -> SLEEP_5
            else -> SLEEP_3
        }
    }
    
    fun getSymptomEmoji(type: String): String {
        return when (type.lowercase()) {
            "боль в животе" -> PAIN
            "головная боль" -> HEADACHE
            "усталость" -> FATIGUE
            "перепады настроения" -> MOOD_SWINGS
            "вздутие" -> BLOATING
            "тошнота" -> NAUSEA
            "болезненность груди" -> BREAST_TENDERNESS
            "спазмы" -> CRAMPS
            else -> OTHER
        }
    }
}
=======
package com.example.cycletracker.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object AppIcons {
    // Navigation icons
    val Home = Icons.Filled.Home
    val Calendar = Icons.Filled.CalendarToday
    val History = Icons.Filled.History
    val Statistics = Icons.Filled.Analytics
    val Settings = Icons.Filled.Settings
    
    // Cycle related icons
    val Cycle = Icons.Filled.Repeat
    val Period = Icons.Filled.Favorite
    val Prediction = Icons.Filled.Schedule
    val Ovulation = Icons.Filled.Star
    
    // Symptom icons
    val Pain = Icons.Filled.LocalHospital
    val Mood = Icons.Filled.Mood
    val Sleep = Icons.Filled.Bedtime
    val Weight = Icons.Filled.MonitorWeight
    val Medication = Icons.Filled.Medication
    
    // Action icons
    val Add = Icons.Filled.Add
    val Edit = Icons.Filled.Edit
    val Delete = Icons.Filled.Delete
    val Save = Icons.Filled.Save
    val Share = Icons.Filled.Share
    val Download = Icons.Filled.Download
    val Upload = Icons.Filled.Upload
    
    // Status icons
    val Check = Icons.Filled.Check
    val Warning = Icons.Filled.Warning
    val Info = Icons.Filled.Info
    val Error = Icons.Filled.Error
    val Success = Icons.Filled.CheckCircle
    
    // UI icons
    val ArrowBack = Icons.Filled.ArrowBack
    val ArrowForward = Icons.Filled.ArrowForward
    val ExpandMore = Icons.Filled.ExpandMore
    val ExpandLess = Icons.Filled.ExpandLess
    val Close = Icons.Filled.Close
    val Menu = Icons.Filled.Menu
    val MoreVert = Icons.Filled.MoreVert
    val MoreHoriz = Icons.Filled.MoreHoriz
}

// Оригинальные иконки CycleTracker
object CycleIcons {
    // Фазы цикла
    val Follicular = R.drawable.ic_cycle_follicular
    val Ovulation = R.drawable.ic_cycle_ovulation
    val Luteal = R.drawable.ic_cycle_luteal
    val Period = R.drawable.ic_cycle_period
    
    // Симптомы
    val Pain = R.drawable.ic_symptom_pain
    val Headache = R.drawable.ic_symptom_headache
    val Fatigue = R.drawable.ic_symptom_fatigue
    val Mood = R.drawable.ic_symptom_mood
    val Bloating = R.drawable.ic_symptom_bloating
    
    // Настроение
    val Mood1 = R.drawable.ic_mood_1
    val Mood5 = R.drawable.ic_mood_5
    val Mood10 = R.drawable.ic_mood_10
    
    // UI элементы
    val Logo = R.drawable.ic_cycle_tracker_logo
    val Chart = R.drawable.ic_statistics_chart
    val Heart = R.drawable.ic_health_heart
    
    // Статусы
    val Success = R.drawable.ic_status_success
    val Warning = R.drawable.ic_status_warning
}

// Функции для получения иконок по типу
fun getCyclePhaseIcon(phase: String): Int {
    return when (phase.lowercase()) {
        "фолликулярная" -> CycleIcons.Follicular
        "овуляция" -> CycleIcons.Ovulation
        "лютеиновая" -> CycleIcons.Luteal
        "месячные", "период" -> CycleIcons.Period
        else -> CycleIcons.Follicular
    }
}

fun getSymptomIcon(symptomType: String): Int {
    return when (symptomType.lowercase()) {
        "боль в животе", "спазмы" -> CycleIcons.Pain
        "головная боль" -> CycleIcons.Headache
        "усталость" -> CycleIcons.Fatigue
        "перепады настроения" -> CycleIcons.Mood
        "вздутие" -> CycleIcons.Bloating
        else -> CycleIcons.Pain
    }
}

fun getMoodIcon(moodLevel: Int): Int {
    return when (moodLevel) {
        in 1..3 -> CycleIcons.Mood1
        in 4..6 -> CycleIcons.Mood5
        in 7..10 -> CycleIcons.Mood10
        else -> CycleIcons.Mood5
    }
}

object AppEmojis {
    // Cycle phases
    const val PERIOD = "🩸"
    const val PREDICTED = "📅"
    const val FOLLICULAR = "🌸"
    const val LUTEAL = "🌙"
    const val OVULATION = "✨"
    
    // Symptoms
    const val PAIN = "😣"
    const val HEADACHE = "🤕"
    const val FATIGUE = "😴"
    const val MOOD_SWINGS = "😤"
    const val BLOATING = "💨"
    const val NAUSEA = "🤢"
    const val BREAST_TENDERNESS = "💕"
    const val CRAMPS = "⚡"
    const val OTHER = "📝"
    
    // Mood levels
    const val MOOD_1 = "😢" // Very sad
    const val MOOD_2 = "😔" // Sad
    const val MOOD_3 = "😕" // Slightly sad
    const val MOOD_4 = "😐" // Neutral
    const val MOOD_5 = "🙂" // Slightly happy
    const val MOOD_6 = "😊" // Happy
    const val MOOD_7 = "😄" // Very happy
    const val MOOD_8 = "🤩" // Excited
    const val MOOD_9 = "🥳" // Ecstatic
    const val MOOD_10 = "🎉" // Euphoric
    
    // Sleep quality
    const val SLEEP_1 = "😵" // Terrible
    const val SLEEP_2 = "😴" // Poor
    const val SLEEP_3 = "😑" // Fair
    const val SLEEP_4 = "😌" // Good
    const val SLEEP_5 = "😊" // Excellent
    
    // Statistics
    const val CHART = "📊"
    const val TREND_UP = "📈"
    const val TREND_DOWN = "📉"
    const val TREND_STABLE = "➡️"
    const val CALENDAR = "📅"
    const val CLOCK = "⏰"
    const val TARGET = "🎯"
    const val TROPHY = "🏆"
    
    // Actions
    const val ADD = "➕"
    const val EDIT = "✏️"
    const val DELETE = "🗑️"
    const val SAVE = "💾"
    const val SHARE = "📤"
    const val DOWNLOAD = "⬇️"
    const val UPLOAD = "⬆️"
    const val REFRESH = "🔄"
    const val SEARCH = "🔍"
    const val FILTER = "🔽"
    
    // Status
    const val SUCCESS = "✅"
    const val WARNING = "⚠️"
    const val ERROR = "❌"
    const val INFO = "ℹ️"
    const val QUESTION = "❓"
    const val LIGHTBULB = "💡"
    
    // Weather/Environment
    const val SUNNY = "☀️"
    const val CLOUDY = "☁️"
    const val RAINY = "🌧️"
    const val SNOWY = "❄️"
    const val STORMY = "⛈️"
    
    // Health
    const val HEART = "❤️"
    const val LUNGS = "🫁"
    const val BRAIN = "🧠"
    const val MUSCLE = "💪"
    const val BONE = "🦴"
    const val PILL = "💊"
    const val SYRINGE = "💉"
    const val THERMOMETER = "🌡️"
    
    // Time
    const val MORNING = "🌅"
    const val AFTERNOON = "☀️"
    const val EVENING = "🌆"
    const val NIGHT = "🌙"
    const val DAWN = "🌄"
    const val DUSK = "🌇"
    
    fun getMoodEmoji(level: Int): String {
        return when (level) {
            1 -> MOOD_1
            2 -> MOOD_2
            3 -> MOOD_3
            4 -> MOOD_4
            5 -> MOOD_5
            6 -> MOOD_6
            7 -> MOOD_7
            8 -> MOOD_8
            9 -> MOOD_9
            10 -> MOOD_10
            else -> MOOD_5
        }
    }
    
    fun getSleepEmoji(quality: Int): String {
        return when (quality) {
            1 -> SLEEP_1
            2 -> SLEEP_2
            3 -> SLEEP_3
            4 -> SLEEP_4
            5 -> SLEEP_5
            else -> SLEEP_3
        }
    }
    
    fun getSymptomEmoji(type: String): String {
        return when (type.lowercase()) {
            "боль в животе" -> PAIN
            "головная боль" -> HEADACHE
            "усталость" -> FATIGUE
            "перепады настроения" -> MOOD_SWINGS
            "вздутие" -> BLOATING
            "тошнота" -> NAUSEA
            "болезненность груди" -> BREAST_TENDERNESS
            "спазмы" -> CRAMPS
            else -> OTHER
        }
    }
}
>>>>>>> 7eb7955987a0da95e1b119ccbbfc2bcdc4522c76
