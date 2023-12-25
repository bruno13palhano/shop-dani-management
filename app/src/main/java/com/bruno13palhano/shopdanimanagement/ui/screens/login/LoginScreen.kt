package com.bruno13palhano.shopdanimanagement.ui.screens.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val isLoginValid by viewModel.isLoginValid.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LoginContent(
        username = viewModel.username,
        password = viewModel.password,
        showPassword = showPassword,
        onUsernameChange = viewModel::updateUsername,
        onPasswordChange = viewModel::updatePassword,
        onShowPasswordChange = { showPassword = it },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onLogin = {
            if (isLoginValid) {
                viewModel.login(
                    onError = {},
                    onSuccess = onSuccess
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    username: String,
    password: String,
    showPassword: Boolean,
    onUsernameChange: (username: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onShowPasswordChange: (showPassword: Boolean) -> Unit,
    onOutsideClick: () -> Unit,
    onLogin: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = {},
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.login_label)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onLogin) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = username,
                onValueChange = onUsernameChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Title,
                        contentDescription = stringResource(id = R.string.username_label)
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.username_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_username_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = password,
                onValueChange = onPasswordChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = stringResource(id = R.string.password_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                trailingIcon = {
                    if (showPassword) {
                        IconButton(onClick = { onShowPasswordChange(false) }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.password_label)
                            )
                        }
                    } else {
                        IconButton(onClick = { onShowPasswordChange(true) }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.password_label)
                            )
                        }
                    }
                },
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.password_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_password_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
        }
    }
}

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
                username = "bruno13palhano",
                password = "12345678",
                showPassword = true,
                onUsernameChange = {},
                onPasswordChange = {},
                onShowPasswordChange = {},
                onOutsideClick = {},
                onLogin = {}
            )
        }
    }
}