package com.example.myapplication.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class HomeViewModel : ViewModel() {
    var tasks = mutableStateListOf<Task>()
    var showDialog by mutableStateOf(false)
    var textInput by mutableStateOf("")
    var selectedTime by mutableStateOf("")
    val availableTimes = List(24 * 12) { i ->
        String.format("%02d:%02d", i / 12, (i % 12) * 5)
    }
    var selectedDate by mutableStateOf(LocalDate.now())
    var selectedDateString by mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d'th' MMMM")))
    var showDatePickerDialog by mutableStateOf(false)
    var showTimePickerDialog by mutableStateOf(false)
    private var nextTaskId = 0  // To assign unique IDs
    var editingTaskId by mutableStateOf<Int?>(null)

    fun addOrUpdateTask() {
        val date = selectedDateString
        val time = selectedTime
        editingTaskId?.let {taskId ->
            // Find and update existing task
            tasks.find { task -> task.id == taskId }?.apply {
                this.observableName = textInput  // Update observable property
                this.observableDate = date      // Update observable property
                this.observableTime = time
            }
        } ?: run {
            // Add new task
            val task = Task(nextTaskId++, textInput, date, time, "default_icon")
            tasks.add(task)
        }
        showDialog = false
        resetInputs()
    }

    fun editTask(taskId: Int) {
        editingTaskId = taskId
        tasks.find { it.id == taskId }?.let {
            textInput = it.name
            selectedDateString = it.date
            selectedTime = it.time
        }
        showDialog = true
    }

    fun deleteTask(taskId: Int) {
        tasks.removeIf { it.id == taskId }
        hideAddTaskDialog()  // Close the dialog after deleting the task
    }

    private fun resetInputs() {
        textInput = ""
        selectedTime = availableTimes.first()
        editingTaskId = null
    }

    private fun safeParseTime(time: String): LocalTime {
        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.MIN  // Default to the minimum time if parsing fails
        }
    }

    fun showAddTaskDialog() {
        editingTaskId = null  // Reset the editing ID to ensure we are adding a new task
        showDialog = true
    }

    fun hideAddTaskDialog() {
        showDialog = false
        resetInputs()  // Reset inputs on dialog close
    }
}