package com.dicoding.submission1intermediate.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserPrefData(
    var isLogin: Boolean = false,
    var apiToken: String? = null
) : Parcelable
