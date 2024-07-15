package com.example.data.datasource

import com.example.data.TaskDB
import com.example.domain.usecase.model.Task


interface TaskDataSource {

    suspend fun insertTask(title: String, description: String, dueDate: String)
    suspend fun getAllTasks(): List<Task>
}