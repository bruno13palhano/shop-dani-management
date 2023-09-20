package com.bruno13palhano.shopdanimanagement.ui.screens.shopping

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ShoppingContent
import com.bruno13palhano.shopdanimanagement.ui.screens.shopping.viewmodel.EditShoppingItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShoppingItemScreen(
    shoppingItemId: Long,
    navigateUp: () -> Unit,
    viewModel: EditShoppingItemViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getShoppingItem(shoppingItemId)
    }

    val isItemNotEmpty by viewModel.isItemNotEmpty.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
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
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateDate(it)
                        }
                        showDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.date_label))
                }
            }
        ) {
            datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.dateInMillis,
                initialDisplayMode = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = datePickerState,
                showModeToggle = orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    ShoppingContent(
        screenTitle = stringResource(id = R.string.edit_shopping_item_label),
        snackbarHostState = snackbarHostState,
        name = viewModel.name,
        purchasePrice = viewModel.purchasePrice,
        quantity = viewModel.quantity,
        isPaid = viewModel.isPaid,
        photo = viewModel.photo,
        date = viewModel.date,
        onNameChange = viewModel::updateName,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onQuantityChange = viewModel::updateQuantity,
        onIsPaidChange = viewModel::updateIsPaid,
        onDateClick = { showDatePickerDialog = true },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onDoneClick = {
            if (isItemNotEmpty) {
                viewModel.updateShoppingItem(shoppingItemId)
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