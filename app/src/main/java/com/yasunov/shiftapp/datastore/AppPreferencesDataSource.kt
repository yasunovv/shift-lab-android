package com.yasunov.shiftapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yasunov.shiftapp.data.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Date

class AppPreferencesDataSource (
    context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
    private val dataStore = context.dataStore
    val userPreferencesFlow: Flow<UserData> = dataStore.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }

        }
        .map { preferences ->
            val firstName = preferences[PreferencesKeys.FIRST_NAME]?: ""
            val secondName = preferences[PreferencesKeys.SECOND_NAME] ?: ""
            val birthday = preferences[PreferencesKeys.BIRTHDAY] ?: -1L
            val password = preferences[PreferencesKeys.PASSWORD] ?: ""
            UserData(firstName, secondName, Date(birthday), password)

        }
    suspend fun updateDateDailyCard(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_NAME] = userData.firstName
            preferences[PreferencesKeys.SECOND_NAME] = userData.secondName
        }
    }

    private object PreferencesKeys {
        val FIRST_NAME = stringPreferencesKey("first_name")
        val SECOND_NAME = stringPreferencesKey("second_name")
        val BIRTHDAY = longPreferencesKey("selected_date")
        val PASSWORD = stringPreferencesKey("password")

    }
}