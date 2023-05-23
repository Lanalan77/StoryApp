package com.dicoding.submission1intermediate.data.api

import com.dicoding.submission1intermediate.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStory(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int
    ): Call<GetAllStoryResponse>


    @GET("stories/{id}")
    fun detailStory(
        @Header("Authorization") token: String,
        @Path("id") idStory: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part("description") desc: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<AddStoryResponse>



}