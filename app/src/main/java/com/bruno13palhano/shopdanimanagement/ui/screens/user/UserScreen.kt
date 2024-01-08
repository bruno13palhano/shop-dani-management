package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel.UserViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import kotlinx.coroutines.launch

@Composable
fun UserScreen(
    onLogoutClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getCurrentUser()
    }

    val updateState by viewModel.updateState.collectAsStateWithLifecycle()

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

    val menuItems = arrayOf(
        stringResource(id = R.string.logout_label),
        stringResource(id = R.string.change_password_label)
    )

    when (updateState) {
        UiState.Fail -> {
            UserContent(
                snackbarHostState = snackbarHostState,
                menuItems = menuItems,
                photo = viewModel.photo,
                username = viewModel.username,
                email = viewModel.email,
                role = viewModel.role,
                onUsernameChange = viewModel::updateUsername,
                onPhotoClick = { galleryLauncher.launch(arrayOf("image/*")) },
                onMoreOptionsItemClick = { index ->
                    when (index) {
                        0 -> {
                            onLogoutClick()
                        }

                        1 -> {
                            onChangePasswordClick()
                        }

                        else -> {}
                    }
                },
                onOutsideClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                },
                onDoneClick = {
                    viewModel.updateUser(
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = errors[it],
                                    withDismissAction = true
                                )
                            }
                        }
                    )
                },
                navigateUp = navigateUp
            )
        }

        UiState.InProgress -> { CircularProgress() }

        UiState.Success -> { LaunchedEffect(key1 = Unit) { navigateUp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserContent(
    snackbarHostState: SnackbarHostState,
    menuItems: Array<String>,
    photo: ByteArray,
    username: String,
    email: String,
    role: String,
    onUsernameChange: (username: String) -> Unit,
    onPhotoClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onOutsideClick: () -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.account_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.drawer_menu_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = menuItems,
                                    expanded = expanded,
                                    onDismissRequest = { expandedValue ->
                                        expanded = expandedValue
                                    },
                                    onClick = onMoreOptionsItemClick
                                )
                            }
                        }
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
                onClick = onPhotoClick,
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
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(id = R.string.email_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.email_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = role,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = stringResource(id = R.string.role_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.role_label),
                        fontStyle = FontStyle.Italic
                    )
                }
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