package com.example.myapplication

data class Task(
    val id: Int,
    val title: String,
    val details: String,
    val dueDate: Long,
    val priority: Priority
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}
