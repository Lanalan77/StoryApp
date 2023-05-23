package com.dicoding.submission1intermediate.ui.story

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryDataClass(
    var id: String,
    var name: String,
    var photo: String
): Parcelable