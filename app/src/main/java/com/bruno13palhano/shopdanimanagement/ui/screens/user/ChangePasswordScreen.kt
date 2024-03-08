package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomPasswordField
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
            onNewPasswordChange = viewModel::updateNewPassword,
            onRepeatNewPasswordChange = viewModel::updateRepeatNewPassword,
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
    onNewPasswordChange: (newPassword: String) -> Unit,
    onRepeatNewPasswordChange: (repeatNewPassword: String) -> Unit,
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
            CustomPasswordField(
                password = newPassword,
                onPasswordChange = onNewPasswordChange,
                icon = Icons.Filled.Key,
                label = stringResource(id = R.string.new_password_label),
                placeholder = stringResource(id = R.string.enter_new_password_label)
            )
            CustomPasswordField(
                password = repeatNewPassword,
                onPasswordChange = onRepeatNewPasswordChange,
                icon = Icons.Filled.Key,
                label = stringResource(id = R.string.repeat_new_password_label),
                placeholder = stringResource(id = R.string.enter_repeat_new_password_label)
            )
        }
    }
}