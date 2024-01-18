package com.yasunov.shiftapp.repository

import com.yasunov.shiftapp.data.UserData
import com.yasunov.shiftapp.datastore.AppPreferencesDataSource
import kotlinx.coroutines.flow.Flow

class AppRepositoryImpl constructor(
    private val appPreferencesDataSource: AppPreferencesDataSource
): AppRepository {
    override fun getUserData(): Flow<UserData> = appPreferencesDataSource.userPreferencesFlow

    override suspend fun setUserData(userData: UserData) {
        appPreferencesDataSource.updateDateDailyCard(userData)
    }
}