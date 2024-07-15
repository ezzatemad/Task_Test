package com.example.data.di.datasourcemodule

import com.example.data.MyDatabaseQueries
import com.example.data.datasource.TaskDataSource
import com.example.data.datasourceimpl.TaskDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {


    @Binds
    abstract fun provideTaskDataSource(taskDataSourceImpl: TaskDataSourceImpl): TaskDataSource


}