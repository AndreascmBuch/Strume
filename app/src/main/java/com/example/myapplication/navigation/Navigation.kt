package com.example.myapplication.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Calendar
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.homescreen.AddTaskDialog
import com.example.myapplication.homescreen.HomeScreen
import com.example.myapplication.homescreen.HomeViewModel
import com.example.myapplication.welcomescreen.Welcome


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val viewModel: HomeViewModel = viewModel()

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
        },
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
                AddTaskDialog(viewModel)
                HomeScreen()
            }
            composable(Screens.CalendarScreen.name) {
                Calendar()
            }
            composable(Screens.HabitsScreen.name) {

            }
            composable(Screens.ListScreen.name) {
                // Your list screen content
            }
        }

    }
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        val fabIcon = when (currentDestination?.route) {
            Screens.HomeScreen.name -> Icons.Default.Add
            Screens.CalendarScreen.name -> Icons.Default.Add
            Screens.HabitsScreen.name -> Icons.Default.Add
            Screens.ListScreen.name -> Icons.Default.Settings
            else -> null
        }
        if (fabIcon != null) {
            FloatingActionButton(
                onClick = {
                    // Perform action based on the current destination
                    when (currentDestination?.route) {
                        Screens.HomeScreen.name -> {
                            viewModel.showAddTaskDialog()
                        }
                        Screens.CalendarScreen.name -> {
                            // Action for Calendar screen
                        }
                        Screens.HabitsScreen.name -> {
                            // Action for Habits screen
                        }
                        Screens.ListScreen.name -> {
                            // Action for List screen
                        }
                    }
                },
                shape = RoundedCornerShape(30.dp),
            ) {
                Icon(fabIcon, contentDescription = "Fab")
            }
        }
    }
}







