package com.example.data.datasourceimpl

import android.util.Log
import com.example.data.MyDatabase
import com.example.data.TaskDB
import com.example.data.datasource.TaskDataSource
import com.example.domain.usecase.model.Task
import javax.inject.Inject

class TaskDataSourceImpl @Inject constructor(

    private val myDatabase: MyDatabase
) : TaskDataSource {

    override suspend fun insertTask(title: String, description: String, dueDate: String) {
        try {
            myDatabase.myDatabaseQueries.insertTask(title, description, dueDate)
        } catch (ex: Exception) {
            Log.d("TAG", "insertTask: ${ex.localizedMessage}")

        }
    }

    override suspend fun getAllTasks(): List<Task> {
        try {
            return myDatabase.myDatabaseQueries.getAllTasks().executeAsList().map { taskDB ->
                Task(
                    id = taskDB.id,
                    title = taskDB.title,
                    description = taskDB.description,
                    dueDate = taskDB.date
                )
            }
        } catch (ex: Exception) {
            throw ex
        }
    }
}
