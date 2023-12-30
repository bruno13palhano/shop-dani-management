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
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
) : ViewModel() {
    var photo by mutableStateOf(byteArrayOf())
        private set
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var repeatPassword by mutableStateOf("")
        private set

    val isFieldsNotEmpty = snapshotFlow {
        username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
                && repeatPassword.isNotEmpty()
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = false
    )

    fun updatePhoto(photo: ByteArray) {
        this.photo = photo
    }

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateRepeatPassword(repeatPassword: String) {
        this.repeatPassword = repeatPassword
    }

    fun createAccount(
        onError: (error: Int) -> Unit,
        onSuccess: () -> Unit
    ) {
        if (password == repeatPassword) {
            val user = User(
                id = 0L,
                username = username,
                email = email,
                password = password,
                photo = photo,
                role = "",
                enabled = true,
                timestamp = getCurrentTimestamp()
            )
            viewModelScope.launch {
                userRepository.create(
                    user = user,
                    onError =  {
                        onError(it)
                    },
                ) {
                    onSuccess()
                }
            }
        } else {
            onError(DataError.WrongPasswordValidation.error)
        }
    }
}