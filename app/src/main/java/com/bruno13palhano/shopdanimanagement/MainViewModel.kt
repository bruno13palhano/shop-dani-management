package com.bruno13palhano.shopdanimanagement

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.InProgress)
    val loginState = _loginState.asStateFlow()

    fun authenticated() {
        val authenticated = userRepository.isAuthenticated()
        if (authenticated) {
            _loginState.value = LoginState.SignedIn
        } else {
            _loginState.value = LoginState.SignedOut
        }
    }
}