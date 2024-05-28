package com.example.myapplication.habitscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun AddHabitDialog(
    state: HabitState,
    onEvent: (HabitEvent) -> Unit,
) {
    if (state.isAddingHabit) {
        AlertDialog(
            onDismissRequest = { onEvent(HabitEvent.HideDialog) },
            title = { Text(text = if (state.editingHabitId == null) "Add Habit" else "Edit Habit") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        value = state.name,
                        onValueChange = { onEvent(HabitEvent.SetName(it)) },
                        placeholder = { Text(text = "Habit Name") }
                    )
                    DropDownMenu(
                        frequencies = listOf(Habit.Frequency.Daily, Habit.Frequency.SecondDay, Habit.Frequency.Weekly),
                        selectedFrequency = state.frequency,
                        onFrequencySelected = { onEvent(HabitEvent.SetFrequency(it)) }
                    )
                }
            },
            confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(all = 8.dp)
                ) {
                    Button(onClick = { onEvent(HabitEvent.HideDialog) }) { Text(text = "Cancel") }
                    Button(onClick = {
                        val habit = Habit(
                            name = state.name,
                            frequency = state.frequency,
                            streak = 0,
                            id = state.editingHabitId ?: 0
                        )
                        onEvent(HabitEvent.SaveHabit(habit))
                    }) {
                        Text(text = if (state.editingHabitId == null) "Save" else "Update")
                    }
                }
            }
        )
    }
}

@Composable
fun HabitsScreen(viewModel: HabitViewModel) {
    val state by viewModel.state.collectAsState()
    Log.d("HomeScreen", "Recomposing with ${state.habits.size} tasks")
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF4E4853))) {
        Image(
            painter = painterResource(id = R.drawable.homescreenshapes),
            contentDescription = "Shape",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Habits",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 50.dp)
            )
            Text(
                text = "Keep up your good habits for a healthy life",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(150.dp))

            LazyColumn(contentPadding = PaddingValues(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items = state.habits) { habit ->
                    HabitItemRow(habit, viewModel)
                }
            }
        }
        Log.d("HomeScreen", "Displaying Habits: ${state.habits}")
        AddHabitDialog(
            state = state,
            onEvent = { viewModel.onEventForHabit(it) }
        )
    }
}

@Composable
fun HabitItemRow(habit: Habit, viewModel: HabitViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Gray)
            .fillMaxWidth()
            .clickable {
                if (habit.streak == 0) {
                    viewModel.onEventForHabit(HabitEvent.IncrementStreak(habit))
                    Toast
                        .makeText(context, "Habit started! Keep it up!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val now = LocalDateTime.now()
                    val lastUpdated = habit.lastUpdated ?: now.minusDays(1)
                    val canIncrement = when (habit.frequency) {
                        Habit.Frequency.Daily -> ChronoUnit.DAYS.between(lastUpdated, now) >= 1
                        Habit.Frequency.SecondDay -> ChronoUnit.DAYS.between(lastUpdated, now) >= 2
                        Habit.Frequency.Weekly -> ChronoUnit.WEEKS.between(lastUpdated, now) >= 1
                    }

                    if (canIncrement) {
                        viewModel.onEventForHabit(HabitEvent.IncrementStreak(habit))
                        Toast
                            .makeText(context, "Streak improved!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast
                            .makeText(
                                context,
                                "You have already tapped. Please wait for the required time period.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = habit.name, fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(start = 8.dp))
                DropDownMenu(
                    frequencies = listOf(Habit.Frequency.Daily, Habit.Frequency.SecondDay, Habit.Frequency.Weekly),
                    selectedFrequency = habit.frequency,
                    onFrequencySelected = { newFrequency ->
                        viewModel.onEventForHabit(HabitEvent.SaveHabit(habit.copy(frequency = newFrequency)))
                    }
                )
            }
            Text(text = "Streak: ${habit.streak}", color = Color.White, modifier = Modifier.padding(end = 8.dp))
            Icon(Icons.Default.Favorite, contentDescription = "Heartbeat", tint = Color.Red)
            IconButton(onClick = { viewModel.onEventForHabit(HabitEvent.DeleteHabit(habit)) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    }
}

@Composable
fun DropDownMenu(
    frequencies: List<Habit.Frequency>,
    selectedFrequency: Habit.Frequency,
    onFrequencySelected: (Habit.Frequency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box() {
        Text(
            text = "Frequency: ${selectedFrequency.displayName}",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            color = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            frequencies.forEach { frequency ->
                DropdownMenuItem(
                    onClick = {
                        onFrequencySelected(frequency)
                        expanded = false // Close the dropdown after selection
                    },
                    text = { Text(text = "Frequency: ${frequency.displayName}")}
                )
            }
        }
    }
}