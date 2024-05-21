package com.example.myapplication.habitscreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HabitsViewModel : ViewModel() {
    val habits = mutableStateListOf<Habit>()
    private val _showAddHabitDialog = mutableStateOf(false)
    val showAddHabitDialog = _showAddHabitDialog

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

    fun showAddHabitDialog() {
        _showAddHabitDialog.value = true
    }

    fun hideAddHabitDialog() {
        _showAddHabitDialog.value = false
    }
}