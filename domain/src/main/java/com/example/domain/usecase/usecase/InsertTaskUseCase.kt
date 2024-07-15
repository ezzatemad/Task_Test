package com.example.domain.usecase.usecase

import android.util.Log
import com.example.domain.usecase.model.Task
import com.example.domain.usecase.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {

    suspend fun insertTask(title: String, description: String, dueDate: String) {
        try {
            taskRepository.insertTask(title, description, dueDate)
        } catch (ex: Exception) {
            Log.d("TAG", "insertTask: ${ex.localizedMessage}")
        }
    }

    suspend fun getAllTasks(): List<Task> {
        try {
            return taskRepository.getAllTasks()
        } catch (ex: Exception) {
            throw ex
        }
    }
}
