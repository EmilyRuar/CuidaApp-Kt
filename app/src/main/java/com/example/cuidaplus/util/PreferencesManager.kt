package com.example.cuidaplus.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_LASTNAME = stringPreferencesKey("user_lastname")
    }

    suspend fun saveLoginState(isLoggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = isLoggedIn
        }
    }

    suspend fun saveUserName(nombre: String, apellido: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = nombre
            prefs[USER_LASTNAME] = apellido
        }
    }

    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_LOGGED_IN] ?: false }

    val getUserNameFlow: Flow<Pair<String, String>> = context.dataStore.data
        .map { prefs ->
            val nombre = prefs[USER_NAME] ?: ""
            val apellido = prefs[USER_LASTNAME] ?: ""
            Pair(nombre, apellido)
        }

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
        }
    }
}
