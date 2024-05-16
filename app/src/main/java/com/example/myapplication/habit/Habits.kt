package com.example.myapplication.habit


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


sealed class Frequency {
    data object Daily : Frequency()
    data object EverySecondDay : Frequency()
    data object Weekly : Frequency()
}

data class Habit(
    val id: Int,
    val name: String,
    var initialFrequency: Frequency,
    var initialStreak: Int
) {
    var frequency: Frequency by mutableStateOf(initialFrequency)
    var streak: Int by mutableIntStateOf(initialStreak)
        private set

    fun incrementStreak() {
        streak++
    }
}




class HabitsViewModel : ViewModel() {
    val habits = mutableStateListOf<Habit>()

    fun addHabit(name: String, frequency: Frequency, streak: Int) {
        val habit = Habit(
            id = habits.size + 1,
            name = name,
            initialFrequency = frequency,
            initialStreak = streak
        )
        habits.add(habit)
    }

    fun updateHabitFrequency(id: Int, frequency: Frequency) {
        val habit = habits.find { it.id == id }
        if (habit != null) {
            habit.frequency = frequency
        }
    }
}

@Composable
fun DropDownMenu(
    frequencies: List<Frequency>,
    selectedFrequency: Frequency,
    onFrequencySelected: (Frequency) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = ("  Habit frequency: ${selectedFrequency.javaClass.simpleName}"),
            color = Color.White,
            modifier = Modifier
                .clickable(onClick = { isExpanded = true })
                .padding(8.dp)

        )

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            frequencies.forEach { frequency ->
                DropdownMenuItem(
                    text = { Text(text = frequency.javaClass.simpleName) },
                    onClick = {
                        onFrequencySelected(frequency)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun HabitItemRow(item: Habit, viewModel: HabitsViewModel) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Gray)
            .fillMaxWidth()
            .clickable { item.incrementStreak() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
            .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${item.streak}",
                fontSize = 30.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .padding(top = 20.dp,end = 10.dp)
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            DropDownMenu(
                frequencies = listOf(
                    Frequency.Daily,
                    Frequency.EverySecondDay,
                    Frequency.Weekly
                ),
                selectedFrequency = item.frequency,
                onFrequencySelected = { frequency ->
                    viewModel.updateHabitFrequency(item.id, frequency)
                }
            )
        }

    }
}

@Composable
fun HabitsScreen() {
    val viewModel: HabitsViewModel = viewModel()
    val newHabitNameState = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
            .padding(start = 16.dp, end = 16.dp, top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Habits", color = Color.White)
        Text(
            text = "Keep up your good Habits for a healthy life",
            color = Color.White
        )
        TextField(
            value = newHabitNameState.value,
            onValueChange = { newHabitNameState.value = it },
            label = { Text("New Habit Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = {
                viewModel.addHabit(
                    name = newHabitNameState.value,
                    frequency = Frequency.Daily,
                    streak = 0
                )
                newHabitNameState.value = ""
            },
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(text = "Add Habit")
        }
        LazyColumn {
            items(items = viewModel.habits) { item ->
                HabitItemRow(
                    item = item,
                    viewModel = viewModel
                )
            }
        }
    }
}








