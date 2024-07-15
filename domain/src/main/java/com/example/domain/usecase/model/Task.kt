package com.example.domain.usecase.model

data class Task(
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val dueDate: String? = null
)
