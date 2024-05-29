package com.example.myapplication.homescreen

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Task::class],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDao
}
