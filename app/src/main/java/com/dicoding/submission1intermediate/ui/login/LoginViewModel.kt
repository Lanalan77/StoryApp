package com.dicoding.submission1intermediate.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission1intermediate.data.api.ApiConfig
import com.dicoding.submission1intermediate.data.response.LoginResponse
import com.dicoding.submission1intermediate.data.response.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {
    private val _postLogin = MutableLiveData<LoginResult>()
    val postLogin: LiveData<LoginResult> = _postLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    companion object{
        private const val TAG = "LoginViewModel"
    }

    fun procLogin(context: Context, email: String, password: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false

                val loginResponse = response.body()
                _token.postValue(loginResponse?.loginResult?.token)

                if (loginResponse != null) {
                    _postLogin.postValue(loginResponse.loginResult)
                    _message.postValue(loginResponse.message)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }


}