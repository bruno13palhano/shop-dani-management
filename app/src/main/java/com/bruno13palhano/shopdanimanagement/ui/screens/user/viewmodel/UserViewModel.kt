package com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
): ViewModel() {
    private var userId = 0L
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

    fun getCurrentUser() {
        viewModelScope.launch {
            userRepository.getCurrentUser(onError = {}, onSuccess = {}).collect {
                userId = it.id
                photo = it.photo
                username = it.username
                email = it.email
                role = it.role
            }
        }
    }

    fun updateUser() {

    }
}