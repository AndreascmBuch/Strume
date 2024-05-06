package com.example.myapplication.homescreen

data class Task(
    val name: String,
    val date: String,
    val time: String, // You may want to use a proper time/date type
    val icon: String // Resource ID of the icon
)