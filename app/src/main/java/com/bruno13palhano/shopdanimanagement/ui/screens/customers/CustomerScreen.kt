package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerViewModel
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    CustomerContent(
        screenTitle = screenTitle,
        snackbarHostState = snackbarHostState,
        name = viewModel.name,
        photo = viewModel.photo,
        email = viewModel.email,
        address = viewModel.address,
        phoneNumber = viewModel.phoneNumber,
        onNameChange = viewModel::updateName,
        onEmailChange = viewModel::updateEmail,
        onAddressChange = viewModel::updateAddress,
        onPhoneNumberChange = viewModel::updatePhoneNumber,
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
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = errors[it],
                                    withDismissAction = true
                                )

                                navigateUp()
                            }
                        }
                    ) {
                        scope.launch { navigateUp() }
                    }
                } else {
                    viewModel.insertCustomer(
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = errors[it],
                                    withDismissAction = true
                                )

                                navigateUp()
                            }
                        }
                    ) {
                        scope.launch { navigateUp() }
                    }
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