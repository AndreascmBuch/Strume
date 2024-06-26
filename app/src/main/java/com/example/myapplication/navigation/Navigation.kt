package com.example.myapplication.navigation

import Calendar
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.Checklist
import com.example.myapplication.habitscreen.HabitEvent
import com.example.myapplication.habitscreen.HabitState
import com.example.myapplication.habitscreen.HabitViewModel
import com.example.myapplication.habitscreen.HabitsScreen
import com.example.myapplication.homescreen.HomeScreen
import com.example.myapplication.homescreen.HomeViewmodel
import com.example.myapplication.homescreen.TaskEvent
import com.example.myapplication.homescreen.TaskState
import com.example.myapplication.welcomescreen.Welcome

@OptIn(UnstableApi::class)
@Composable
fun Navigation(
    homeState: TaskState,
    onEventForTask: (TaskEvent) -> Unit,
    habitState: HabitState,
    onEventForHabit: (HabitEvent) -> Unit
) {
    // Opretter en NavController til at styre navigationen
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Initialiserer ViewModels til vaner og hjem
    val habitsViewModel: HabitViewModel = viewModel()
    val homeViewModel: HomeViewmodel = viewModel()

    // Scaffold med en bundnavigation
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
                                // Naviger til den valgte rute
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
                            },
                            label = {
                                Text(
                                    text = navItem.title,
                                    color = Color.White
                                )
                            }
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        // NavHost til at definere navigationens ruter
        NavHost(
            navController = navController,
            startDestination = "Welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Welcome") {
                Welcome(navController)
            }
            composable(Screens.HomeScreen.name) {
                HomeScreen(homeViewModel)
            }
            composable(Screens.CalendarScreen.name) {
                val homeState by homeViewModel.state.collectAsState()
                Calendar(state = homeState)
            }
            composable(Screens.HabitsScreen.name) {
                HabitsScreen(habitsViewModel)
            }
            composable(Screens.ListScreen.name) {
                Checklist()
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Bestemmer hvilket ikon der skal vises på FAB baseret på den aktuelle rute
        val fabIcon = when (currentDestination?.route) {
            Screens.HomeScreen.name -> Icons.Default.Add
            Screens.CalendarScreen.name -> Icons.Default.Add
            Screens.HabitsScreen.name -> Icons.Default.Add
            Screens.ListScreen.name -> Icons.Default.Check
            else -> null
        }
        if (fabIcon != null) {
            // FloatingActionButton til at udføre handlinger baseret på den aktuelle rute
            FloatingActionButton(
                onClick = {
                    // Udfører handling baseret på den aktuelle destination
                    when (currentDestination?.route) {
                        Screens.HomeScreen.name -> {
                            Log.d("Navigation", "ViewModel instance from FAB:")
                            onEventForTask(TaskEvent.ShowDialog)
                        }

                        Screens.CalendarScreen.name -> {
                            // Handling for Kalender skærm
                        }

                        Screens.HabitsScreen.name -> {
                            Log.d("Navigation", "HabitViewModel instance from FAB:")
                            onEventForHabit(HabitEvent.ShowDialog)
                        }

                        Screens.ListScreen.name -> {
                            // Handling for List skærm
                        }
                    }
                },
                shape = RoundedCornerShape(30.dp),
                containerColor = Color(0xFF3b77f0),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(4.dp)
                    .border(
                        width = 2.dp,
                        color = Color(0xFF002163),
                        shape = CircleShape
                    )
            ) {
                Icon(fabIcon, contentDescription = "Fab")
            }
        }
    }
}