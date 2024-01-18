package com.yasunov.shiftapp.registration

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.yasunov.shiftapp.data.UserData
import com.yasunov.shiftapp.data.UserDataRegistration
import com.yasunov.shiftapp.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class RegistrationViewModel(
    private val repository: AppRepository
) : ViewModel() {
    private var _uiState: MutableStateFlow<UserDataRegistration> =
        MutableStateFlow(UserDataRegistration())
    val uiState: StateFlow<UserDataRegistration> = _uiState.asStateFlow()

    fun updateFirstName(name: String) {
        _uiState.update {
            it.copy(
                firstName = name
            )
        }
    }

    fun checkNameLength(str: String): Boolean {
        return str.length >= 2
    }

    fun updateSecondName(str: String) {
        _uiState.update {
            it.copy(
                secondName = str
            )
        }
    }

    fun updateFirstPassword(str: String) {
        _uiState.update {
            it.copy(
                firstPassword = str
            )
        }
    }

    fun checkPasswordLength(): Boolean {
        return uiState.value.firstPassword.length >= 6
    }

    fun checkPasswordsEquality(): Boolean {
        return _uiState.value.firstPassword == uiState.value.secondPassword
    }

    fun updateSecondPassword(str: String) {
        _uiState.update {
            it.copy(
                secondPassword = str
            )
        }
    }

    fun updateTime(time: Date) {
        _uiState.update {
            it.copy(
                selectedDate = time
            )
        }
    }

    fun checkAge(): Boolean {
        return uiState.value.selectedDate.time < (Date().time - 18 * 365 * 24 * 60 * 60 * 1000L)
    }

    fun checkSubmit(): Boolean {
        return checkAge() && checkNameLength(uiState.value.firstName) &&
                checkNameLength(uiState.value.secondPassword) && checkPasswordLength() &&
                checkPasswordConsistDigit() && checkPasswordConsistUpperCase() &&
                checkPasswordsEquality() && checkPasswordNotConsistWhiteSpace()
    }

    fun checkPasswordConsistDigit(): Boolean {
        return uiState.value.firstPassword.any { it.isDigit() }
    }

    fun checkPasswordNotConsistWhiteSpace(): Boolean {
        return !uiState.value.firstPassword.any { it.isWhitespace() }
    }

    fun checkPasswordConsistUpperCase(): Boolean {
        return uiState.value.firstPassword.any { it.isUpperCase() }
    }
    fun checkNameSymb(str: String): Boolean {
        return !str.any { it.isDigit() }
    }
    fun signUpUser() {
        if (checkSubmit()) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.setUserData(
                    UserData(
                        firstName = uiState.value.firstName,
                        secondName = uiState.value.secondName,
                        selectedDate = uiState.value.selectedDate,
                        password = uiState.value.firstPassword
                    )
                )
            }
        }
    }



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
                    return RegistrationViewModel(myRepository) as T
                }
            }
    }
}
