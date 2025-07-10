package com.example.appcliente_moviles1.utils

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
val Context.dataStore by preferencesDataStore(name = "settings")

val TOKEN_KEY = stringPreferencesKey("token")

suspend fun saveToken(context: Context, token: String) {
    context.dataStore.edit { prefs ->
        prefs[TOKEN_KEY] = token
    }
}

suspend fun getToken(context: Context): String? {
    val prefs = context.dataStore.data.first()
    return prefs[TOKEN_KEY]
}

suspend fun clearToken(context: Context) {
    context.dataStore.edit { prefs ->
        prefs.remove(TOKEN_KEY)
    }
}
