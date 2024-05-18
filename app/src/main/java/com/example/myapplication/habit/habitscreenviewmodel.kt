package com.example.myapplication.habit

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class HabitsViewModel : ViewModel() {
    val habits = mutableStateListOf<Habit>()

    fun addHabit(name: String, frequency: Frequency, streak: Int) {
        val habit = Habit(
            id = habits.size + 1,
            name = name,
            initialFrequency = frequency,
            initialStreak = streak
        )
        habits.add(habit)
    }

    fun updateHabitFrequency(id: Int, frequency: Frequency) {
        val habit = habits.find { it.id == id }
        if (habit != null) {
            habit.frequency = frequency
            habit.resetStreak()
        }
    }
}