package com.example.myapplication.homescreen

import android.app.TimePickerDialog
import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex

fun getDayOfMonthSuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(viewModel: HomeViewModel) {
    val context = LocalContext.current

    if (viewModel.showDialog) {
        val calendar = Calendar.getInstance()

        fun showDatePicker() {
            val datePickerDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                    viewModel.selectedDate = localDate
                    viewModel.selectedDateString = localDate.format(
                        DateTimeFormatter.ofPattern("EEEE, d'" + getDayOfMonthSuffix(dayOfMonth) + "' MMMM")
                    )
                    Log.d("DatePicker", "Selected Date String: ${viewModel.selectedDateString}")
                    viewModel.showDatePickerDialog = false
                },
                viewModel.selectedDate.year,
                viewModel.selectedDate.monthValue - 1,
                viewModel.selectedDate.dayOfMonth
            )
            datePickerDialog.show()
        }

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
            title = { Text(if (viewModel.editingTaskId == null) "Add New Task" else "Edit Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = viewModel.textInput,
                        onValueChange = { viewModel.textInput = it },
                        label = { Text("Task Details") }
                    )
                    Button(onClick = { showDatePicker() }) {
                        Text("Select Date: ${viewModel.selectedDateString}")
                    }
                    Button(onClick = { showTimePicker() }) {
                        Text("Select Time: ${viewModel.selectedTime}")
                    }
                    if (viewModel.editingTaskId != null) {
                        Button(
                            onClick = { viewModel.deleteTask(viewModel.editingTaskId!!) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Delete", color = Color.White)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { viewModel.addOrUpdateTask() }) {
                    Text(if (viewModel.editingTaskId == null) "Add" else "Update")
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
    Log.d("HomeScreen", "Recomposing with ${homeViewModel.tasks.size} tasks")

    val viewModel: HomeViewModel = viewModel()
    val tasks = homeViewModel.tasks

    LaunchedEffect(tasks) {
        Log.d("HomeScreen", "Tasks have changed. Size: ${tasks.size}")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.homescreenshapes),
            contentDescription = "Shape",
            contentScale = ContentScale.Crop,  // Ensure the image covers the top area
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        // Overlaying text on top of the image
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hello",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Transparent)
            )
            Text(
                text = "Here are your tasks for the day",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Transparent)
            )

            Spacer(modifier = Modifier.height(100.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 24.dp, 24.dp, 0.dp) // Adjusting padding to ensure it fills width
        ) {
            items(tasks) { task ->
                Log.d("HomeScreen", "Displaying task: ${task.name}, ${task.date}, ${task.time}")
                Text(
                    text = task.date,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "${task.observableName}, ${task.observableTime}",
                    color = Color.White,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF737483))
                        .padding(14.dp)
                        .fillMaxWidth()
                        .clickable { homeViewModel.editTask(task.id) }
                )
                Log.d("HomeScreen", "Displaying task: ${task.name}")
            }
            Log.d("HomeScreen", "LazyColumn recomposing with ${viewModel.tasks.size} tasks")
        }
    }

    AddTaskDialog(viewModel)
}}

