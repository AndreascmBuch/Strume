package com.example.myapplication.homescreen

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task ORDER BY name ASC")
    fun getTaskOrderByName(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY date ASC")
    fun getTaskOrderByTime(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY time ASC")
    fun getTaskOrderByDate(): Flow<List<Task>>
}
