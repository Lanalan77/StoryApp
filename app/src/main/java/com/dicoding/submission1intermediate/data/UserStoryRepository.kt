package com.dicoding.submission1intermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dicoding.submission1intermediate.data.api.ApiService
import com.dicoding.submission1intermediate.room.UserStoryDatabase
import com.dicoding.submission1intermediate.room.UserStoryEntity
import com.dicoding.submission1intermediate.util.PagingRemoteMediator

class UserStoryRepository(private val storyDatabase: UserStoryDatabase, private val apiService: ApiService, private val apiToken: String) {
    fun getAllStory(): LiveData<PagingData<UserStoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = PagingRemoteMediator(storyDatabase, apiService, apiToken),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.userStoryDao().getAllStory()
            }
        ).liveData
    }
}