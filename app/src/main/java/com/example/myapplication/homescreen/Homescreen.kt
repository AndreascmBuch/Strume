package com.example.myapplication.homescreen

import android.app.TimePickerDialog
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

fun getDayOfMonthSuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

// Hovedskærmskomponenten
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(viewModel: HomeViewModel) {
    val context = LocalContext.current

    if (viewModel.showDialog) {
        val calendar = Calendar.getInstance()


        // Launch DatePickerDialog
        fun showDatePicker() {
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.selectedDate = localDate // Store the LocalDate
                    // Format and store the string representation
                    viewModel.selectedDateString = localDate.format(DateTimeFormatter.ofPattern("EEEE, d'" + getDayOfMonthSuffix(dayOfMonth) + "' MMMM"))
                    Log.d("DatePicker", "Selected Date String: ${viewModel.selectedDateString}") // Add this line
                    viewModel.showDatePickerDialog = false // Close the date picker dialog
                },
                viewModel.selectedDate.year,
                viewModel.selectedDate.monthValue - 1,
                viewModel.selectedDate.dayOfMonth
            )
            datePickerDialog.show()
        }

        // Launch TimePickerDialog
        fun showTimePicker() {
            TimePickerDialog(
                context,
                { _: TimePicker, hourOfDay: Int, minute: Int ->
                    viewModel.selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        AlertDialog(
            onDismissRequest = { viewModel.hideAddTaskDialog() },
            title = { Text("Add New Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = viewModel.textInput,
                        onValueChange = { viewModel.textInput = it },
                        label = { Text("Task Details") }
                    )
                    Button(onClick = { showDatePicker() }) {
                        Text("Select Date: ${viewModel.selectedDate}")
                    }
                    Button(onClick = { showTimePicker() }) {
                        Text("Select Time: ${viewModel.selectedTime}")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.addTask()
                    Log.d("Dialog", "Add button clicked")
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { viewModel.hideAddTaskDialog() }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    // This log will show the number of tasks in the ViewModel
    Log.d("HomeScreen", "Recomposing with ${homeViewModel.tasks.size} tasks")

    // Correct the ViewModel instance reference for logging
    Log.d("HomeScreen", "ViewModel instance in HomeScreen: $homeViewModel")

    val viewModel: HomeViewModel = viewModel()
    val tasks = homeViewModel.tasks

    LaunchedEffect(tasks) {
        Log.d("HomeScreen", "Tasks have changed. Size: ${tasks.size}")
        // Potentially other side effects
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Hello", color = Color.White)
        Text(text = "Here are your tasks for the day", color = Color.White)

        LazyColumn {
            items(tasks) { task ->
                Log.d("HomeScreen", "Displaying task: ${task.name}, ${task.date}, ${task.time}")
                Text(
                    text = task.date,
                    color = Color.White,  // Making the date stand out
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,  // Larger text size for date
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Task: ${task.name}, Time: ${task.time}",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
                Log.d("HomeScreen", "Displaying task: ${task.name}")
            }
            Log.d("HomeScreen", "LazyColumn recomposing with ${viewModel.tasks.size} tasks")
        }
        }

        // Call the AddTaskDialog composable function
        AddTaskDialog(viewModel)
}