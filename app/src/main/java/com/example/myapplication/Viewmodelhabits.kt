package com.example.myapplication

import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val habitsList = mutableListOf<Habits>()
    fun addHabit(habit:Habits) {
        habitsList.add(habit)
    }
}