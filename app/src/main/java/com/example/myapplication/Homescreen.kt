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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(username: String,navController: NavController) {
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

        val tasks = remember { mutableStateListOf<String>() }
        var showDialog by remember { mutableStateOf(false) }
        var textInput by remember { mutableStateOf("") }

        // Listen med tasks
        LazyColumn {
            items(tasks) { task ->
                Text(text = task, color = Color.White, modifier = Modifier.padding(8.dp))
            }
        }
// knap til tilføjelse
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "+", color = Color.White)
        }

        // Dialog til at tilføje tasks
        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = { Text("Add New Task") },
                text = {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        label = { Text("Task Details") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                tasks.add(textInput)
                                textInput = ""
                                showDialog = false
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
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
                Button(onClick = { /* Do something */ }) {
                    Text(text = "Home")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = { /* Do something */ }) {
                    Text(text = "Calendar")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = {navController.navigate("Habits")}) {
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
