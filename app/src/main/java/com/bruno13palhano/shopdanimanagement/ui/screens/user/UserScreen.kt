package com.bruno13palhano.shopdanimanagement.ui.screens.user

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getUserResponse
import com.bruno13palhano.shopdanimanagement.ui.screens.user.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    onLogoutClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(false)
    gesturesEnabled(true)
    UserScreen(
        onLogoutClick = onLogoutClick,
        onChangePasswordClick = onChangePasswordClick,
        navigateUp = navigateUp
    )
}

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

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                viewModel.updatePhoto(photo = it.toString())
            }
        }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showContent by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getUserResponse()

    val menuItems =
        arrayOf(
            stringResource(id = R.string.logout_label),
            stringResource(id = R.string.change_password_label)
        )

    when (updateState) {
        UiState.Fail -> {
            showContent = true
        }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> {
            LaunchedEffect(key1 = Unit) { navigateUp() }
        }
    }

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        UserContent(
            snackbarHostState = snackbarHostState,
            menuItems = menuItems,
            photo = viewModel.photo,
            username = viewModel.username,
            email = viewModel.email,
            onUsernameChange = viewModel::updateUsername,
            onPhotoClick = { galleryLauncher.launch(arrayOf("image/*")) },
            onMoreOptionsItemClick = { index ->
                when (index) {
                    MoreOptions.LOGOUT -> {
                        onLogoutClick()
                    }
                    MoreOptions.CHANGE_PASSWORD -> {
                        onChangePasswordClick()
                    }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserContent(
    snackbarHostState: SnackbarHostState,
    menuItems: Array<String>,
    photo: String,
    username: String,
    email: String,
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                onClick = onPhotoClick,
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
                onTextChange = {},
                icon = Icons.Filled.Email,
                label = stringResource(id = R.string.email_label),
                placeholder = ""
            )
        }
    }
}

private object MoreOptions {
    const val LOGOUT = 0
    const val CHANGE_PASSWORD = 1
}