package com.bruno13palhano.shopdanimanagement.ui.screens.customers

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CustomerContent
import com.bruno13palhano.shopdanimanagement.ui.screens.customers.viewmodel.CustomerViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewCustomerScreen(
    navigateUp: () -> Unit,
    viewModel: CustomerViewModel = hiltViewModel()
) {
    val isCustomerNotEmpty by viewModel.isCustomerNotEmpty.collectAsStateWithLifecycle()
    val contentResolver = LocalContext.current.contentResolver
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            viewModel.updatePhoto(uri.toString())
        }
    val focusManger = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    CustomerContent(
        screenTitle = "New Customer",
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
                viewModel.insertCustomer()
                navigateUp()
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                }
            }
        },
        navigateUp = navigateUp
    )
}