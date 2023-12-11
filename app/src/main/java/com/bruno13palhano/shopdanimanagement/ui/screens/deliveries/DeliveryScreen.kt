package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

import android.content.res.Configuration
import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.clearFocusOnKeyboardDismiss
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveryViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryScreen(
    deliveryId: Long,
    navigateUp: () -> Unit,
    viewModel: DeliveryViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getDeliveryById(deliveryId)
    }

    val configuration = LocalConfiguration.current
    val focusManager = LocalFocusManager.current
    var shippingDatePickerState = rememberDatePickerState()
    var showShippingDatePickerDialog by remember { mutableStateOf(false) }
    var deliveryDatePickerState = rememberDatePickerState()
    var showDeliveryDatePickerDialog by remember { mutableStateOf(false) }

    if (showShippingDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showShippingDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        shippingDatePickerState.selectedDateMillis?.let {
                            viewModel.updateShippingDate(it)
                        }
                        showShippingDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.shipping_date_label))
                }
            }
        ) {
            shippingDatePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.shippingDate,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = shippingDatePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    if (showDeliveryDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = {
                showDeliveryDatePickerDialog = false
                focusManager.clearFocus(force = true)
            },
            confirmButton = {
                Button(
                    onClick = {
                        deliveryDatePickerState.selectedDateMillis?.let {
                            viewModel.updateDeliveryDate(it)
                        }
                        showDeliveryDatePickerDialog = false
                        focusManager.clearFocus(force = true)
                    }
                ) {
                    Text(text = stringResource(id = R.string.delivery_date_label))
                }
            }
        ) {
            deliveryDatePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.deliveryDate,
                initialDisplayMode = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    DisplayMode.Picker
                } else {
                    DisplayMode.Input
                }
            )
            DatePicker(
                state = deliveryDatePickerState,
                showModeToggle = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            )
        }
    }

    DeliveryContent(
        name = viewModel.name,
        address = viewModel.address,
        phoneNumber = viewModel.phoneNumber,
        productName = viewModel.productName,
        price = viewModel.price,
        deliveryPrice = viewModel.deliveryPrice,
        shippingDate = dateFormat.format(viewModel.shippingDate),
        deliveryDate = dateFormat.format(viewModel.deliveryDate),
        delivered = viewModel.delivered,
        onDeliveryPriceChange = viewModel::updateDeliveryPrice,
        onDeliveredChange = viewModel::updateDelivered,
        onShippingDateClick = { showShippingDatePickerDialog = true },
        onDeliveryDateClick = { showDeliveryDatePickerDialog = true },
        onOutsideClick = { focusManager.clearFocus(force = true) },
        onDoneButtonClick = {
            viewModel.updateDelivery(
                deliveryId = deliveryId,
                onError = {}
            ) {

            }
            navigateUp()
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliveryContent(
    name: String,
    address: String,
    phoneNumber: String,
    productName: String,
    price: String,
    deliveryPrice: String,
    shippingDate: String,
    deliveryDate: String,
    delivered: Boolean,
    onDeliveryPriceChange: (deliveryPrice: String) -> Unit,
    onDeliveredChange: (delivered: Boolean) -> Unit,
    onShippingDateClick: () -> Unit,
    onDeliveryDateClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit,
) {
    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault()) as DecimalFormat
    val decimalSeparator = decimalFormat.decimalFormatSymbols.decimalSeparator
    val pattern = remember { Regex("^\\d*\\$decimalSeparator?\\d*\$") }

    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.delivery_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = name,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = stringResource(id = R.string.name_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.name_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = address,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocationCity,
                        contentDescription = stringResource(id = R.string.address_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.address_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = phoneNumber,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = stringResource(id = R.string.phone_number_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.phone_number_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = productName,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Title,
                        contentDescription = stringResource(id = R.string.product_name_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.product_name_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = price,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.PriceCheck,
                        contentDescription = stringResource(id = R.string.sale_price_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.sale_price_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = deliveryPrice,
                onValueChange = { deliveryPriceValue ->
                    if (deliveryPriceValue.isEmpty() || deliveryPrice.matches(pattern)) {
                        onDeliveryPriceChange(deliveryPriceValue)
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.LocalShipping,
                        contentDescription = stringResource(id = R.string.delivery_price_label)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Decimal
                ),
                keyboardActions = KeyboardActions(onDone = {
                    defaultKeyboardAction(ImeAction.Done)
                }),
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.delivery_price_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_delivery_price_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onShippingDateClick()
                        }
                    },
                value = shippingDate,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = stringResource(id = R.string.shipping_date_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.shipping_date_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onDeliveryDateClick()
                        }
                    },
                value = deliveryDate,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = stringResource(id = R.string.delivery_date_label)
                    )
                },
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.delivery_date_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = delivered,
                    onCheckedChange = onDeliveredChange
                )
                Text(text = stringResource(id = R.string.is_delivered_label))
            }
        }
    }
}
