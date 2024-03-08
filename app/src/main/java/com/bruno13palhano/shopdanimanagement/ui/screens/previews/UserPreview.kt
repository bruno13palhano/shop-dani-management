package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.user.ChangePasswordContent
import com.bruno13palhano.shopdanimanagement.ui.screens.user.UserContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview
@Composable
fun UserDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserContent(
                snackbarHostState = SnackbarHostState(),
                menuItems = arrayOf(),
                photo = byteArrayOf(),
                username = "bruno",
                email = "test@gmail.com",
                role = "",
                onUsernameChange = {},
                onPhotoClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview
@Composable
fun UserPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            UserContent(
                snackbarHostState = SnackbarHostState(),
                menuItems = arrayOf(),
                photo = byteArrayOf(),
                username = "bruno",
                email = "test@gmail.com",
                role = "",
                onUsernameChange = {},
                onPhotoClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview
@Composable
fun ChangePasswordDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChangePasswordContent(
                snackbarHostState = SnackbarHostState(),
                newPassword = "12345678",
                repeatNewPassword = "12345678",
                onNewPasswordChange = {},
                onRepeatNewPasswordChange = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview
@Composable
fun ChangePasswordPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ChangePasswordContent(
                snackbarHostState = SnackbarHostState(),
                newPassword = "12345678",
                repeatNewPassword = "12345678",
                onNewPasswordChange = {},
                onRepeatNewPasswordChange = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}