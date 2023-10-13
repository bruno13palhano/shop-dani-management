package com.bruno13palhano.shopdanimanagement.ui.screens.products

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ProductContent
import com.bruno13palhano.shopdanimanagement.ui.components.ProductMenuItem
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.getBytes
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    isEditable: Boolean,
    screenTitle: String,
    productId: Long,
    categoryId: Long,
    onAddToCatalogClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllCategories()
        viewModel.updateDate(date = currentDate)
    }

    if (isEditable) {
        LaunchedEffect(key1 = Unit) {
            viewModel.getProduct(productId)
        }
    } else {
        viewModel.setCategoryChecked(categoryId)
    }

    val context = LocalContext.current
    val isProductValid by viewModel.isProductValid.collectAsStateWithLifecycle()

    val takeFlags: Int = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    val contentResolver = context.contentResolver
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                contentResolver.takePersistableUriPermission(uri, takeFlags)
                getBytes(context, it)?.let { imageByteArray ->
                    viewModel.updatePhoto(photo = imageByteArray)
                }
            }
        }
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var datePickerState = rememberDatePickerState()
    var showDatePickerDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.updateDate(it)
                    }
                    showDatePickerDialog = false
                    focusManager.clearFocus(force = true)
                }) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            }
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.date,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    ProductContent(
        isEditable = isEditable,
        screenTitle = screenTitle,
        snackbarHostState = snackbarHostState,
        categories = viewModel.allCategories,
        companies = viewModel.allCompanies,
        name = viewModel.name,
        code = viewModel.code,
        description = viewModel.description,
        photo = viewModel.photo,
        date = dateFormat.format(viewModel.date),
        category = viewModel.category,
        company = viewModel.company,
        onNameChange = viewModel::updateName,
        onCodeChange = viewModel::updateCode,
        onDescriptionChange = viewModel::updateDescription,
        onDismissCategory = {
            viewModel.updateCategories(viewModel.allCategories)
            focusManager.clearFocus(force = true)
        },
        onCompanySelected = { viewModel.updateCompany(it) },
        onDismissCompany = { focusManager.clearFocus(force = true) },
        onImageClick = { galleryLauncher.launch(arrayOf("image/*")) },
        onDateClick = { showDatePickerDialog = true},
        onMoreOptionsItemClick = { index ->
            when (index) {
                ProductMenuItem.addToCatalog -> {
                    onAddToCatalogClick(productId)
                }
                ProductMenuItem.delete -> {
                    navigateUp()
                    viewModel.deleteProduct(productId)
                }
            }
        },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onActionButtonClick = {
            if (isProductValid) {
                if (isEditable) {
                    viewModel.updateProduct(productId)
                } else {
                    viewModel.insertProduct()
                }
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