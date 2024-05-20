package com.example.myapplication.habit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class Habit(
    val id: Int,
    val name: String,
    var initialFrequency: Frequency,
    var initialStreak: Int,
    var lastUpdated: LocalDateTime = LocalDateTime.now()
) {
    var frequency: Frequency by mutableStateOf(initialFrequency)
    var streak: Int by mutableIntStateOf(initialStreak)

    fun incrementStreak(): Boolean {
        val now = LocalDateTime.now()
        val canIncrement = when (frequency) {
            Frequency.Daily -> ChronoUnit.DAYS.between(lastUpdated, now) >= 1
            Frequency.EverySecondDay -> ChronoUnit.DAYS.between(lastUpdated, now) >= 2
            Frequency.Weekly -> ChronoUnit.WEEKS.between(lastUpdated, now) >= 1
        }

        return if (canIncrement) {
            streak++
            lastUpdated = now
            true
        } else {
            false
        }
    }

    fun resetStreak() {
        streak = 0
    }
}


sealed class Frequency {
    data object Daily : Frequency()
    data object EverySecondDay : Frequency()
    data object Weekly : Frequency()
}
