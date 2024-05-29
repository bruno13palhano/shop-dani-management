package com.bruno13palhano.shopdanimanagement.ui.screens.deliveries

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.CircularProgress
import com.bruno13palhano.shopdanimanagement.ui.components.CustomClickField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomFloatField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomIntegerField
import com.bruno13palhano.shopdanimanagement.ui.components.CustomTextField
import com.bruno13palhano.shopdanimanagement.ui.components.clickableNoEffect
import com.bruno13palhano.shopdanimanagement.ui.screens.common.DataError
import com.bruno13palhano.shopdanimanagement.ui.screens.common.UiState
import com.bruno13palhano.shopdanimanagement.ui.screens.common.getErrors
import com.bruno13palhano.shopdanimanagement.ui.screens.dateFormat
import com.bruno13palhano.shopdanimanagement.ui.screens.deliveries.viewmodel.DeliveryViewModel
import kotlinx.coroutines.launch

@Composable
fun DeliveryRoute(
    showBottomMenu: (show: Boolean) -> Unit,
    gesturesEnabled: (enabled: Boolean) -> Unit,
    deliveryId: Long,
    navigateUp: () -> Unit
) {
    showBottomMenu(true)
    gesturesEnabled(true)
    DeliveryScreen(
        deliveryId = deliveryId,
        navigateUp = navigateUp
    )
}

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
    val deliveryState by viewModel.deliveryState.collectAsStateWithLifecycle()
    var showContent by remember { mutableStateOf(true) }
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
            shippingDatePickerState =
                rememberDatePickerState(
                    initialSelectedDateMillis = viewModel.shippingDate,
                    initialDisplayMode =
                        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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
            deliveryDatePickerState =
                rememberDatePickerState(
                    initialSelectedDateMillis = viewModel.deliveryDate,
                    initialDisplayMode =
                        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
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

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val errors = getErrors()

    when (deliveryState) {
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
                    saleId = deliveryId,
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
            },
            navigateUp = navigateUp
        )
    }
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
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.delivery_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            CustomTextField(
                text = name,
                onTextChange = {},
                readOnly = true,
                icon = Icons.Filled.Person,
                label = stringResource(id = R.string.name_label),
                placeholder = ""
            )
            CustomTextField(
                text = address,
                onTextChange = {},
                readOnly = true,
                icon = Icons.Filled.LocationCity,
                label = stringResource(id = R.string.address_label),
                placeholder = ""
            )
            CustomIntegerField(
                value = phoneNumber,
                onValueChange = {},
                readOnly = true,
                icon = Icons.Filled.Phone,
                label = stringResource(id = R.string.phone_number_label),
                placeholder = ""
            )
            CustomTextField(
                text = productName,
                onTextChange = {},
                readOnly = true,
                icon = Icons.Filled.Title,
                label = stringResource(id = R.string.product_name_label),
                placeholder = ""
            )
            CustomFloatField(
                value = price,
                onValueChange = {},
                icon = Icons.Filled.PriceCheck,
                label = stringResource(id = R.string.sale_price_label),
                placeholder = ""
            )
            CustomFloatField(
                value = deliveryPrice,
                onValueChange = onDeliveryPriceChange,
                icon = Icons.Filled.LocalShipping,
                label = stringResource(id = R.string.delivery_price_label),
                placeholder = stringResource(id = R.string.enter_delivery_price_label)
            )
            CustomClickField(
                value = shippingDate,
                onClick = onShippingDateClick,
                icon = Icons.Filled.CalendarMonth,
                label = stringResource(id = R.string.shipping_date_label),
                placeholder = ""
            )
            CustomClickField(
                value = deliveryDate,
                onClick = onDeliveryDateClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.delivery_date_label),
                placeholder = ""
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