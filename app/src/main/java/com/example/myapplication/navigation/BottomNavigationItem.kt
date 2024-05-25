package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItem(
        title="Home",
        icon = Icons.Default.Home,
        route = Screens.HomeScreen.name
        ),
    NavItem(
        title="Calendar",
        icon = Icons.Default.DateRange,
        route = Screens.CalendarScreen.name
    ),
    NavItem(
        title="Habits",
        icon = Icons.Default.Favorite,
        route = Screens.HabitsScreen.name
    ),
    NavItem(
        title="List",
        icon = Icons.Default.Check,
        route = Screens.ListScreen.name
    ),
)

