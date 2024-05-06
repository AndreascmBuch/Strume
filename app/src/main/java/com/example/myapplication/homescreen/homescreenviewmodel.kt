package com.example.myapplication.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {
    val tasks = mutableStateListOf<Task>()
    var showDialog by mutableStateOf(false)
    var textInput by mutableStateOf("")
    var selectedTime by mutableStateOf("")
    val availableTimes = List(24 * 12) { i ->
        String.format("%02d:%02d", i / 12, (i % 12) * 5)
    }

    fun addTask(task: Task) {
        tasks.add(task)
    }

    fun addTask() {
        if (textInput.isNotBlank()) {
            val task = com.example.myapplication.homescreen.Task(
                textInput,
                "2024-05-07",
                selectedTime,
                "default_icon"
            ) // Change "2024-05-07" to the actual date value
            tasks.add(task)
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
