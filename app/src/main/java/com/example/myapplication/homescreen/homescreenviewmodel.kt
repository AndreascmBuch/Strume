package com.example.myapplication.homescreen


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
) : ViewModel() {
    // MutableStateFlow til at holde styr på sorteringstypen
    private val _sortType = MutableStateFlow(SortType.Name)

    // Strøm af opgaver, som ændrer sig baseret på sorteringstypen
    private val _task = _sortType.flatMapConcat { sortType ->
        when (sortType) {
            SortType.Name -> dao.getTaskOrderByName()
            SortType.Date -> dao.getTaskOrderByDate()
            SortType.Time -> dao.getTaskOrderByTime()
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    // MutableStateFlow til at holde applikationens nuværende tilstand
    private val _state = MutableStateFlow(TaskState())

    // Kombinerer _state, _sortType og _task for at danne den samlede tilstand
    val state = combine(_state, _sortType, _task) { state, sortType, task ->
        state.copy(
            task = task,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskState())

    // Funktion til at håndtere forskellige hændelser
    fun onEventForTask(event: TaskEvent) {
        when (event) {
            is TaskEvent.DeleteTask -> {
                // Starter en coroutine for at håndtere sletning uden for hovedtråden
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
                // Opdaterer tilstanden for at skjule dialogen
                _state.update { it.copy(isAddingTask = false) }
            }

            is TaskEvent.SaveTask -> {
                val name = state.value.name
                val date = state.value.date
                val time = state.value.time
                if (name.isBlank() || date.isBlank() || time.isBlank()) {
                    return
                }
                val task = Task(
                    name = name,
                    date = date,
                    time = time,
                    id = state.value.editingTaskId ?: 0
                )
                // Starter en coroutine for at håndtere gemning/opdatering uden for hovedtråden
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
                // Opdaterer tilstanden med den valgte dato
                _state.update { it.copy(date = event.date) }
            }

            is TaskEvent.SetTime -> {
                // Opdaterer tilstanden med den valgte tid
                _state.update { it.copy(time = event.time) }
            }

            is TaskEvent.SetName -> {
                // Opdaterer tilstanden med det indtastede navn
                _state.update { it.copy(name = event.name) }
            }

            is TaskEvent.ShowDialog -> {
                // Viser dialogen og nulstiller redigeringstilstanden
                _state.update { it.copy(isAddingTask = true, editingTaskId = null) }
            }

            is TaskEvent.SortTask -> {
                // Opdaterer sorteringstypen
                _sortType.value = event.sortType
            }

            is TaskEvent.EditTask -> {
                val task = event.task
                // Opdaterer tilstanden for at starte redigering af en eksisterende opgave
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