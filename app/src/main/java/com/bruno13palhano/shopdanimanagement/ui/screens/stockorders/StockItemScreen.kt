package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.ItemContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.currentDate
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.setAlarmNotification
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(
    isEditable: Boolean,
    productId: Long,
    stockItemId: Long,
    screenTitle: String,
    navigateUp: () -> Unit,
    viewModel: StockItemViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        if (isEditable) {
            viewModel.getStockItem(stockItemId)
        } else {
            viewModel.updateDate(currentDate)
            viewModel.updateDateOfPayment(currentDate)
            viewModel.updateValidity(currentDate)
            viewModel.getProduct(productId)
        }
    }

    val stockItemState by viewModel.stockItemState.collectAsStateWithLifecycle()
    val isItemNotEmpty by viewModel.isItemNotEmpty.collectAsStateWithLifecycle()
    val notifyItem by viewModel.notifyItem.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val orientation = configuration.orientation
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var showContent by remember { mutableStateOf(true) }
    var datePickerState = rememberDatePickerState()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var dateOfPaymentPickerState = rememberDatePickerState()
    var showDateOfPaymentPickerDialog by remember { mutableStateOf(false) }
    var validityPickerState = rememberDatePickerState()
    var showValidityPickerDialog by remember { mutableStateOf(false) }

    val menuItems = arrayOf(
        stringResource(id = R.string.delete_label)
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    val expiredTitle = stringResource(id = R.string.stock_expired_item_label)
    val expiredDescription = stringResource(id = R.string.stock_expired_item_tag, viewModel.name)
    val debitTitle = stringResource(id = R.string.stock_payment_label)
    val debitDescription = stringResource(
        id = R.string.payment_tag,
        viewModel.name,
        viewModel.purchasePrice
    )

    when (stockItemState) {
        UiState.Fail -> { showContent = true }

        UiState.InProgress -> {
            showContent = false
            CircularProgress()
        }

        UiState.Success -> {
            LaunchedEffect(key1 = Unit) { navigateUp() }

            if (notifyItem) {
                setAlarmNotification(
                    id = stockItemId,
                    title = debitTitle,
                    date = viewModel.dateOfPayment,
                    description = debitDescription,
                    context = context
                )
            }

            setAlarmNotification(
                id = productId,
                title = expiredTitle,
                date = viewModel.validity,
                description = expiredDescription,
                context = context
            )
        }
    }

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
                initialSelectedDateMillis = viewModel.date,
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
                initialSelectedDateMillis = viewModel.dateOfPayment,
                initialDisplayMode = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = dateOfPaymentPickerState,
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
                initialSelectedDateMillis = viewModel.validity,
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

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)),
        exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessLow))
    ) {
        ItemContent(
            screenTitle = screenTitle,
            snackbarHostState = snackbarHostState,
            menuItems = menuItems,
            name = viewModel.name,
            photo = viewModel.photo,
            quantity = viewModel.quantity,
            date = dateFormat.format(viewModel.date),
            dateOfPayment = dateFormat.format(viewModel.dateOfPayment),
            purchasePrice = viewModel.purchasePrice,
            salePrice = viewModel.salePrice,
            validity = dateFormat.format(viewModel.validity),
            category = viewModel.category,
            company = viewModel.company,
            isPaid = viewModel.isPaid,
            onQuantityChange = viewModel::updateQuantity,
            onPurchasePriceChange = viewModel::updatePurchasePrice,
            onSalePriceChange = viewModel::updateSalePrice,
            onIsPaidChange = viewModel::updateIsPaid,
            onDateClick = { showDatePickerDialog = true },
            onDateOfPaymentClick = { showDateOfPaymentPickerDialog = true },
            onValidityClick = { showValidityPickerDialog = true },
            onMoreOptionsItemClick = { index ->
                when (index) {
                    0 -> {
                        viewModel.deleteStockItem(
                            stockOrderId = stockItemId,
                            onError = { error ->
                                scope.launch {
                                    if (error == DataError.DeleteDatabase.error)
                                        snackbarHostState.showSnackbar(
                                            message = errors[error],
                                            withDismissAction = true
                                        )

                                    navigateUp()
                                }
                            }
                        )
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
                        viewModel.updateStockItem(
                            stockItemId = stockItemId,
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
                        viewModel.insertItems(
                            productId = productId,
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