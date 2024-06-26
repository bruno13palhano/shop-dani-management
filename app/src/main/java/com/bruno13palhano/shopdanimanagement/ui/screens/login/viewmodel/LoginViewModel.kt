package com.bruno13palhano.shopdanimanagement.ui.screens.login.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        @UserRep private val userRepository: UserRepository
    ) : ViewModel() {
        private val _loginState = MutableStateFlow<LoginState>(LoginState.SignedOut)
        val loginState = _loginState.asStateFlow()

        var email by mutableStateOf("")
            private set
        var password by mutableStateOf("")
            private set

        val isLoginValid =
            snapshotFlow { email.isNotEmpty() && password.isNotEmpty() }
                .stateIn(
                    scope = viewModelScope,
                    started = WhileSubscribed(5_000),
                    initialValue = false
                )

        fun updateEmail(username: String) {
            this.email = username
        }

        fun updatePassword(password: String) {
            this.password = password
        }

        fun login(onError: (error: Int) -> Unit) {
            val user =
                User(
                    uid = "",
                    username = "",
                    email = email.trim(),
                    password = password.trim(),
                    photo = "",
                    timestamp = getCurrentTimestamp()
                )

            viewModelScope.launch {
                _loginState.value = LoginState.InProgress
                userRepository.login(
                    user = user,
                    onError = {
                        onError(it)
                        _loginState.value = LoginState.SignedOut
                    },
                    onSuccess = {
                        _loginState.value = LoginState.SignedIn
                    }
                )
            }
        }

        fun logout() {
            viewModelScope.launch {
                userRepository.logout(
                    onError = { },
                    onSuccess = { }
                )
            }
        }

        fun isAuthenticated() = true
    }