package com.bruno13palhano.shopdanimanagement.ui.screens.login

sealed class LoginState {
    object SignedOut : LoginState()

    object InProgress : LoginState()

    object SignedIn : LoginState()
}