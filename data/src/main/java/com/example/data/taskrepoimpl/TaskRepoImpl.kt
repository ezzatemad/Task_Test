package com.example.data.taskrepoimpl

import android.util.Log
import com.example.data.TaskDB
import com.example.data.datasource.TaskDataSource
import com.example.domain.usecase.model.Task
import com.example.domain.usecase.repository.TaskRepository
import javax.inject.Inject

class TaskRepoImpl @Inject constructor(
    private val taskDataSource: TaskDataSource
) : TaskRepository {

    override suspend fun insertTask(title: String, description: String, dueDate: String) {
        try {
            taskDataSource.insertTask(title, description, dueDate)

        } catch (ex: Exception) {
            Log.d("TAG", "insertTask: ${ex.localizedMessage}")
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        try {
            return taskDataSource.getAllTasks()
        } catch (ex: Exception) {
            throw ex
        }
    }
}
