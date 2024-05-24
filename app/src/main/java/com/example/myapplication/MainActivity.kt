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
import com.example.myapplication.homescreen.HomeScreen
import com.example.myapplication.homescreen.TaskDatabase
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.homescreen.HomeViewmodel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java, "Task-database"
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
        installSplashScreen()
        setContent {
            val state by viewModel.state.collectAsState()
            Navigation(state,onEvent = viewModel::onEvent)
            }
        }
    }

