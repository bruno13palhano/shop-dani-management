package com.bruno13palhano.shopdanimanagement.ui.screens.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomPasswordField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import com.bruno13palhano.shopdanimanagement.ui.screens.login.viewmodel.CreateAccountViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateAccountRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onSuccess: () -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(false)
    CreateAccountScreen(
        onSuccess = onSuccess,
        navigateUp = navigateUp
    )
}

@Composable
fun CreateAccountScreen(
    onSuccess: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val isValid by viewModel.isFieldsNotEmpty.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                getBytes(context, it)?.let { imageByteArray ->
                    viewModel.updatePhoto(photo = imageByteArray)
                }
            }
        }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getUserResponse()

    when (loginState) {
        LoginState.SignedOut -> {
            showContent = true
        }

        LoginState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        LoginState.SignedIn -> {
            LaunchedEffect(key1 = Unit) { onSuccess() }
        }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        CreateAccountContent(
            snackbarHostState = snackbarHostState,
            username = viewModel.username,
            email = viewModel.email,
            password = viewModel.password,
            repeatPassword = viewModel.repeatPassword,
            photo = viewModel.photo,
            onUsernameChange = viewModel::updateUsername,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onRepeatPasswordChange = viewModel::updateRepeatPassword,
            onImageClick = { galleryLauncher.launch(arrayOf("image/*")) },
            onOutsideClick = {
                keyboardController?.hide()
                focusManager.clearFocus(force = true)
            },
            onDoneClick = {
                if (isValid) {
                    viewModel.createAccount(
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
                            message = errors[UserResponse.FillMissingFields.code]
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
fun CreateAccountContent(
    snackbarHostState: SnackbarHostState,
    username: String,
    email: String,
    password: String,
    repeatPassword: String,
    photo: ByteArray,
    onUsernameChange: (username: String) -> Unit,
    onEmailChange: (email: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onRepeatPasswordChange: (password: String) -> Unit,
    onImageClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.create_account_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        Column(
            modifier =
                Modifier
                    .padding(it)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier =
                    Modifier
                        .padding(16.dp),
                onClick = onImageClick,
                shape = RoundedCornerShape(5)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier =
                            Modifier
                                .size(200.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier =
                            Modifier
                                .size(200.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            CustomTextField(
                text = username,
                onTextChange = onUsernameChange,
                icon = Icons.Filled.Title,
                label = stringResource(id = R.string.username_label),
                placeholder = stringResource(id = R.string.enter_username_label)
            )
            CustomTextField(
                text = email,
                onTextChange = onEmailChange,
                icon = Icons.Filled.Email,
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
            CustomPasswordField(
                password = repeatPassword,
                onPasswordChange = onRepeatPasswordChange,
                icon = Icons.Filled.Key,
                label = stringResource(id = R.string.repeat_password_label),
                placeholder = stringResource(id = R.string.enter_repeat_password_label)
            )
            TextButton(
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp),
                onClick = navigateUp
            ) {
                Text(text = stringResource(id = R.string.already_have_account_label))
            }
        }
    }
}