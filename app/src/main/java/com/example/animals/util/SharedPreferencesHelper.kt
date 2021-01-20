package com.example.animals.util

import android.content.Context


class SharedPreferencesHelper(context: Context) {

    private val PREF_API_KEY = "Api_key"
    private val settings = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    private val editor = settings.edit()


    fun saveApiKey(key: String?) {
        editor.putString(PREF_API_KEY, key)
        editor.apply()
    }

    fun getApiKey(): String? {
        return if (settings.contains(PREF_API_KEY)) {
            settings.getString(PREF_API_KEY, null)
        } else null
    }
}


