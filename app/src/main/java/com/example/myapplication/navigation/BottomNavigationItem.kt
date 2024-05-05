package com.example.myapplication.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

val items = listOf(
    BottomNavigationItem(
        title = "home",
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
        hasNews = false
    ),
    BottomNavigationItem(
        title = "calendar",
        selectedIcon = Icons.Filled.List,
        unSelectedIcon = Icons.Outlined.List,
        hasNews = false,
    ),
    BottomNavigationItem(
        title = "habits",
        selectedIcon = Icons.Filled.Favorite,
        unSelectedIcon = Icons.Outlined.Favorite,
        hasNews = false,
    ),
    BottomNavigationItem(
        title = "ist",
        selectedIcon = Icons.Filled.CheckCircle,
        unSelectedIcon = Icons.Outlined.CheckCircle,
        hasNews = false,
    )
)

