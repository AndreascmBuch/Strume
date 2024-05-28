import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.homescreen.TaskState
import com.example.myapplication.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(state: TaskState) {
    var selectedDate by remember { mutableIntStateOf(-1) }
    val weekdays = listOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")

    // Bottom sheet state functionality
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Calendar layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF4E4853))
    ) {
        Image(
            painter = painterResource(id = R.drawable.homescreenshapes),
            contentDescription = "Shape",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Your Calendar",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(top = 84.dp, bottom = 2.dp)
                )
                Text(
                    text = "Get an overview of your tasks",
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp)
            ) {
                Text(
                    text = "june 2024",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 22.dp, bottom = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    weekdays.forEach { day ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = day,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            val daysInMonth = 30 // 30 days in june
            val firstDayOffset = 5 // June 1st is a Saturday, so offset by 5 (0 indexed, so 0 is monday and 6 is sunday)
            for (week in 0 until 6) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    for (day in 0 until 7) {
                        val date = week * 7 + day - firstDayOffset + 1
                        if (date in 1..daysInMonth) {
                            // hasTask checks if any task value is present for a date
                            val hasTask = state.task.any {
                                val dayOfMonth = "\\d+".toRegex().find(it.date)?.value?.toIntOrNull()
                                dayOfMonth == date
                            }
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(if (selectedDate == date) Color(0x5E3B77F0) else Color.Transparent)
                                    .border(
                                        width = 2.dp,
                                        // If task value is present, then border, else transparent
                                        color = if (hasTask) Color(0xFF3B77F0) else Color.Transparent,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        selectedDate = date
                                        scope.launch { sheetState.show() }
                                        showBottomSheet = true
                                    }
                            ) {
                                Text(
                                    text = "$date",
                                    fontSize = 18.sp,
                                    color = Color.White
                                )
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .size(48.dp)
                            )
                        }
                    }
                }
            }
        }
        // Bottom Sheet functionality
        if (showBottomSheet) {
            val tasksForSelectedDate = state.task.filter {
                val dayOfMonth = "\\d+".toRegex().find(it.date)?.value?.toIntOrNull()
                dayOfMonth == selectedDate
            }
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(275.dp)  // Bottom sheet height
                        .padding(16.dp)
                        .background(Color(0xFF4E4853), shape = RoundedCornerShape(16.dp))
                ) {
                    // Layout of Bottom Sheet content for each date
                    LazyColumn(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(tasksForSelectedDate) { task ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 16.dp)
                                ) {
                                    Text(
                                        text = task.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(end = 24.dp)
                                ) {
                                    Text(
                                        text = task.time,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}