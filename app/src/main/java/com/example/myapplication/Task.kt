package com.example.myapplication

data class Task(val id: Int, val title: String, val priority: Priority)
enum class Priority { LOW, MEDIUM, HIGH }
data class Habits(val title:String)


