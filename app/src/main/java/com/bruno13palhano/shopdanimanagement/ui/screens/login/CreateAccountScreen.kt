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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import com.bruno13palhano.shopdanimanagement.ui.screens.login.viewmodel.CreateAccountViewModel
import kotlinx.coroutines.launch

@Composable
fun CreateAccountScreen(
    onSuccess: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: CreateAccountViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val isValid by viewModel.isFieldsNotEmpty.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
    var showPassword by remember { mutableStateOf(false) }
    var showRepeatPassword by remember { mutableStateOf(false) }
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
        LoginState.SignedOut -> { showContent = true }

        LoginState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        LoginState.SignedIn -> { LaunchedEffect(key1 = Unit) { onSuccess() } }
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
            showPassword = showPassword,
            showRepeatPassword = showRepeatPassword,
            onUsernameChange = viewModel::updateUsername,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onRepeatPasswordChange = viewModel::updateRepeatPassword,
            onShowPasswordChange = { showPassword = it },
            onShowRepeatPasswordChange = { showRepeatPassword = it },
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
    showPassword: Boolean,
    showRepeatPassword: Boolean,
    onUsernameChange: (username: String) -> Unit,
    onEmailChange: (email: String) -> Unit,
    onPasswordChange: (password: String) -> Unit,
    onRepeatPasswordChange: (password: String) -> Unit,
    onShowPasswordChange: (show: Boolean) -> Unit,
    onShowRepeatPasswordChange: (show: Boolean) -> Unit,
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
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp),
                onClick = onImageClick,
                shape = RoundedCornerShape(5)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(5)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.customer_photo_label),
                        contentScale = ContentScale.Crop
                    )
                }
            }
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
                value = email,
                onValueChange = onEmailChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(id = R.string.email_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.email_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_email_label),
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
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = repeatPassword,
                onValueChange = onRepeatPasswordChange,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = stringResource(id = R.string.repeat_password_label)
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
                    if (showRepeatPassword) {
                        IconButton(onClick = { onShowRepeatPasswordChange(false) }) {
                            Icon(
                                imageVector = Icons.Filled.Visibility,
                                contentDescription = stringResource(id = R.string.repeat_password_label)
                            )
                        }
                    } else {
                        IconButton(onClick = { onShowRepeatPasswordChange(true) }) {
                            Icon(
                                imageVector = Icons.Filled.VisibilityOff,
                                contentDescription = stringResource(id = R.string.repeat_password_label)
                            )
                        }
                    }
                },
                visualTransformation = if (showRepeatPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.repeat_password_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_repeat_password_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 8.dp),
                onClick = navigateUp
            ) {
                Text(text = stringResource(id = R.string.already_have_account_label))
            }
        }
    }
}