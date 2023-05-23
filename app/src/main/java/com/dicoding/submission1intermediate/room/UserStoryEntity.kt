package com.dicoding.submission1intermediate.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

@Entity
class UserStoryEntity (
    @PrimaryKey
    @field:SerializedName("id")
    val id: String,

    @field: SerializedName("user")
    var user: String? = null,

    @field: SerializedName("photoStory")
    var photoStory: String? = null,

    @field: SerializedName("lat")
    var lat: Double? = null,

    @field: SerializedName("lon")
    var lon: Double? = null
)