package com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(

) : ViewModel() {
    var newPassword by mutableStateOf("")
        private set
    var repeatNewPassword by mutableStateOf("")
        private set

    fun updateNewPassword(newPassword: String) {
        this.newPassword = newPassword
    }

    fun updateRepeatNewPassword(repeatNewPassword: String) {
        this.repeatNewPassword = repeatNewPassword
    }
}