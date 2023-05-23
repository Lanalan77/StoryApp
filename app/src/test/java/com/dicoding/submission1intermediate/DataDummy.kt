package com.dicoding.submission1intermediate

import com.dicoding.submission1intermediate.room.UserStoryEntity

object DataDummy {

    fun generateDummyQuoteResponse(): List<UserStoryEntity> {
        val items: MutableList<UserStoryEntity> = arrayListOf()
        for (i in 0..100) {
            val quote = UserStoryEntity(
                i.toString(),
                "name + $i",
                "photoStory $i",
            )
            items.add(quote)
        }
        return items
    }

}