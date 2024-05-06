package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val habitsList = mutableStateListOf<Habits>()
    fun addHabit(habit:Habits) {
        habitsList.add(habit)
    }
}

// ViewModel til at styre opgaver og dialogtilstande

class HomeViewModel : ViewModel() {
    val tasks = mutableStateListOf<String>()
    var showDialog by mutableStateOf(false)
    var textInput by mutableStateOf("")
    var selectedTime by mutableStateOf("")
    val availableTimes = List(24 * 12) { i ->
        String.format("%02d:%02d", i / 12, (i % 12) * 5)
    }

    fun addTask() {
        if (textInput.isNotBlank()) {
            val taskDescription = "$textInput at $selectedTime"
            tasks.add(taskDescription)
            textInput = ""
            showDialog = false
        }
    }

    fun showAddTaskDialog() {
        showDialog = true
    }

    fun hideAddTaskDialog() {
        showDialog = false
    }
}