package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

// Hovedskærmskomponenten

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
        Text(text = "Hello ", color = Color.White)
        Text(text = "Here are your tasks for the day", color = Color.White)

        LazyColumn {
            items(viewModel.tasks) { task ->
                Text(text = task, color = Color.White, modifier = Modifier.padding(8.dp))
            }
        }
        if (viewModel.showDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.hideAddTaskDialog() },
                title = { Text("Add New Task") },
                text = {
                    OutlinedTextField(
                        value = viewModel.textInput,
                        onValueChange = { viewModel.textInput = it },
                        label = { Text("Task Details") }
                    )
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
    Column(modifier = Modifier
        .padding(start = 165.dp, end = 16.dp, top = 750.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        Button(
            onClick = { viewModel.showAddTaskDialog() },
            colors = ButtonDefaults.buttonColors(Color(0xFF6597DD))
        ) {
            Text(text = "+", color = Color.White)
        }
    }
}


fun AddTaskDialog(viewModel: HomeViewModel) {
    if (viewModel.showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideAddTaskDialog() },
            title = { Text("Add New Task") },
            text = {
                OutlinedTextField(
                    value = viewModel.textInput,
                    onValueChange = { viewModel.textInput = it },
                    label = { Text("Task Details") }
                )
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
fun HomeScreen(username: String, navController: NavController) {
    val viewModel: HomeViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Hello $username", color = Color.White)
        Text(text = "Here are your tasks for the day", color = Color.White)

        LazyColumn {
            items(viewModel.tasks) { task ->
                Text(text = task, color = Color.White, modifier = Modifier.padding(8.dp))
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
