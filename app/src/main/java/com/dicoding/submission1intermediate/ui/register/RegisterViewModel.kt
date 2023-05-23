package com.dicoding.submission1intermediate.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submission1intermediate.data.api.ApiConfig
import com.dicoding.submission1intermediate.data.api.ApiService
import com.dicoding.submission1intermediate.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    private val _isEmailTaken = MutableLiveData<Boolean>()
    val isEmailTaken: LiveData<Boolean> = _isEmailTaken

    private val _postRegister = MutableLiveData<RegisterResponse>()
    val postRegister: LiveData<RegisterResponse> = _postRegister

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object {
        const val TAG = "RegisterViewModel"
    }

    fun procRegister(name: String, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                val registerResponse = response.body()
                if (registerResponse != null) {
                    _message.postValue(registerResponse.message)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


}