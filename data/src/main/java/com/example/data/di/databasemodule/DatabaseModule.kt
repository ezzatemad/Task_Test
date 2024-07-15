package com.example.data.di.databasemodule

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.data.MyDatabase
import com.example.data.MyDatabaseQueries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MyDatabase {
        val driver = AndroidSqliteDriver(MyDatabase.Schema, context, "mydatabase.db")
        return MyDatabase(driver)
    }

    @Provides
    fun provideTaskQueries(database: MyDatabase): MyDatabaseQueries {
        return database.myDatabaseQueries
    }
}
