package com.dicoding.submission1intermediate.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserStoryDao {
    @Query("SELECT * FROM UserStoryEntity")
    fun getAllStory(): PagingSource<Int, UserStoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStory(story: List<UserStoryEntity>)

    @Query("DELETE FROM UserStoryEntity")
    fun deleteAll()
}