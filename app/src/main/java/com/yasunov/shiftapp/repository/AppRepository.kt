package com.yasunov.shiftapp.repository

import com.yasunov.shiftapp.data.UserData
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getUserData(): Flow<UserData>
    suspend fun setUserData(userData: UserData)
}