package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel.ChangePasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    ChangePasswordScreen(navigateUp = navigateUp)
}

@Composable
fun ChangePasswordScreen(
    navigateUp: () -> Unit,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getCurrentUser()
    }

    val updateState by viewModel.updateState.collectAsStateWithLifecycle()
    val isFieldsNotEmpty by viewModel.isFieldsNotEmpty.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showRepeatNewPassword by remember { mutableStateOf(false) }

    val focusManger = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getUserResponse()

    when (updateState) {
        UiState.Fail -> { showContent = true }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> { LaunchedEffect(key1 = Unit) { navigateUp() } }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        ChangePasswordContent(
            snackbarHostState = snackbarHostState,
            newPassword = viewModel.newPassword,
            repeatNewPassword = viewModel.repeatNewPassword,
            showNewPassword = showNewPassword,
            showRepeatNewPassword = showRepeatNewPassword,
            onNewPasswordChange = viewModel::updateNewPassword,
            onRepeatNewPasswordChange = viewModel::updateRepeatNewPassword,
            onShowNewPasswordChange = { showNewPassword = it },
            onShowRepeatNewPasswordChange = { showRepeatNewPassword = it },
            onOutsideClick = {
                keyboardController?.hide()
                focusManger.clearFocus(force = true)
            },
            onDoneClick = {
                if (isFieldsNotEmpty) {
                    viewModel.changePassword(
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = errors[it],
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errors[UserResponse.FillMissingFields.code],
                            withDismissAction = true
                        )
                    }
                }
            },
            navigateUp = navigateUp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordContent(
    snackbarHostState: SnackbarHostState,
    newPassword: String,
    repeatNewPassword: String,
    showNewPassword: Boolean,
    showRepeatNewPassword: Boolean,
    onNewPasswordChange: (newPassword: String) -> Unit,
    onRepeatNewPasswordChange: (repeatNewPassword: String) -> Unit,
    onShowNewPasswordChange: (show: Boolean) -> Unit,
    onShowRepeatNewPasswordChange: (show: Boolean) -> Unit,
    onOutsideClick: () -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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