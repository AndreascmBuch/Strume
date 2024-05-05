package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Calendar
import com.example.myapplication.HabitsScreen
import com.example.myapplication.HomeScreen
import com.example.myapplication.MyViewModel
import com.example.myapplication.Welcome


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Welcome") {
        composable("Welcome") {
            Welcome(navController)
        }
        composable("Homepage/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            HomeScreen(username = username, navController)
        }
        composable("Habits") {
            HabitsScreen(MyViewModel())

        }
        composable("Calendar") {
            Calendar()
        }
    }
}



