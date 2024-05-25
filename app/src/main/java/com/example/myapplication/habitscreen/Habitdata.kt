package com.example.myapplication.habitscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class HabitData(
    val id: Int,
    val name: String,
    var initialFrequency: Frequency,
    var initialStreak: Int,
    var lastUpdated: LocalDateTime? = null
) {
    var frequency: Frequency by mutableStateOf(initialFrequency)
    var streak: Int by mutableIntStateOf(initialStreak)

    fun incrementStreak(): Boolean {
        val now = LocalDateTime.now()
        val canIncrement = when (frequency) {
            Frequency.Daily -> lastUpdated == null || ChronoUnit.DAYS.between(lastUpdated, now) >= 1
            Frequency.SecondDay -> lastUpdated == null || ChronoUnit.DAYS.between(lastUpdated, now) >= 2
            Frequency.Weekly -> lastUpdated == null || ChronoUnit.WEEKS.between(lastUpdated, now) >= 1
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
        lastUpdated = null
    }
}

sealed class Frequency {
    object Daily : Frequency()
    object SecondDay : Frequency()
    object Weekly : Frequency()
}

