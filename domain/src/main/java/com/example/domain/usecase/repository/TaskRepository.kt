package com.example.domain.usecase.repository

import com.example.domain.usecase.model.Task

interface TaskRepository {

    suspend fun insertTask(title: String, description: String, dueDate: String)
    suspend fun getAllTasks(): List<Task>


}