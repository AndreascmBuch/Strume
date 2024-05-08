package com.example.myapplication.welcomescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myapplication.R
import com.example.myapplication.navigation.Screens


@Composable
fun Welcome(navController: NavController) {
    val (username, setValue) = remember { mutableStateOf("") }
    val image = painterResource(R.drawable.strume_logo)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853)),
        contentAlignment = Alignment.Center

    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Image(
                painter = image,
                contentDescription = "Strume logo",
                alpha = 1f
            )
            Text(text = "Welcome to Strume", color = Color.White)
            TextField(
                value = username,
                onValueChange = { newUsername -> setValue(newUsername) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textStyle = TextStyle(color = Color.Black)
            )
            // Navigation and take username to homepage
            Button(
                onClick = {
                    if (username.isNotBlank()) {
                        navController.navigate("${Screens.HomeScreen.name}") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6597DD))
            ) {
                Text(text = "Continue", color = Color.White)
            }
        }
    }
}


