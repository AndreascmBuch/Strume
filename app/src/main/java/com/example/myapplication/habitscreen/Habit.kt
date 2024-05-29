package com.example.myapplication.habitscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Entity(tableName = "habits")
data class Habit(
    val name: String,
    val frequency: Frequency,
    val streak: Int,
    val lastUpdated: LocalDateTime? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {
    enum class Frequency(val displayName: String) {
        Daily("Daily"),
        SecondDay("Every Second Day"),
        Weekly("Weekly");

        override fun toString(): String {
            return displayName
        }

    }
}
