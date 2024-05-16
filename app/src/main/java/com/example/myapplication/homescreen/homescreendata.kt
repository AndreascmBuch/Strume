package com.example.myapplication.homescreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Task(
    val id: Int,
    var name: String,
    var date: String,
    var time: String, // You may want to use a proper time/date type
    val icon: String // Resource ID of the icon
) {
    var observableName by mutableStateOf(name)
    var observableDate by mutableStateOf(date)
    var observableTime by mutableStateOf(time)
}