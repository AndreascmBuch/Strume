import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calendar() {
    var selectedDate by remember { mutableIntStateOf(-1) }
    val weekdays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Plan your life",
            fontSize = 24.dp,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        Text(
            text = "Add event forward in time",
            fontSize = 18.dp,
            modifier = Modifier.padding(bottom = 24.dp))
        Text(
            text = "June 2024",
            fontSize = 18.dp, modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, bottom = 32.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Displaying weekdays
            weekdays.forEach { day -> Text(
                text = day,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f))
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
                                .background(if (selectedDate == date) Color(0x8021B6E8) else Color.Transparent)
                                .clickable { selectedDate = date }
                        ) {
                            Text(
                                text = "$date",
                                fontSize = 16.dp)
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(1f)
                                .size(48.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Calendar()
}
