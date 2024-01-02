package com.bruno13palhano.shopdanimanagement.ui.screens.common

sealed class UserState {
    object Success: UserState()
    object InProgress: UserState()
    object Fail: UserState()
}