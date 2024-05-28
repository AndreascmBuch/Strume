package com.example.myapplication.homescreen


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    val name: String,
    val date: String,
    val time: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
