package com.example.myapplication.homescreen

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import java.time.LocalTime

fun getDayOfMonthSuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}
@Composable
fun AddTaskDialog(
    state: TaskState,
    onEvent: (TaskEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    // Using proper date and time formatters
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy") // Updated pattern to match the log output
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Function to show date picker and format the date correctly
    fun showDatePicker() {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                val dateString = localDate.format(dateFormatter)
                Log.d("AddTaskDialog", "Selected Date: $dateString")
                onEvent(TaskEvent.SetDate(dateString))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun showTimePicker() {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val timeString = String.format("%02d:%02d", hourOfDay, minute)
                Log.d("AddTaskDialog", "Selected Time: $timeString")
                onEvent(TaskEvent.SetTime(timeString))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideDialog)
        },
        title = { Text(text = if (state.editingTaskId == null) "Add Task" else "Edit Task") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = state.name,
                    onValueChange = {
                        onEvent(TaskEvent.SetName(it))
                    },
                    placeholder = {
                        Text(text = "Task Name")
                    }
                )
            }
        },
        confirmButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { showDatePicker() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xE63B77F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = if (state.editingTaskId == null) "Select Date" else "Edit Date")
                    }
                    Button(
                        onClick = { showTimePicker() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xE63B77F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = if (state.editingTaskId == null) "Select Time" else "Edit Time")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onEvent(TaskEvent.HideDialog) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xE63B77F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Cancel")
                    }
                    Button(
                        onClick = { onEvent(TaskEvent.SaveTask) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xE63B77F0)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = if (state.editingTaskId == null) "Save" else "Save")
                    }
                }
                if (state.editingTaskId != null) { // Only show "Delete" button if editing a task
                    Button(
                        onClick = {
                            val task = state.task.find { it.id == state.editingTaskId }
                            if (task != null) {
                                onEvent(TaskEvent.DeleteTask(task))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xE6A32920)),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                            .heightIn(min = 40.dp)
                            .fillMaxWidth(0.6f)
                    ) {
                        Text(text = "Delete")
                    }
                }
            }
        },
        dismissButton = {
            // This can be left empty if you don't want an additional dismiss button
        }
    )
}

@Composable
fun HomeScreen(viewModel: HomeViewmodel) {
    val state by viewModel.state.collectAsState()
    Log.d("HomeScreen", "Recomposing with ${state.task.size} tasks")

    LaunchedEffect(state.task) {
        Log.d("HomeScreen", "Tasks have changed. Size: ${state.task.size}")
    }

    // Added date formatter for parsing the date strings
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy") // Updated pattern to match the log output

    // Sort tasks by date and time
    val sortedTasks = state.task.sortedWith(compareBy(
        // Parsing the date and time strings to LocalDate and LocalTime
        {
            try {
                val parsedDate = LocalDate.parse(it.date.replace(Regex("(st|nd|rd|th)"), ""), dateFormatter)
                Log.d("HomeScreen", "Parsed date: $parsedDate from original date: ${it.date}")
                parsedDate
            } catch (e: Exception) {
                Log.e("HomeScreen", "Error parsing date: ${it.date}", e)
                LocalDate.MIN // Use minimum date as fallback
            }
        },
        {
            try {
                val parsedTime = LocalTime.parse(it.time)
                Log.d("HomeScreen", "Parsed time: $parsedTime from original time: ${it.time}")
                parsedTime
            } catch (e: Exception) {
                Log.e("HomeScreen", "Error parsing time: ${it.time}", e)
                LocalTime.MIN // Use minimum time as fallback
            }
        }
    ))

    Log.d("HomeScreen", "Sorted Tasks: ${sortedTasks.map { "${it.date} ${it.time} ${it.name}" }}")

    val tasksGroupedByDate = sortedTasks.groupBy { it.date }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.homescreenshapes),
            contentDescription = "Shape",
            contentScale = ContentScale.Crop,
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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Transparent)
            )
            Text(
                text = "Here are your tasks for the day",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.Transparent)
            )

            Spacer(modifier = Modifier.height(60.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
            ) {
                // Iterate over the grouped tasks
                tasksGroupedByDate.forEach { (date, tasks) ->
                    // Display the date once for each group
                    item {
                        Text(
                            text = date,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .padding(top = 18.dp),
                        )
                    }
                    // Display each task under the current date
                    itemsIndexed(tasks) { index, task ->
                        // Calculate dynamic bottom padding for tasks on the same date
                        val bottomPadding = if (index < tasks.size - 1) 7.dp else 10.dp
                        Row(
                            modifier = Modifier
                                .padding(bottom = bottomPadding)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF737483))
                                .padding(14.dp)
                                .height(36.dp)
                                .fillMaxWidth()
                                .clickable { viewModel.onEventForTask(TaskEvent.EditTask(task)) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = task.name,
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = task.time,
                                    color = Color.White,
                                    fontSize = 12.sp, // Smaller font size for the time
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(37.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF6597DD))
                            )
                        }
                        Log.d("HomeScreen", "Displaying task: ${task.name}")
                    }
                }
            }
        }

        // AddTaskDialog is conditionally displayed based on state.isAddingTask
        if (state.isAddingTask) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                AddTaskDialog(
                    state = state,
                    onEvent = { viewModel.onEventForTask(it) }
                )
            }
        }
    }
}
