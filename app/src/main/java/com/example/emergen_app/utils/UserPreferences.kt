package com.example.emergencyapp.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// إنشاء DataStore
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val LAST_SCREEN_KEY = stringPreferencesKey("last_screen")
    }

    // استرجاع آخر شاشة قام المستخدم بالدخول إليها
    val lastScreen: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LAST_SCREEN_KEY]
    }

    // حفظ آخر شاشة
    suspend fun saveLastScreen(screen: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SCREEN_KEY] = screen
        }
    }
}
