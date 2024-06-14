package com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import com.bruno13palhano.core.model.User
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.getCurrentTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel
    @Inject
    constructor(
        @UserRep private val userRepository: UserRepository
    ) : ViewModel() {
        private var _updateState = MutableStateFlow<UiState>(UiState.Fail)
        val updateState = _updateState.asStateFlow()

        private var uid = ""
        var photo by mutableStateOf("")
            private set
        var username by mutableStateOf("")
            private set
        var email by mutableStateOf("")
            private set

        fun updatePhoto(photo: String) {
            this.photo = photo
        }

        fun updateUsername(username: String) {
            this.username = username
        }

        fun getCurrentUser() {
            viewModelScope.launch {
                userRepository.getCurrentUser(onError = {}, onSuccess = {}).collect {
                    uid = it.uid
                    photo = it.photo
                    username = it.username
                    email = it.email
                }
            }
        }

        fun updateUser(onError: (error: Int) -> Unit) {
            val user =
                User(
                    uid = uid,
                    username = username,
                    email = email,
                    password = "",
                    photo = "",
                    timestamp = getCurrentTimestamp()
                )

            viewModelScope.launch {
                _updateState.value = UiState.InProgress
                userRepository.update(
                    user = user,
                    onError = {
                        onError(it)
                        _updateState.value = UiState.Fail
                    },
                    onSuccess = {
                        _updateState.value = UiState.Success
                    }
                )
            }
        }
    }