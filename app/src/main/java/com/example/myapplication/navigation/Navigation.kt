package com.example.myapplication.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Calendar
import com.example.myapplication.HabitsScreen
import com.example.myapplication.HomeScreen
import com.example.myapplication.MyViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.welcomescreen.Welcome

@Composable
@Preview
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != "Welcome") {
                NavigationBar(
                    modifier = Modifier.background(Color.Black),
                    containerColor = Color.Black
                ) {
                    listOfNavItems.forEach { navItem ->
                        NavigationBarItem(
                            modifier = Modifier.background(Color.Black),
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                            onClick = {
                                navController.navigate(navItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = null,
                                    tint = Color(0xFF383838)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Welcome") {
                Welcome(navController)
            }
            composable(Screens.HomeScreen.name) {
                HomeScreen()
            }
            composable(Screens.CalendarScreen.name) {
                Calendar()
            }
            composable(Screens.HabitsScreen.name) {
                HabitsScreen(MyViewModel())
            }
            composable(Screens.ListScreen.name) {
                // Your list screen content
            }
        }
    }
}





