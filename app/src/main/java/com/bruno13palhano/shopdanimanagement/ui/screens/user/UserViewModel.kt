package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.lifecycle.ViewModel
import com.bruno13palhano.core.data.di.UserRep
import com.bruno13palhano.core.data.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    @UserRep private val userRepository: UserRepository
): ViewModel() {

}