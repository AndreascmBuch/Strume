package com.example.myapplication.homescreen

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.layout.ContentScale

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

    fun showDatePicker() {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val localDate = LocalDate.of(year, month + 1, dayOfMonth)
                val dateString = localDate.format(
                    DateTimeFormatter.ofPattern("EEEE, d'${getDayOfMonthSuffix(dayOfMonth)}' MMMM")
                )
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
                Button(onClick = { showDatePicker() }) {
                    Text("Select Date: ${state.date}")
                }
                Button(onClick = { showTimePicker() }) {
                    Text("Select Time: ${state.time}")
                }
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Button(
                    onClick = { onEvent(TaskEvent.HideDialog) }
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = { onEvent(TaskEvent.SaveTask) }
                ) {
                    Text(text = if (state.editingTaskId == null) "Save" else "Update")
                }
                if (state.editingTaskId != null) { // Only show "Delete" button if editing a task
                    Button(
                        onClick = {
                            val task = state.task.find { it.id == state.editingTaskId }
                            if (task != null) {
                                onEvent(TaskEvent.DeleteTask(task))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
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

    val tasksGroupedByDate = state.task.groupBy { it.date }

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
                        val bottomPadding = if (index < tasks.size - 1) 8.dp else 0.dp
                        Text(
                            text = "${task.name}, ${task.time}",
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFF737483))
                                .padding(14.dp)
                                .fillMaxWidth()
                                .padding(bottom = bottomPadding)
                                .clickable { viewModel.onEventForTask(TaskEvent.EditTask(task)) }
                        )
                        Log.d("HomeScreen", "Displaying task: ${task.name}")
                    }
                    Log.d("HomeScreen", "LazyColumn recomposing with ${state.task.size} tasks")
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
}
