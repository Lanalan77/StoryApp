package com.dicoding.submission1intermediate.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.dicoding.submission1intermediate.data.api.ApiService
import com.dicoding.submission1intermediate.data.response.GetAllStoryResponse
import com.dicoding.submission1intermediate.room.RemoteKeys
import com.dicoding.submission1intermediate.room.UserStoryDatabase
import com.dicoding.submission1intermediate.room.UserStoryEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

@OptIn(ExperimentalPagingApi::class)
class PagingRemoteMediator(
    private val database: UserStoryDatabase,
    private val apiService: ApiService,
    private val apiToken: String

) : RemoteMediator<Int, UserStoryEntity>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserStoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val allStory = ArrayList<UserStoryEntity>()

            var endOfPaging = false
            val responseData = apiService.getAllStory(apiToken, page, size = state.config.pageSize, location = 0 )
            responseData.enqueue(object: Callback<GetAllStoryResponse>{
                override fun onResponse(
                    call: Call<GetAllStoryResponse>,
                    response: Response<GetAllStoryResponse>
                ) {
                    val responseStoryData = response.body()?.listStory
                    if (responseStoryData != null){
                        for (storyList in responseStoryData){
                            allStory.add(UserStoryEntity(storyList.id, storyList.name, storyList.photoUrl, storyList.lat as Double?, storyList.lon as Double? ))

                        }
                    }else{
                        endOfPaging = true
                    }

                    Executors.newSingleThreadExecutor().execute {
                        if (loadType == LoadType.REFRESH) {
                            database.remoteKeysDao().deleteRemoteKeys()

                            database.userStoryDao().deleteAll()
                        }
                        val prevKey = if (page == 1) null else page - 1
                        val nextKey = if (endOfPaging) null else page + 1
                        val keys = allStory.map {
                            RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                        }
                        database.userStoryDao().insertAllStory(allStory)
                        database.remoteKeysDao().insertAll(keys)
                    }
                }

                override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {

                }

            })

            return MediatorResult.Success(endOfPaginationReached = endOfPaging)

        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }


    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, UserStoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, UserStoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, UserStoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

}
