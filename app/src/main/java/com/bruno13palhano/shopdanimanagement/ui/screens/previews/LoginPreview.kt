package com.bruno13palhano.shopdanimanagement.ui.screens.previews

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.ui.screens.login.CreateAccountContent
import com.bruno13palhano.shopdanimanagement.ui.screens.login.LoginContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview
@Composable
fun LoginPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginContent(
                snackbarHostState = SnackbarHostState(),
                email = "bruno13palhano",
                password = "12345678",
                onEmailChange = {},
                onPasswordChange = {},
                onOutsideClick = {},
                onCreateAccountClick = {},
                onLogin = {}
            )
        }
    }
}

@Preview
@Composable
fun LoginDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginContent(
                snackbarHostState = SnackbarHostState(),
                email = "bruno13palhano",
                password = "12345678",
                onEmailChange = {},
                onPasswordChange = {},
                onOutsideClick = {},
                onCreateAccountClick = {},
                onLogin = {}
            )
        }
    }
}

@Preview
@Composable
fun CreateAccountPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CreateAccountContent(
                snackbarHostState = SnackbarHostState(),
                username = "test",
                email = "test@email.com",
                password = "12345678",
                repeatPassword = "12345678",
                photo = "",
                onUsernameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onRepeatPasswordChange = {},
                onImageClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview
@Composable
fun CreateAccountDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            CreateAccountContent(
                snackbarHostState = SnackbarHostState(),
                username = "test",
                email = "test@email.com",
                password = "12345678",
                repeatPassword = "12345678",
                photo = "",
                onUsernameChange = {},
                onEmailChange = {},
                onPasswordChange = {},
                onRepeatPasswordChange = {},
                onImageClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}