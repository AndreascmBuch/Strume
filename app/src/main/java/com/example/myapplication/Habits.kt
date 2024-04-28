package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable

fun HabitsScreen(viewModel: MyViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var habitTitle by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Habits", color = Color.White)
        Text(
            text = "Keep up your good Habits for a healthy life",
            color = Color.White
        )

        // Add new Habit
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF6597DD))
        ) {
            Text(text = "+", color = Color.White)
        }
        LazyColumn {
            items(viewModel.habitsList) { habit ->
                Text(
                    text = habit.title,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Dialog to add a new habit
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Add New Habit") },
                text = {
                    Column {
                        TextField(
                            value = habitTitle,
                            onValueChange = { habitTitle = it },
                            label = { Text("Habit Title") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.addHabit(Habits(title = habitTitle.text))
                            showDialog = false
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xFF4E4853))
                    .padding(start = 5.dp, end = 5.dp, top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
        ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = {  }) {
                Text(text = "Home")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = { }) {
                Text(text = "Calendar")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = {}) {
                Text(text = "Habits")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Button(onClick = { /* Do something */ }) {
                Text(text = "List")
            }
        }
    }
    }

}

