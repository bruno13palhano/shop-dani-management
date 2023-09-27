package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ItemContent
import com.bruno13palhano.shopdanimanagement.ui.screens.setAlarmNotification
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.ItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    isEditable: Boolean,
    isOrderedByCustomer: Boolean,
    productId: Long,
    stockOrderItemId: Long,
    screenTitle: String,
    navigateUp: () -> Unit,
    viewModel: ItemViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (isEditable) {
            viewModel.getStockOrder(stockOrderItemId)
        } else {
            viewModel.getProduct(productId)
        }
    }

    val isItemNotEmpty by viewModel.isItemNotEmpty.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val orientation = configuration.orientation
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var datePickerState = rememberDatePickerState()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var validityPickerState = rememberDatePickerState()
    var showValidityPickerDialog by remember { mutableStateOf(false) }

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

    if (showValidityPickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showValidityPickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        validityPickerState.selectedDateMillis?.let {
                            viewModel.updateValidity(it)
                        }
                        showValidityPickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.validity_label))
                }
            }
        ) {
            validityPickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.validityInMillis,
                initialDisplayMode = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = validityPickerState,
                showModeToggle = orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    val menuItems = arrayOf(
        stringResource(id = R.string.delete_label)
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)

    ItemContent(
        screenTitle = screenTitle,
        snackbarHostState = snackbarHostState,
        menuItems = menuItems,
        name = viewModel.name,
        photo = viewModel.photo,
        quantity = viewModel.quantity,
        date = viewModel.date,
        purchasePrice = viewModel.purchasePrice,
        salePrice = viewModel.salePrice,
        validity = viewModel.validity,
        category = viewModel.category,
        company = viewModel.company,
        isPaid = viewModel.isPaid,
        onQuantityChange = viewModel::updateQuantity,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onSalePriceChange = viewModel::updateSalePrice,
        onIsPaidChange = viewModel::updateIsPaid,
        onDateClick = { showDatePickerDialog = true },
        onValidityClick = { showValidityPickerDialog = true },
        onMoreOptionsItemClick = { index ->
            when (index) {
                0 -> {
                    viewModel.deleteStockOrderItem(stockOrderId = stockOrderItemId)
                    navigateUp()
                }
            }
        },
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onDoneButtonClick = {
            if (isItemNotEmpty) {
                if (isEditable) {
                    viewModel.updateStockOrderItem(stockOrderItemId, isOrderedByCustomer)
                    setAlarmNotification(
                        id = stockOrderItemId,
                        title = viewModel.name,
                        date = viewModel.validityInMillis,
                        description = viewModel.company,
                        context = context
                    )
                } else {
                    viewModel.insertItems(productId, isOrderedByCustomer)
                    setAlarmNotification(
                        id = productId,
                        title = viewModel.name,
                        date = viewModel.validityInMillis,
                        description = viewModel.company,
                        context = context
                    )
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