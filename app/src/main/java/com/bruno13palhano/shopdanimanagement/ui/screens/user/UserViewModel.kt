package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
): ViewModel() {
    var photo by mutableStateOf(byteArrayOf())
        private set
    var username by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var role by mutableStateOf("")
        private set

    fun updatePhoto(photo: ByteArray) {
        this.photo = photo
    }

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateRole(role: String) {
        this.role = role
    }

    fun updateUser() {

    }
}