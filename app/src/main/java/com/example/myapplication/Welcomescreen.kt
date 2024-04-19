package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Welcome() {
    Box() {
        Column {
            Text(text = "Welcome to Strume")
            // Button for navigation to homepage
            Button(onClick = { /*TODO*/ }) {
            }
            Text(text = "Click here to continue")
        }

    }
}