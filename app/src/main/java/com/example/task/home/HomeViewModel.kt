package com.example.task.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TaskDB
import com.example.domain.usecase.model.Task
import com.example.domain.usecase.usecase.InsertTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val insertTaskUseCase: InsertTaskUseCase
) : ViewModel() {

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    val title_error = mutableStateOf("")
    val desc_error = mutableStateOf("")


    private val _date = MutableStateFlow(
        SimpleDateFormat(
            "MMM dd, yyyy",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)
    )
    val date: StateFlow<String> get() = _date


    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> get() = _taskList

    private var sortBy = mutableStateOf("")


    fun onDueDateChange(newDate: String) {
        _date.value = newDate
    }


    fun insertTask(): Boolean {
        return if (validateField()) {
            viewModelScope.launch {
                insertTaskUseCase.insertTask(title.value, description.value, _date.value)
                getAllTasks()
            }
            title.value = ""
            description.value = ""
            true
        } else {
            false
        }
    }

    fun getAllTasks() {
        viewModelScope.launch {
            try {
                val tasks = insertTaskUseCase.getAllTasks()
                _taskList.value = when (sortBy.value) {
                    "Date" -> tasks.sortedBy { it.dueDate }
                    "Name" -> tasks.sortedBy { it.title }
                    else -> tasks // No sorting
                }
            } catch (ex: Exception) {
                Log.d("TAG", "getAllTasks: ${ex.localizedMessage}")
            }
        }
    }

    fun toggleSortByName() {
        sortBy.value = "Name"
        getAllTasks()
    }

    fun toggleSortByDate() {
        sortBy.value = "Date"
        getAllTasks()
    }

    fun validateField(): Boolean {
        if (title.value.isNullOrEmpty() || title.value.isNullOrBlank()) {
            title_error.value = "Enter Valid Title"
            return false
        } else {
            title_error.value = ""
        }
        if (description.value.isNullOrEmpty() || description.value.isNullOrBlank()) {
            desc_error.value = "Enter Valid Description"
            return false
        } else {
            desc_error.value = ""
        }
        return true
    }
}
