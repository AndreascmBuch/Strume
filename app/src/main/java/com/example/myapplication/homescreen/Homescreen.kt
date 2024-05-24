package com.example.myapplication.homescreen

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import com.example.myapplication.navigation.Navigation


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
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(TaskEvent.HideDialog)
        },
        title = { Text(text = "Add Task") },
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
                TextField(
                    value = state.date,
                    onValueChange = {
                        onEvent(TaskEvent.SetDate(it))
                    },
                    placeholder = {
                        Text(text = "Date")
                    }
                )
                TextField(
                    value = state.time,
                    onValueChange = {
                        onEvent(TaskEvent.SetTime(it))
                    },
                    placeholder = {
                        Text(text = "Time")
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onEvent(TaskEvent.SaveTask)
                }
            ) {
                Text(text = "Save")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onEvent(TaskEvent.HideDialog)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun HomeScreen(state: TaskState, onEvent: (TaskEvent) -> Unit) {
    Log.d("HomeScreen", "Recomposing with ${state.task.size} tasks")

    LaunchedEffect(state.task) {
        Log.d("HomeScreen", "Tasks have changed. Size: ${state.task.size}")
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

            Spacer(modifier = Modifier.height(60.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 24.dp, 24.dp, 0.dp)
            ) {
                items(state.task) { task ->
                    Log.d("HomeScreen", "Displaying task: ${task.name}, ${task.date}, ${task.time}")
                    Text(
                        text = task.date,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .padding(top = 18.dp),
                    )
                    Text(
                        text = "${task.name}, ${task.time}",
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF737483))
                            .padding(14.dp)
                            .fillMaxWidth()
                            .clickable { /*homeViewModel.editTask(task.id)*/ }
                    )
                    Log.d("HomeScreen", "Displaying task: ${task.name}")
                }
                Log.d("HomeScreen", "LazyColumn recomposing with ${state.task.size} tasks")
            }
        }
        // AddTaskDialog is conditionally displayed based on state.isAddingTask
        if (state.isAddingTask) {
            AddTaskDialog(
                state = state,
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}


