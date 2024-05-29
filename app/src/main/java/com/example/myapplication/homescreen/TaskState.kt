package com.example.myapplication.homescreen

data class TaskState(
    val task: List<Task> = emptyList(),
    val name: String = "",
    val date: String = "",
    val time: String = "",
    val isAddingTask: Boolean = false,
    val sortType: SortType = SortType.Name,
    val editingTaskId: Int? = null
)