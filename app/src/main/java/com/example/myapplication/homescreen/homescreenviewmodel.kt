package com.example.myapplication.homescreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewmodel(
    private val dao: TaskDao
):ViewModel() {
    private val _sortType = MutableStateFlow(SortType.Name)
    private val _task = _sortType.flatMapConcat { sortType ->
        when (sortType) {
            SortType.Name -> dao.getTaskOrderByName()
            SortType.Date -> dao.getTaskOrderByDate()
            SortType.Time -> dao.getTaskOrderByTime()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(TaskState())
    val state = combine(_state, _sortType, _task) { state, sortType, task ->
        state.copy(
            task = task,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    fun onEventForTask(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                // Launching a coroutine to handle deletion off the main thread
                viewModelScope.launch {
                    dao.deleteTask(event.task)
                    _state.update {
                        it.copy(
                            isAddingTask = false,
                            name = "",
                            date = "",
                            time = "",
                            editingTaskId = null
                        )
                    }
                }
            }

            is TaskEvent.HideDialog -> {
                _state.update { it.copy(isAddingTask = false) }
            }

            is TaskEvent.SaveTask -> {
                val name = state.value.name
                val date = state.value.date
                val time = state.value.time
                if (name.isBlank() || date.isBlank() || time.isBlank()) {
                    return
                }
                val task =
                    Task(name = name, date = date, time = time, id = state.value.editingTaskId ?: 0)
                // Launching a coroutine to handle saving/updating off the main thread
                viewModelScope.launch {
                    dao.upsertTask(task)
                    _state.update {
                        it.copy(
                            isAddingTask = false,
                            name = "",
                            date = "",
                            time = "",
                            editingTaskId = null
                        )
                    }
                }
            }

            is TaskEvent.SetDate -> {
                _state.update { it.copy(date = event.date) }
            }

            is TaskEvent.SetTime -> {
                _state.update { it.copy(time = event.time) }
            }

            is TaskEvent.SetName -> {
                _state.update { it.copy(name = event.name) }
            }

            is TaskEvent.ShowDialog -> {
                _state.update { it.copy(isAddingTask = true, editingTaskId = null) }
            }

            is TaskEvent.SortTask -> {
                _sortType.value = event.sortType
            }

            is TaskEvent.EditTask -> {
                val task = event.task
                _state.update {
                    it.copy(
                        isAddingTask = true,
                        name = task.name,
                        date = task.date,
                        time = task.time,
                        editingTaskId = task.id
                    )
                }
            }
        }
    }
}

/*
class HomeViewModel : ViewModel() {
    var tasks = mutableStateListOf<Task>()
    var showDialog by mutableStateOf(false)
    var textInput by mutableStateOf("")
    var selectedTime by mutableStateOf("")
    val availableTimes = List(24 * 12) { i ->
        String.format("%02d:%02d", i / 12, (i % 12) * 5)
    }
    var selectedDate by mutableStateOf(LocalDate.now())
    var selectedDateString by mutableStateOf(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d'th' MMMM")))
    var showDatePickerDialog by mutableStateOf(false)
    var showTimePickerDialog by mutableStateOf(false)
    private var nextTaskId = 0  // To assign unique IDs
    var editingTaskId by mutableStateOf<Int?>(null)

    fun addOrUpdateTask() {
        val date = selectedDateString
        val time = selectedTime
        editingTaskId?.let {taskId ->
            // Find and update existing task
            tasks.find { task -> task.id == taskId }?.apply {
                this.observableName = textInput  // Update observable property
                this.observableDate = date      // Update observable property
                this.observableTime = time
            }
        } ?: run {
            // Add new task
            val task = Task(nextTaskId++, textInput, date, time, "default_icon")
            tasks.add(task)
        }
        showDialog = false
        resetInputs()
    }

    fun editTask(taskId: Int) {
        editingTaskId = taskId
        tasks.find { it.id == taskId }?.let {
            textInput = it.name
            selectedDateString = it.date
            selectedTime = it.time
        }
        showDialog = true
    }

    fun deleteTask(taskId: Int) {
        tasks.removeIf { it.id == taskId }
        hideAddTaskDialog()  // Close the dialog after deleting the task
    }

    private fun resetInputs() {
        textInput = ""
        selectedTime = availableTimes.first()
        editingTaskId = null
    }

    private fun safeParseTime(time: String): LocalTime {
        return try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"))
        } catch (e: DateTimeParseException) {
            LocalTime.MIN  // Default to the minimum time if parsing fails
        }
    }

    fun showAddTaskDialog() {
        editingTaskId = null  // Reset the editing ID to ensure we are adding a new task
        showDialog = true
    }

    fun hideAddTaskDialog() {
        showDialog = false
        resetInputs()  // Reset inputs on dialog close
    }
}*/