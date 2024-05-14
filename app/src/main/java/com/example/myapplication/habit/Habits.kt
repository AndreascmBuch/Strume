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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class Frequency {
    DAILY,
    EVERY_SECOND_DAY,
    WEEKLY,
}

data class Habit(
    val id: Int,
    val name: String,
    var frequency: Frequency,
    val streak: Int
)

@Composable
fun HabitsScreen() {
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
        val itemsList = listOf(
            Habit(1, "Exercise", Frequency.DAILY, 1),
            Habit(2, "Healthy Eating", Frequency.EVERY_SECOND_DAY, 2),
            Habit(3, "Adequate Sleep", Frequency.WEEKLY, 2),
            Habit(4, "Meditation", Frequency.EVERY_SECOND_DAY, 3),
            Habit(5, "Hydration", Frequency.DAILY, 4)
        )

        LazyColumn {
            items(items = itemsList) { item ->
                HabitItemRow(item = item)
            }


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
fun HabitItemRow(item: Habit) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Gray)
            .fillMaxWidth()
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
            onFrequencySelected = {
                // Here, you can update the frequency of the habit
                // You might want to update the habit in your ViewModel or repository
            }
        )
    }
}









