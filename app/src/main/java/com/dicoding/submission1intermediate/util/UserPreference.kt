package com.dicoding.submission1intermediate.util

import android.content.Context

class UserPreference(context: Context) {

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val TOKEN = "token"
        private const val IS_LOGIN = "is_login"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSession(value: UserPrefData) {
        val editor = preferences.edit()
        editor.putString(TOKEN, value.apiToken)
        editor.putBoolean(IS_LOGIN, value.isLogin)
        editor.apply()
    }

    fun getSession(): UserPrefData {
        val model = UserPrefData()
        model.apiToken = preferences.getString(TOKEN, "")
        model.isLogin = preferences.getBoolean(IS_LOGIN, false)
        return model
    }

}