package com.example.myapplication.habitscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class HabitViewModel(private val dao: HabitDao) : ViewModel() {

    private val _state = MutableStateFlow(HabitState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getHabitsOrderedByName().collect { habits ->
                _state.update { it.copy(habits = habits) }
            }
        }
    }
    fun onEventForHabit(event: HabitEvent) {
        when (event) {
            is HabitEvent.SaveHabit -> {
                viewModelScope.launch {
                    dao.upsertHabit(event.habit)
                }
                _state.update { it.copy(isAddingHabit = false, name = "", frequency = Habit.Frequency.Daily, editingHabitId = null) }
            }
            is HabitEvent.SetName -> {
                _state.update { it.copy(name = event.name) }
            }
            is HabitEvent.SetFrequency -> {
                _state.update { it.copy(frequency = event.frequency) }
            }
            is HabitEvent.DeleteHabit -> {
                viewModelScope.launch { dao.deleteHabit(event.habit) }
                _state.update { it.copy(isAddingHabit = false, name = "", frequency = Habit.Frequency.Daily, editingHabitId = null) }
            }
            is HabitEvent.ShowDialog -> {
                _state.update { it.copy(isAddingHabit = true, editingHabitId = null) }
            }
            is HabitEvent.HideDialog -> {
                _state.update { it.copy(isAddingHabit = false) }
            }
            is HabitEvent.IncrementStreak -> {
                viewModelScope.launch {
                    val habit = event.habit.copy(
                        streak = event.habit.streak + 1,
                        lastUpdated = LocalDateTime.now()
                    )
                    dao.upsertHabit(habit)
                }
            }
            is HabitEvent.ResetStreak -> {
                viewModelScope.launch {
                    val habit = event.habit.copy(streak = 0)
                    dao.upsertHabit(habit)
                }
            }
        }
    }
}

