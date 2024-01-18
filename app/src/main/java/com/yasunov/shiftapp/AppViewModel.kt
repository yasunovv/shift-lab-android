package com.yasunov.shiftapp

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.yasunov.shiftapp.data.UserData
import com.yasunov.shiftapp.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.util.Date

class AppViewModel(
    appRepository: AppRepository
) : ViewModel() {
    val userData = appRepository.getUserData().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        initialValue = UserData(
            firstName = "",
            secondName = "",
            selectedDate = Date(),
            password = ""
        )
    )

    companion object {
        fun provideFactory(
            myRepository: AppRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return AppViewModel(myRepository) as T
                }
            }
    }

}