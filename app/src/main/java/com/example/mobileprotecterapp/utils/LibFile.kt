package com.example.mobileprotecterapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.Directory.PACKAGE_NAME
import com.example.mobileprotecterapp.BuildConfig

class LibFile(private val context: Context) {

    private val PREFS_FILE_NAME_PARAM = BuildConfig.APPLICATION_ID

    init {
        settings = context.getSharedPreferences(PREFS_FILE_NAME_PARAM, 0)

    }

    fun setStringSet(key: String, value: HashSet<String>) {
        settings.edit().putStringSet(key, value).apply()
    }

    fun getStringSet(key: String, defaultValue: HashSet<String>): HashSet<String> {
        return settings.getStringSet(key, defaultValue) as HashSet<String>
    }

    fun setString(key: String, value: String) {
        settings.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return settings.getString(key, defaultValue)
    }

    fun setInt(key: String, value: Int) {
        settings.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return settings.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return settings.getInt(key, defaultValue)
    }

    fun setLong(key: String, value: Long) {
        settings.edit().putLong(key, value).apply()
    }

    fun getLong(key: String): Long {
        return settings.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return settings.getLong(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        settings.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return settings.getBoolean(key, false)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    fun removeKey(key :String){
        settings.edit().remove(key).apply()
    }

    fun clearData() {
        settings.edit().clear().apply()
    }

    fun clearLogoutData() {
        settings.edit().remove(KEY_LOGIN_USER).apply()
        settings.edit().remove(KEY_TOKEN).apply()
    }

    companion object {

        const val KEY_TOKEN = BuildConfig.APPLICATION_ID + "_USER_TOKEN"
        const val KEY_LOGIN_USER = PACKAGE_NAME + "_LOGIN_USER"


        private var instance: LibFile? = null

        private lateinit var settings: SharedPreferences

        fun getInstance(context: Context): LibFile {
            if (instance == null) {
                instance = LibFile(context)
            }
            return instance as LibFile
        }

        fun getString(key: String): String? {
            return settings.getString(key, null)
        }
    }
}