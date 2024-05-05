package com.example.myapplication.navigation
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Calendar
import com.example.myapplication.HabitsScreen
import com.example.myapplication.HomeScreen
import com.example.myapplication.MyViewModel


@Composable

fun MainNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) {val padding = it  }
        NavHost(
            navController = navController,
            startDestination = "homepage"
        ) {
            composable("homepage") {
                HomeScreen()
            }
            composable("habits") {
                HabitsScreen(MyViewModel())
            }
            composable("calendar") {
                Calendar()
            }
        }
    }


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            badgeCount = null,
            hasNews = false,
            selectedIcon = Icons.Default.Home,
            unSelectedIcon = Icons.Default.Home
        ),
        BottomNavigationItem(
            title = "Habits",
            badgeCount = null, // You can change this as per your requirement
            hasNews = false, // You can change this as per your requirement
            selectedIcon = Icons.Default.Search,
            unSelectedIcon = Icons.Default.Search
        ),
        BottomNavigationItem(
            title = "Calendar",
            badgeCount = null, // You can change this as per your requirement
            hasNews = false, // You can change this as per your requirement
            selectedIcon = Icons.Default.,
            unSelectedIcon = Icons.Default.CalendarToday
        )
    )

    BottomNavigationBar(
        items = items,
        onItemClick = { index ->
            when (index) {
                0 -> navController.navigate("homepage")
                1 -> navController.navigate("habits")
                2 -> navController.navigate("calendar")
            }
        }
    )
}


