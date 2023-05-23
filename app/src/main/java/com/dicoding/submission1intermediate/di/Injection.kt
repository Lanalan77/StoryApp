package com.dicoding.submission1intermediate.di

import android.content.Context
import com.dicoding.submission1intermediate.data.UserStoryRepository
import com.dicoding.submission1intermediate.data.api.ApiConfig
import com.dicoding.submission1intermediate.room.UserStoryDatabase

object Injection {
    fun provideRepository(context: Context, apiToken: String): UserStoryRepository {
        val database = UserStoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return UserStoryRepository(database, apiService, apiToken)
    }
}