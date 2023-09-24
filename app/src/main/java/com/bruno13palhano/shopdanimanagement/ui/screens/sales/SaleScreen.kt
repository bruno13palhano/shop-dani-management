package com.bruno13palhano.shopdanimanagement.ui.screens.sales

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
import com.bruno13palhano.shopdanimanagement.ui.components.SaleContent
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SaleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleScreen(
    isEdit: Boolean,
    screenTitle: String,
    isOrderedByCustomer: Boolean,
    productId: Long,
    saleId: Long,
    navigateUp: () -> Unit,
    viewModel: SaleViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (isEdit) {
            viewModel.getSale(saleId)
        } else {
            if (isOrderedByCustomer) {
                viewModel.getProduct(productId)
            } else {
                viewModel.getStockItem(productId)
            }
        }
    }
    val isSaleNotEmpty by viewModel.isSaleNotEmpty.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var dateOfSalePickerState = rememberDatePickerState()
    var showDateOfSalePickerDialog by remember { mutableStateOf(false) }
    var dateOfPaymentPickerState = rememberDatePickerState()
    var showDateOfPaymentPickerDialog by remember { mutableStateOf(false) }

    if (showDateOfSalePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDateOfSalePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        dateOfSalePickerState.selectedDateMillis?.let {
                            viewModel.updateDateOfSale(it)
                        }
                        showDateOfSalePickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.date_of_sale_label))
                }
            }
        ) {
            dateOfSalePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.dateOfSaleInMillis,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = dateOfSalePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    if (showDateOfPaymentPickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDateOfPaymentPickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        dateOfPaymentPickerState.selectedDateMillis?.let {
                            viewModel.updateDateOfPayment(it)
                        }
                        showDateOfPaymentPickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.date_of_payment_label))
                }
            }
        ) {
            dateOfPaymentPickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.dateOfPaymentInMillis,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = dateOfPaymentPickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT,
            )
        }
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = stringResource(id = R.string.empty_fields_error)
    val stockQuantityMessage = stringResource(id = R.string.only_x_items_error_label, viewModel.stockQuantity)

    val menuItems = arrayOf(
        stringResource(id = R.string.delete_label)
    )

    SaleContent(
        isEdit = isEdit,
        screenTitle = screenTitle,
        snackbarHostState = snackbarHostState,
        menuItems = menuItems,
        productName = viewModel.productName,
        customerName = viewModel.customerName,
        photo = viewModel.photo,
        quantity = viewModel.quantity,
        dateOfSale = viewModel.dateOfSale,
        dateOfPayment = viewModel.dateOfPayment,
        purchasePrice = viewModel.purchasePrice,
        salePrice = viewModel.salePrice,
        category = viewModel.category,
        company = viewModel.company,
        isPaidByCustomer = viewModel.isPaidByCustomer,
        onQuantityChange = viewModel::updateQuantity,
        onPurchasePriceChange = viewModel::updatePurchasePrice,
        onSalePriceChange = viewModel::updateSalePrice,
        onIsPaidByCustomerChange = viewModel::updateIsPaidByCustomer,
        onDateOfSaleClick = { showDateOfSalePickerDialog = true },
        onDateOfPaymentClick = { showDateOfPaymentPickerDialog = true },
        customers = viewModel.allCustomers,
        onDismissCustomer = { focusManager.clearFocus(force = true) },
        onCustomerSelected = viewModel::updateCustomerName,
        onOutsideClick = {
            keyboardController?.hide()
            focusManager.clearFocus(force = true)
        },
        onMoreOptionsItemClick = { index ->
            when (index) {
                SaleItemMenu.delete -> {
                    viewModel.deleteSale(saleId)
                    navigateUp()
                }
            }
        },
        onDoneButtonClick = {
            if (isSaleNotEmpty) {
                if (isEdit) {
                    viewModel.updateSale(saleId)
                    navigateUp()
                } else {
                    viewModel.insertSale(
                        productId = productId,
                        isOrderedByCustomer = isOrderedByCustomer,
                        onSuccess = navigateUp,
                        onError = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = stockQuantityMessage
                                )
                            }
                        }
                    )
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(errorMessage)
                }
            }
        },
        navigateUp = navigateUp
    )
}

private object SaleItemMenu {
    const val delete = 0
}