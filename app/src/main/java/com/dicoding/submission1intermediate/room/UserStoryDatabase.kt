package com.dicoding.submission1intermediate.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserStoryEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = true
)

abstract class UserStoryDatabase: RoomDatabase() {
    abstract fun userStoryDao(): UserStoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: UserStoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): UserStoryDatabase {
            if(INSTANCE == null) {
                synchronized(UserStoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UserStoryDatabase::class.java, "user_story")
                        .build()
                }
            }
            return INSTANCE as UserStoryDatabase
        }
    }

}