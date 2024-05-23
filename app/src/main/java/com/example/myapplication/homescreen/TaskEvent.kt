package com.example.myapplication.homescreen

sealed interface TaskEvent {
    data object SaveTask: TaskEvent
    data class SetName(val name:String):TaskEvent
    data class SetDate(val date:String):TaskEvent
    data class SetTime(val time:String):TaskEvent
    object ShowDialog:TaskEvent
    object HideDialog:TaskEvent
    data class SortTask(val sortType:SortType):TaskEvent
    data class DeleteTask(val task:Task):TaskEvent

}