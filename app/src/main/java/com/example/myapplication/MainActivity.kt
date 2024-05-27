package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import com.example.myapplication.habitscreen.HabitDatabase
import com.example.myapplication.habitscreen.HabitViewModel
import com.example.myapplication.homescreen.TaskDatabase
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.homescreen.HomeViewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Build TaskDatabase
        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java, "Task-database"
        ).build()

        // Build HabitDatabase
        val db2 = Room.databaseBuilder(
            applicationContext,
            HabitDatabase::class.java, "Habit-database" // corrected typo here
        ).build()

        val viewModel by viewModels<HomeViewmodel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(
                        modelClass: Class<T>,
                        extras: CreationExtras
                    ): T {
                        return HomeViewmodel(db.dao) as T
                    }
                }
            }
        )

        val habitViewModel by viewModels<HabitViewModel>(
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(
                        modelClass: Class<T>,
                        extras: CreationExtras
                    ): T {
                        return HabitViewModel(db2.habitDao) as T
                    }
                }
            }
        )
        installSplashScreen()
        setContent {
            val state by viewModel.state.collectAsState()
            val state2 by habitViewModel.state.collectAsState()
            Navigation(
                state,
                onEventForTask = viewModel::onEventForTask,
                state2,
                habitViewModel::onEventForHabit
            )
        }

    }
}

