package com.dicoding.submission1intermediate.ui.story

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submission1intermediate.data.UserStoryRepository
import com.dicoding.submission1intermediate.data.api.ApiConfig
import com.dicoding.submission1intermediate.data.response.*
import com.dicoding.submission1intermediate.di.Injection
import com.dicoding.submission1intermediate.room.UserStoryEntity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class StoryViewModel(userStoryRepository: UserStoryRepository
): ViewModel() {

    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> = _listStory

    private val _detailStory = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailStory

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val pagingStory: LiveData<PagingData<UserStoryEntity>> =
        userStoryRepository.getAllStory().cachedIn(viewModelScope)


    companion object{
        private const val TAG = "StoryViewModel"
    }

    fun getAllStory(apiToken: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllStory(apiToken, size = Int.MAX_VALUE, location = 1)
        client.enqueue(object: Callback<GetAllStoryResponse>{
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                _isLoading.value = false
                val storyResponse = response.body()
                _listStory.postValue(storyResponse?.listStory)
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getDetailStory(apiToken: String, id: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().detailStory(apiToken, id)
        client.enqueue(object: Callback<DetailStoryResponse>{
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                val detailStoryResponse = response.body()
                _detailStory.postValue(detailStoryResponse?.story)
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun addStory(apiToken: String, desc: RequestBody, photo: MultipartBody.Part){
        _isLoading.value = true
        val client = ApiConfig.getApiService().addStory(apiToken, desc, photo)
        client.enqueue(object: Callback<AddStoryResponse>{
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                val addStoryResponse = response.body()
                _msg.postValue(addStoryResponse?.message)
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

}

class ViewModelFactory(private val context: Context, private val apiToken: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(context, apiToken)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}