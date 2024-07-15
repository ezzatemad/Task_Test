package com.example.task.di.usecasemodule

import com.example.domain.usecase.repository.TaskRepository
import com.example.domain.usecase.usecase.InsertTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideInsertTaskUseCase(taskRepository: TaskRepository): InsertTaskUseCase {
        return InsertTaskUseCase(taskRepository)
    }
}