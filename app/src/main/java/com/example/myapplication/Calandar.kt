import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calendar() {
    var selectedDate by remember { mutableIntStateOf(-1) }
    val weekdays = listOf("mon", "tue", "wed", "thu", "fri", "sat", "sun")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color(0xFF4E4853))
            .padding(bottom = 48.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Plan your life",
                fontSize = 36.sp,
                color = Color.White,
                modifier = Modifier.padding(top = 42.dp, bottom = 4.dp)
            )
            Text(
                text = "Add event forward in time",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "june 2024",
                fontSize = 22.sp,
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
                // Displaying weekdays
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

        // A grid displaying the dates of the month
        val daysInMonth = 30
        val firstDayOffset = 5 // June 1st is a Saturday, so offset is 5 (0-based index for Saturday)
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
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f)
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (selectedDate == date) Color(0x8021B6E8) else Color.Transparent)
                                .clickable { selectedDate = date }
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
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    Calendar()
}
