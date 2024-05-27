package com.example.myapplication.habitscreen

import com.example.myapplication.homescreen.SortType

data class HabitState(
    val habits: List<Habit> = emptyList(),
    val name: String = "",
    val frequency: Habit.Frequency = Habit.Frequency.Daily,
    val isAddingHabit: Boolean = false,
    val editingHabitId: Int? = null
)





