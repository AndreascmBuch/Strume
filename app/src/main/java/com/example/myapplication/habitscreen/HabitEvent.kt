package com.example.myapplication.habitscreen


    sealed interface HabitEvent {
        data class SaveHabit(val habit: Habit) : HabitEvent
        data class SetName(val name: String) : HabitEvent
        data class SetFrequency(val frequency: Habit.Frequency) : HabitEvent
        data class DeleteHabit(val habit: Habit) : HabitEvent
        object ShowDialog : HabitEvent
        object HideDialog : HabitEvent
        data class IncrementStreak(val habit: Habit) : HabitEvent
        data class ResetStreak(val habit: Habit) : HabitEvent
    }

