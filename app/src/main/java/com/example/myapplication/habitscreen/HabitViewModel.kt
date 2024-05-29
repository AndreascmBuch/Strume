package com.example.myapplication.habitscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class HabitViewModel(private val dao: HabitDao) : ViewModel() {

    // MutableStateFlow til at holde applikationens nuværende tilstand
    private val _state = MutableStateFlow(HabitState())
    val state = _state.asStateFlow()

    init {
        // Initialisering: Henter vaner fra databasen og opdaterer tilstanden
        viewModelScope.launch {
            dao.getHabitsOrderedByName().collect { habits ->
                _state.update { it.copy(habits = habits) }
            }
        }
    }

    // Funktion til at håndtere forskellige hændelser relateret til vaner
    fun onEventForHabit(event: HabitEvent) {
        when (event) {
            is HabitEvent.SaveHabit -> {
                // Coroutine til at gemme eller opdatere en vane i databasen
                viewModelScope.launch {
                    dao.upsertHabit(event.habit)
                }
                // Opdater tilstanden for at lukke dialogen og nulstille felter
                _state.update { it.copy(isAddingHabit = false, name = "", frequency = Habit.Frequency.Daily, editingHabitId = null) }
            }
            is HabitEvent.SetName -> {
                // Opdater tilstanden med det indtastede navn
                _state.update { it.copy(name = event.name) }
            }
            is HabitEvent.SetFrequency -> {
                // Opdater tilstanden med den valgte frekvens
                _state.update { it.copy(frequency = event.frequency) }
            }
            is HabitEvent.DeleteHabit -> {
                // Coroutine til at slette en vane fra databasen
                viewModelScope.launch { dao.deleteHabit(event.habit) }
                // Opdater tilstanden for at lukke dialogen og nulstille felter
                _state.update { it.copy(isAddingHabit = false, name = "", frequency = Habit.Frequency.Daily, editingHabitId = null) }
            }
            is HabitEvent.ShowDialog -> {
                // Opdater tilstanden for at vise dialogen og nulstille redigeringstilstanden
                _state.update { it.copy(isAddingHabit = true, editingHabitId = null) }
            }
            is HabitEvent.HideDialog -> {
                // Opdater tilstanden for at skjule dialogen
                _state.update { it.copy(isAddingHabit = false) }
            }
            is HabitEvent.IncrementStreak -> {
                // Coroutine til at øge streak for en vane og opdatere sidste opdateringstidspunkt
                viewModelScope.launch {
                    val habit = event.habit.copy(
                        streak = event.habit.streak + 1,
                        lastUpdated = LocalDateTime.now()
                    )
                    dao.upsertHabit(habit)
                }
            }
            is HabitEvent.ResetStreak -> {
                // Coroutine til at nulstille streak for en vane
                viewModelScope.launch {
                    val habit = event.habit.copy(streak = 0)
                    dao.upsertHabit(habit)
                }
            }
        }
    }
}



