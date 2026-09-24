package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

enum class ThemeMode(val title: String, val banglaTitle: String) {
    SYSTEM("System Default", "সিস্টেম ডিফল্ট"),
    LIGHT("Light", "লাইট মোড"),
    DARK("Dark", "ডার্ক মোড")
}

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "mega_utility_settings")

class ThemePreferences(private val context: Context) {
    companion object {
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode_key")
    }

    val themeMode: Flow<ThemeMode> = context.userDataStore.data.map { preferences ->
        val modeStr = preferences[THEME_MODE_KEY] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(modeStr)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.userDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    suspend fun clearAll() {
        context.userDataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
