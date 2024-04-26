package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val habitsList = mutableStateListOf<Habits>()
    fun addHabit(habit:Habits) {
        habitsList.add(habit)
    }
}