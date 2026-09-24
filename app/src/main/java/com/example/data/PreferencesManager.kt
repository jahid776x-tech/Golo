package com.example.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mega_utility_prefs", Context.MODE_PRIVATE)

    fun getFavoriteIds(): Set<Int> {
        val stringSet = prefs.getStringSet("favorites", null) ?: setOf("1", "6", "11", "13", "20", "40", "41", "54", "62", "67")
        return stringSet.mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun toggleFavorite(id: Int): Boolean {
        val current = getFavoriteIds().toMutableSet()
        val isNowFav = if (current.contains(id)) {
            current.remove(id)
            false
        } else {
            current.add(id)
            true
        }
        prefs.edit().putStringSet("favorites", current.map { it.toString() }.toSet()).apply()
        return isNowFav
    }

    fun isFavorite(id: Int): Boolean {
        return getFavoriteIds().contains(id)
    }

    // Tasbih counter
    fun getTasbihCount(): Int = prefs.getInt("tasbih_count", 0)
    fun setTasbihCount(count: Int) = prefs.edit().putInt("tasbih_count", count).apply()

    // Water intake (ml)
    fun getWaterIntake(): Int = prefs.getInt("water_intake", 750)
    fun setWaterIntake(ml: Int) = prefs.edit().putInt("water_intake", ml).apply()

    // Dark theme override (null = system default, true = dark, false = light)
    fun getThemeMode(): String = prefs.getString("theme_mode", "SYSTEM") ?: "SYSTEM"
    fun setThemeMode(mode: String) = prefs.edit().putString("theme_mode", mode).apply()

    fun clearAll() {
        prefs.edit().clear().putStringSet("favorites", emptySet()).apply()
    }
}
