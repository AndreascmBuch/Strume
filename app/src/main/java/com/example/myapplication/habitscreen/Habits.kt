package com.example.myapplication.habitscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog
import com.example.myapplication.R

@Composable
fun DropDownMenu(
    frequencies: List<Frequency>,
    selectedFrequency: Frequency,
    onFrequencySelected: (Frequency) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
        Text(
            text = "Habit frequency: ${selectedFrequency::class.simpleName}",
            fontSize = 20.sp,
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
                    onClick = {
                        onFrequencySelected(frequency)
                        isExpanded = false
                    },
                    text = {
                        Text(text = frequency::class.simpleName ?: "")
                    }
                )
            }
        }
    }
}


@Composable
fun HabitItemRow(item: HabitData, viewModel: HabitsViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color.Gray)
            .fillMaxWidth()
            .clickable {
                if (item.streak == 0) {
                    item.incrementStreak()
                    Toast
                        .makeText(
                            context,
                            "Habit started! Keep it up!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    val incremented = item.incrementStreak()
                    if (!incremented) {
                        Toast
                            .makeText(
                                context,
                                "You have already tapped. Please wait for the required time period.",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        Toast
                            .makeText(
                                context,
                                "Streak improved!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    DropDownMenu(
                        frequencies = listOf(
                            Frequency.Daily,
                            Frequency.SecondDay,
                            Frequency.Weekly
                        ),
                        selectedFrequency = item.frequency,
                        onFrequencySelected = { frequency ->
                            viewModel.updateHabitFrequency(item.id, frequency)
                        }
                    )
                }
                Text(
                    text = "${item.streak}",
                    fontSize = 40.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(end = 5.dp, top = 1.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}



@Composable
fun AddHabitDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onHabitAdd: (String, Frequency) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val habitNameState = remember { mutableStateOf("") }
                    var selectedFrequency by remember { mutableStateOf<Frequency>(Frequency.Daily) }

                    Text(
                        text = "Add new Habit",
                        style = MaterialTheme.typography.headlineLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = habitNameState.value,
                        onValueChange = { habitNameState.value = it },
                        label = { Text("Habit Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DropDownMenu(
                        frequencies = listOf(
                            Frequency.Daily,
                            Frequency.SecondDay,
                            Frequency.Weekly
                        ),
                        selectedFrequency = selectedFrequency,
                        onFrequencySelected = { frequency ->
                            selectedFrequency = frequency
                        },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            onHabitAdd(habitNameState.value, selectedFrequency)
                            habitNameState.value = ""
                            onDismissRequest()
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HabitsScreen(viewModel: HabitsViewModel = viewModel()) {
    val showDialog by viewModel.showAddHabitDialog
    Box( modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0xFF4E4853))) {
        Image(
            painter = painterResource(id = R.drawable.homescreenshapes),
            contentDescription = "Shape",
            contentScale = ContentScale.Crop,  // Ensure the image covers the top area
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Habits",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold)
            Text(
                text = "Keep up your good habits for a healthy life",
                color = Color.White,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.height(150.dp))
            LazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                items(items = viewModel.habits) { item ->
                    HabitItemRow(
                        item = item,
                        viewModel = viewModel
                    )
                }
            }
        }

        AddHabitDialog(
            showDialog = showDialog,
            onDismissRequest = { viewModel.hideAddHabitDialog() },
            onHabitAdd = { habitName, frequency ->
                viewModel.addHabit(
                    name = habitName,
                    frequency = frequency,
                    streak = 0
                )
                viewModel.hideAddHabitDialog()
            }
        )
    }
}