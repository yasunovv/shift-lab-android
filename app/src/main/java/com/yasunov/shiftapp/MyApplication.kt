package com.yasunov.shiftapp

import android.app.Application
import com.yasunov.shiftapp.datastore.AppPreferencesDataSource
import com.yasunov.shiftapp.repository.AppRepository
import com.yasunov.shiftapp.repository.AppRepositoryImpl

class MyApplication : Application() {
    lateinit var  appRepository: AppRepository
    override fun onCreate() {
        super.onCreate()
        appRepository = AppRepositoryImpl(appPreferencesDataSource = AppPreferencesDataSource(this.applicationContext))
    }
}
