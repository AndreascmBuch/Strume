package com.example.myapplication.homescreen

import android.app.TimePickerDialog
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
import java.util.Calendar

// Hovedskærmskomponenten
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(viewModel: HomeViewModel) {
    val context = LocalContext.current

    if (viewModel.showDialog) {
        val calendar = Calendar.getInstance()

        // Launch DatePickerDialog
        fun showDatePicker() {
            // Instantiating the DatePickerDialog
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    viewModel.selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.showDatePickerDialog = false  // Set to false after date selection
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            // Display the dialog
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
                Button(onClick = { viewModel.addTask() }) {
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
fun HomeScreen() {
    val viewModel: HomeViewModel = viewModel()

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
            items(viewModel.tasks) { task ->
                Text(
                    text = "Task: ${task.name}, Date: ${task.date}, Time: ${task.time}",
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Button(
            onClick = { viewModel.showAddTaskDialog() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "+", color = Color.White)
        }

        // Call the AddTaskDialog composable function
        AddTaskDialog(viewModel)

        // Other parts of your UI...
    }
}
