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


    fun addTask() {
        if (textInput.isNotBlank()) {
            // Use formatted date string for task date
            val task = Task(name = textInput, date = selectedDateString, time = selectedTime, icon = "default_icon")
            tasks.add(task)
            // Sorting tasks should now consider the LocalDate parsed from date string if necessary
            tasks.sortWith(Comparator.comparing<Task, LocalDate> { LocalDate.parse(it.date, DateTimeFormatter.ofPattern("EEEE, d'th' MMMM")) }
                .thenComparing(Comparator.comparing<Task, LocalTime> { safeParseTime(it.time) }))
            // Log the addition
            Log.d("HomeViewModel", "Task added: $task, Total tasks now: ${tasks.size}")
            // Reset inputs and close dialog
            textInput = ""  // Clear the input
            selectedTime = availableTimes.first()  // Reset time
            showDialog = false  // Close dialog
        } else {
            Log.d("HomeViewModel", "No task input to add")
        }
    }

    private fun safeParseTime(time: String): LocalTime {
        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.MIN  // Default to the minimum time if parsing fails
        }
    }

    fun showAddTaskDialog() {
        showDialog = true
    }

    fun hideAddTaskDialog() {
        showDialog = false
    }
}
