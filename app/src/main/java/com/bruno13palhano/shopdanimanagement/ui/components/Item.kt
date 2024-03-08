package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemContent(
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    menuItems: Array<String>,
    name: String,
    photo: ByteArray,
    quantity: String,
    date: String,
    dateOfPayment: String,
    purchasePrice: String,
    salePrice: String,
    validity: String,
    category: String,
    company: String,
    isPaid: Boolean,
    onQuantityChange: (quantity: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onSalePriceChange: (salePrice: String) -> Unit,
    onIsPaidChange: (isPaid: Boolean) -> Unit,
    onDateClick: () -> Unit,
    onDateOfPaymentClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onValidityClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = stringResource(id = R.string.more_options_label)
                        )
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            MoreOptionsMenu(
                                items = menuItems,
                                expanded = expanded,
                                onDismissRequest = { expandedValue -> expanded = expandedValue },
                                onClick = onMoreOptionsItemClick
                            )
                        }
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
        Column(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                ElevatedCard(
                    modifier = Modifier
                        .padding(start = 8.dp),
                ) {
                    if (photo.isEmpty()) {
                        Image(
                            modifier = Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            imageVector = Icons.Filled.Image,
                            contentDescription = stringResource(id = R.string.product_image_label),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = rememberAsyncImagePainter(model = photo),
                            contentDescription = stringResource(id = R.string.product_image_label),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Column {
                    CustomTextField(
                        text = name,
                        onTextChange = {},
                        readOnly = true,
                        icon = Icons.Filled.Title,
                        label = stringResource(id = R.string.name_label),
                        placeholder = ""
                    )
                    CustomIntegerField(
                        value = quantity,
                        onValueChange = onQuantityChange,
                        icon = Icons.Filled.ShoppingBag,
                        label = stringResource(id = R.string.quantity_label),
                        placeholder = stringResource(id = R.string.enter_quantity_label)
                    )
                }
            }
            CustomClickField(
                value = date,
                onClick = onDateClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.date_label),
                placeholder = stringResource(id = R.string.enter_date_label)
            )
            CustomClickField(
                value = dateOfPayment,
                onClick = onDateOfPaymentClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.date_of_payment_label),
                placeholder = stringResource(id = R.string.enter_date_label)
            )
            CustomClickField(
                value = validity,
                onClick = onValidityClick,
                icon = Icons.Filled.CalendarMonth,
                label = stringResource(id = R.string.validity_label),
                placeholder = stringResource(id = R.string.enter_validity_label)
            )
            CustomFloatField(
                value = purchasePrice,
                onValueChange = onPurchasePriceChange,
                icon = Icons.Filled.Paid,
                label = stringResource(id = R.string.purchase_price_label),
                placeholder = stringResource(id = R.string.enter_purchase_price_label)
            )
            CustomFloatField(
                value = salePrice,
                onValueChange = onSalePriceChange,
                icon = Icons.Filled.PriceCheck,
                label = stringResource(id = R.string.sale_price_label),
                placeholder = stringResource(id = R.string.enter_sale_price_label)
            )
            CustomTextField(
                text = category,
                onTextChange = {},
                icon = Icons.Filled.Category,
                label = stringResource(id = R.string.categories_label),
                placeholder = ""
            )
            CustomTextField(
                text = company,
                onTextChange = {},
                icon = Icons.Filled.BusinessCenter,
                label = stringResource(id = R.string.company_label),
                placeholder = ""
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPaid,
                    onCheckedChange = onIsPaidChange
                )
                Text(text = stringResource(id = R.string.is_paid_label))
            }
        }
    }
}