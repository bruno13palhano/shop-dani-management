package com.bruno13palhano.shopdanimanagement.ui.screens.login

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
): ViewModel() {
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    val isLoginValid = snapshotFlow { username.isNotEmpty() && password.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = false
        )

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun login(onError: (error: Int) -> Unit, onSuccess: () -> Unit) {
        val user = User(
            id = 0L,
            username = username.trim(),
            email = "",
            password = password.trim(),
            photo = byteArrayOf(),
            role = "",
            enabled = true,
            timestamp = getCurrentTimestamp()
        )

        viewModelScope.launch {
            userRepository.login(user = user, onError = onError, onSuccess = onSuccess)
        }
    }
}