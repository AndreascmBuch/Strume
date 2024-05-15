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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

enum class Frequency {
    DAILY,
    EVERY_SECOND_DAY,
    WEEKLY,
}

data class Habit(
    val id: Int,
    val name: String,
    var frequency: Frequency,
    private var _streak: Int
) {
    var streak: Int by mutableIntStateOf(_streak)
        private set

    fun incrementStreak() {
        _streak++
        streak = _streak
    }
}

class HabitsViewModel : ViewModel() {
    val habits = mutableListOf<Habit>()

    fun addHabit(name: String, frequency: Frequency, streak: Int) {
        val habit = Habit(
            id = habits.size + 1,
            name = name,
            frequency = frequency,
            _streak = streak
        )
        habits.add(habit)
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
            text = selectedFrequency.name,
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
                    text = { Text(text = frequency.name) },
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
            .clickable {(item.incrementStreak()) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = item.name,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${item.streak}",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        DropDownMenu(
            frequencies = listOf(
                Frequency.DAILY,
                Frequency.EVERY_SECOND_DAY,
                Frequency.WEEKLY
            ),
            selectedFrequency = item.frequency,
            onFrequencySelected = { frequency ->

            }
        )
    }
}


@Composable
fun HabitsScreen() {
    val viewModel = remember { HabitsViewModel() }
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
                    frequency = Frequency.DAILY,
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
                HabitItemRow(item = item, viewModel = viewModel)
            }
        }
    }
}







