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
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UserState
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
) : ViewModel() {
    private var _updateState = MutableStateFlow<UserState>(UserState.Fail)
    val updateState = _updateState.asStateFlow()

    private var userId = 0L
    private var username = ""
    private var email = ""
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
                timestamp = getCurrentTimestamp()
            }
        }
    }

    fun changePassword(onError: (error: Int) -> Unit) {
        if(newPassword == repeatNewPassword) {
            _updateState.value = UserState.InProgress

            val user = User(
                id = userId,
                username = username,
                email = email,
                password = newPassword,
                photo = byteArrayOf(),
                role = "",
                enabled = true,
                timestamp = timestamp
            )

            viewModelScope.launch {
                userRepository.updateUserPassword(
                    user = user,
                    onError = {
                        onError(it)
                        _updateState.value = UserState.Fail
                    },
                    onSuccess = {
                        _updateState.value = UserState.Success
                    }
                )
            }
        } else {
            onError(DataError.WrongPasswordValidation.error)
            _updateState.value = UserState.Fail
        }
    }
}