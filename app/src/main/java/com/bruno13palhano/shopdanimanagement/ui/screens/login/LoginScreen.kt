package com.bruno13palhano.shopdanimanagement.ui.screens.login

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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.sync.Sync
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomPasswordField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.login.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(false)
    LoginScreen(
        onSuccess = onSuccess,
        onCreateAccountClick = onCreateAccountClick
    )
}

@Composable
fun LoginScreen(
    onSuccess: () -> Unit,
    onCreateAccountClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.isAuthenticated()) {
        if (viewModel.isAuthenticated()) {
            Sync.initialize(context)
        }
    }

    val loginStatus by viewModel.loginState.collectAsStateWithLifecycle()
    val isLoginValid by viewModel.isLoginValid.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getUserResponse()

    when (loginStatus) {
        LoginState.SignedOut -> {
            LaunchedEffect(key1 = Unit) {
                viewModel.logout()
            }
            showContent = true
        }
        LoginState.InProgress -> {
            showContent = false
            CircularProgress()
        }
        LoginState.SignedIn -> {
            LaunchedEffect(key1 = Unit) {
                onSuccess()
            }
        }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        LoginContent(
            snackbarHostState = snackbarHostState,
            email = viewModel.email,
            password = viewModel.password,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onOutsideClick = {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            },
            onCreateAccountClick = onCreateAccountClick,
            onLogin = {
                if (isLoginValid) {
                    viewModel.login(
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = errors[it],
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    snackbarHostState: SnackbarHostState,
    email: String,
    password: String,
    onEmailChange: (email: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onOutsideClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onLogin: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
        ) {
            CustomTextField(
                text = email,
                onTextChange = onEmailChange,
                icon = Icons.Filled.Title,
                label = stringResource(id = R.string.email_label),
                placeholder = stringResource(id = R.string.enter_email_label)
            )
            CustomPasswordField(
                password = password,
                onPasswordChange = onPasswordChange,
                icon = Icons.Filled.Key,
                label = stringResource(id = R.string.password_label),
                placeholder = stringResource(id = R.string.enter_password_label)
            )
            TextButton(
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp),
                onClick = onCreateAccountClick
            ) {
                Text(text = stringResource(id = R.string.create_account_label))
            }
        }
    }
}