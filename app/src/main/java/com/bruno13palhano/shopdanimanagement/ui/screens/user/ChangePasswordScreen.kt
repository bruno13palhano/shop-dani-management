package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Key
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel.ChangePasswordViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ChangePasswordScreen(
    navigateUp: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    var showNewPassword by remember { mutableStateOf(false) }
    var showRepeatNewPassword by remember { mutableStateOf(false) }

    ChangePasswordContent(
        newPassword = viewModel.newPassword,
        repeatNewPassword = viewModel.repeatNewPassword,
        showNewPassword = showNewPassword,
        showRepeatNewPassword = showRepeatNewPassword,
        onNewPasswordChange = viewModel::updateNewPassword,
        onRepeatNewPasswordChange = viewModel::updateRepeatNewPassword,
        onShowNewPasswordChange = { showNewPassword = it},
        onShowRepeatNewPasswordChange = { showRepeatNewPassword = it },
        onDoneClick = {},
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(
    newPassword: String,
    repeatNewPassword: String,
    showNewPassword: Boolean,
    showRepeatNewPassword: Boolean,
    onNewPasswordChange: (newPassword: String) -> Unit,
    onRepeatNewPasswordChange: (repeatNewPassword: String) -> Unit,
    onShowNewPasswordChange: (show: Boolean) -> Unit,
    onShowRepeatNewPasswordChange: (show: Boolean) -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.change_password_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = newPassword,
                onValueChange = onNewPasswordChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = stringResource(id = R.string.new_password_label)
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
                    if (showRepeatNewPassword) {
                        IconButton(onClick = { onShowNewPasswordChange(false) }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.new_password_label)
                            )
                        }
                    } else {
                        IconButton(onClick = { onShowNewPasswordChange(true) }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.new_password_label)
                            )
                        }
                    }
                },
                visualTransformation = if (showNewPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.new_password_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_new_password_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = repeatNewPassword,
                onValueChange = onRepeatNewPasswordChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = stringResource(id = R.string.repeat_new_password_label)
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
                    if (showRepeatNewPassword) {
                        IconButton(onClick = { onShowRepeatNewPasswordChange(false) }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.repeat_new_password_label)
                            )
                        }
                    } else {
                        IconButton(onClick = { onShowRepeatNewPasswordChange(true) }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.repeat_new_password_label)
                            )
                        }
                    }
                },
                visualTransformation = if (showRepeatNewPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.repeat_new_password_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_repeat_new_password_label),
                        fontStyle = FontStyle.Italic
                    )
                }
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
                newPassword = "12345678",
                repeatNewPassword = "12345678",
                showNewPassword = true,
                showRepeatNewPassword = false,
                onNewPasswordChange = {},
                onRepeatNewPasswordChange = {},
                onShowNewPasswordChange = {},
                onShowRepeatNewPasswordChange = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}