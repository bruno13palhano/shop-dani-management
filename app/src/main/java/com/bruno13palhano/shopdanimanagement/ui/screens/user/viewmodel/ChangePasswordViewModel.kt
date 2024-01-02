package com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
) : ViewModel() {
    private var userId = 0L
    private var username = ""
    private var email = ""
    private var photo = byteArrayOf()
    private var role = ""
    private var enabled = false
    private var timestamp = ""

    var newPassword by mutableStateOf("")
        private set
    var repeatNewPassword by mutableStateOf("")
        private set

    val isFieldsNotEmpty = snapshotFlow {
        newPassword.isNotEmpty() && repeatNewPassword.isNotEmpty()
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateNewPassword(newPassword: String) {
        this.newPassword = newPassword
    }

    fun updateRepeatNewPassword(repeatNewPassword: String) {
        this.repeatNewPassword = repeatNewPassword
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser(onError = {}, onSuccess = {}).collect {
                userId = it.id
                username = it.username
                email = it.email
                photo = it.photo
                role = it.role
                enabled = it.enabled
                timestamp = getCurrentTimestamp()
            }
        }
    }

    fun changePassword(onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        if(newPassword == repeatNewPassword) {
            val user = User(
                id = userId,
                username = username,
                email = email,
                password = newPassword,
                photo = photo,
                role = role,
                enabled = enabled,
                timestamp = timestamp
            )

            viewModelScope.launch {
                userRepository.updateUserPassword(
                    user = user,
                    onError = onError,
                    onSuccess = onSuccess
                )
            }
        } else {
            onError(DataError.WrongPasswordValidation.error)
        }
    }
}