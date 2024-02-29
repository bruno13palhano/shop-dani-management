package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import kotlinx.coroutines.launch

@Composable
fun NewCustomerRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CustomerScreen(
        screenTitle = stringResource(id = R.string.new_customer_label),
        isEditable = false,
        customerId = 0L,
        navigateUp = navigateUp
    )
}

@Composable
fun EditCustomerRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    customerId: Long,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    CustomerScreen(
        screenTitle = stringResource(id = R.string.edit_customer_label),
        isEditable = true,
        customerId = customerId,
        navigateUp = navigateUp
    )
}

@Composable
fun CustomerScreen(
    screenTitle: String,
    isEditable: Boolean,
    customerId: Long,
    navigateUp: () -> Unit,
    viewModel: CustomerViewModel = hiltViewModel()
) {
    if (isEditable) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getCustomer(customerId)
        }
    }

    val context = LocalContext.current
    val customerState by viewModel.customerState.collectAsStateWithLifecycle()
    val isCustomerNotEmpty by viewModel.isCustomerNotEmpty.collectAsStateWithLifecycle()
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                getBytes(context, it)?.let { imageByteArray ->
                    viewModel.updatePhoto(photo = imageByteArray)
                }
            }
        }
    val focusManger = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showContent by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    when (customerState) {
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
        CustomerContent(
            screenTitle = screenTitle,
            snackbarHostState = snackbarHostState,
            name = viewModel.name,
            photo = viewModel.photo,
            email = viewModel.email,
            address = viewModel.address,
            city = viewModel.city,
            phoneNumber = viewModel.phoneNumber,
            gender = viewModel.gender,
            age = viewModel.age,
            onNameChange = viewModel::updateName,
            onEmailChange = viewModel::updateEmail,
            onAddressChange = viewModel::updateAddress,
            onCityChange = viewModel::updateCity,
            onPhoneNumberChange = viewModel::updatePhoneNumber,
            onGenderChange = viewModel::updateGender,
            onAgeChange = viewModel::updateAge,
            onPhotoClick = {
                galleryLauncher.launch(arrayOf("image/*"))
            },
            onOutsideClick = {
                keyboardController?.hide()
                focusManger.clearFocus(force = true)
            },
            onDoneButtonClick = {
                if (isCustomerNotEmpty) {
                    if (isEditable) {
                        viewModel.updateCustomer(
                            id = customerId,
                            onError = { error ->
                                scope.launch {
                                    if (error == DataError.UpdateDatabase.error) {
                                        snackbarHostState.showSnackbar(
                                            message = errors[error],
                                            withDismissAction = true
                                        )
                                    }

                                    navigateUp()
                                }
                            }
                        )
                    } else {
                        viewModel.insertCustomer(
                            onError = { error ->
                                scope.launch {
                                    if (error == DataError.InsertDatabase.error) {
                                        snackbarHostState.showSnackbar(
                                            message = errors[error],
                                            withDismissAction = true
                                        )
                                    }

                                    navigateUp()
                                }
                            }
                        )
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errors[DataError.FillMissingFields.error],
                            withDismissAction = true
                        )
                    }
                }
            },
            navigateUp = navigateUp
        )
    }
}