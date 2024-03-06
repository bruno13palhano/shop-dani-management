package com.bruno13palhano.shopdanimanagement.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleContent(
    isEdit: Boolean,
    screenTitle: String,
    snackbarHostState: SnackbarHostState,
    menuItems: Array<String>,
    productName: String,
    customerName: String,
    photo: ByteArray,
    quantity: String,
    dateOfSale: String,
    dateOfPayment: String,
    purchasePrice: String,
    deliveryPrice: String,
    salePrice: String,
    category: String,
    company: String,
    amazonCode: String,
    amazonRequestNumber: String,
    amazonPrice: String,
    amazonTax: String,
    amazonProfit: String,
    amazonSKU: String,
    resaleProfit: String,
    totalProfit: String,
    isPaidByCustomer: Boolean,
    isAmazon: Boolean,
    onQuantityChange: (quantity: String) -> Unit,
    onAmazonCodeChange: (amazonCode: String) -> Unit,
    onAmazonRequestNumberChange: (amazonRequestNumber: String) -> Unit,
    onAmazonPriceChange: (amazonPrice: String) -> Unit,
    onAmazonTaxChange: (amazonTax: String) -> Unit,
    onAmazonSKUChange: (amazonSKU: String) -> Unit,
    onResaleProfitChange: (resaleProfit: String) -> Unit,
    onPurchasePriceChange: (purchasePrice: String) -> Unit,
    onSalePriceChange: (salePrice: String) -> Unit,
    onDeliveryPriceChange: (deliveryPrice: String) -> Unit,
    onIsPaidByCustomerChange: (isPaidByCustomer: Boolean) -> Unit,
    onIsAmazonChange: (isAmazon: Boolean) -> Unit,
    onDateOfSaleClick: () -> Unit,
    onDateOfPaymentClick: () -> Unit,
    customers: List<CustomerCheck>,
    onDismissCustomer: () -> Unit,
    onCustomerSelected: (selected: String) -> Unit,
    onOutsideClick: () -> Unit,
    onMoreOptionsItemClick: (index: Int) -> Unit,
    onDoneButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var openCustomerSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.clickableNoEffect { onOutsideClick() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    if (isEdit) {
                        IconButton(onClick = { expanded = true } ) {
                            Box(contentAlignment = Alignment.Center) {
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
                                        onDismissRequest = { expandedValue ->
                                            expanded = expandedValue
                                        },
                                        onClick = onMoreOptionsItemClick
                                    )
                                }
                            }
                        }
                    }
                },
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
            AnimatedVisibility(visible = openCustomerSheet) {
                BottomSheet(
                    onDismissBottomSheet = {
                        openCustomerSheet = false
                        onDismissCustomer()
                    }
                ) {
                    val initialCustomer = customers
                        .filter { customer -> customer.isChecked }
                        .findLast { customer -> customer.isChecked }?.name
                    val (selected, onOptionSelected) = rememberSaveable {
                        mutableStateOf(initialCustomer)
                    }

                    Column(modifier = Modifier.padding(bottom = 32.dp)) {
                        customers.forEach { customerItem ->
                            ListItem(
                                headlineContent = { Text(text = customerItem.name) },
                                leadingContent = {
                                    RadioButton(
                                        selected = customerItem.name == selected,
                                        onClick = {
                                            onOptionSelected(customerItem.name)
                                            onCustomerSelected(customerItem.name)
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

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
                            contentDescription = stringResource(id = R.string.product_image_label)
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .size(128.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = rememberAsyncImagePainter(model = photo),
                            contentDescription = stringResource(id = R.string.product_image_label)
                        )
                    }
                }
                Column {
                    CustomTextField(
                        text = productName,
                        onTextChange = {},
                        icon = Icons.Filled.Title,
                        label = stringResource(id = R.string.name_label),
                        placeholder = stringResource(id = R.string.name_label),
                        readOnly = true
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
                value = customerName,
                onClick = { openCustomerSheet = true },
                icon = Icons.Filled.Person,
                label = stringResource(id = R.string.customer_name_label),
                placeholder = stringResource(id = R.string.enter_customer_name_label)
            )
            CustomClickField(
                value = dateOfSale,
                onClick = onDateOfSaleClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.date_of_sale_label),
                placeholder = stringResource(id = R.string.enter_date_label)
            )
            CustomClickField(
                value = dateOfPayment,
                onClick = onDateOfPaymentClick,
                icon = Icons.Filled.EditCalendar,
                label = stringResource(id = R.string.date_of_payment_label),
                placeholder = stringResource(id = R.string.enter_date_label)
            )

            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Checkbox(
                    checked = isAmazon,
                    onCheckedChange = onIsAmazonChange
                )
                Text(text = stringResource(id = R.string.is_amazon_label))
            }
            AnimatedVisibility(visible = isAmazon) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary, RectangleShape)
                ) {
                    CustomIntegerField(
                        value = amazonCode,
                        onValueChange = onAmazonCodeChange,
                        icon = Icons.Filled.Numbers,
                        label = stringResource(id = R.string.amazon_code_label),
                        placeholder = stringResource(id = R.string.enter_amazon_code_label)
                    )
                    CustomIntegerField(
                        value = amazonRequestNumber,
                        onValueChange = onAmazonRequestNumberChange,
                        icon = Icons.Filled.Numbers,
                        label = stringResource(id = R.string.amazon_request_number_label),
                        placeholder = stringResource(
                            id = R.string.enter_amazon_request_number_label
                        )
                    )
                    CustomFloatField(
                        value = amazonPrice,
                        onValueChange = onAmazonPriceChange,
                        icon = Icons.Filled.Paid,
                        label = stringResource(id = R.string.amazon_price_label),
                        placeholder = stringResource(id = R.string.enter_amazon_price_label)
                    )
                    CustomFloatField(
                        value = resaleProfit,
                        onValueChange = onResaleProfitChange,
                        icon = Icons.Filled.Paid,
                        label = stringResource(id = R.string.resale_profit_label),
                        placeholder = stringResource(id = R.string.enter_resale_profit_label)
                    )
                    CustomIntegerField(
                        value = amazonTax,
                        onValueChange = onAmazonTaxChange,
                        icon = Icons.Filled.Paid,
                        label = stringResource(id = R.string.amazon_tax_label),
                        placeholder = stringResource(id = R.string.enter_amazon_tax_label)
                    )
                    CustomTextField(
                        text = amazonProfit,
                        onTextChange = {},
                        icon = Icons.Filled.Paid,
                        label = stringResource(id = R.string.amazon_profit_label),
                        placeholder = stringResource(id = R.string.enter_amazon_profit_label),
                        readOnly = true
                    )
                    CustomTextField(
                        text = amazonSKU,
                        onTextChange = onAmazonSKUChange,
                        icon = Icons.Filled.Title,
                        label = stringResource(id = R.string.amazon_sku_label),
                        placeholder = stringResource(id = R.string.enter_amazon_sku_label)
                    )
                    CustomTextField(
                        text = totalProfit,
                        onTextChange = {},
                        icon = Icons.Filled.Paid,
                        label = stringResource(id = R.string.total_profit_label),
                        placeholder = stringResource(id = R.string.enter_total_profit_label)
                    )
                }
            }

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
            CustomFloatField(
                value = deliveryPrice,
                onValueChange = onDeliveryPriceChange,
                icon = Icons.Filled.LocalShipping,
                label = stringResource(id = R.string.delivery_price_label),
                placeholder = stringResource(id = R.string.enter_delivery_price_label)
            )
            CustomClickField(
                value = category,
                onClick = {},
                icon = Icons.Filled.Category,
                label = stringResource(id = R.string.categories_label),
                placeholder = ""
            )
            CustomClickField(
                value = company,
                onClick = {},
                icon = Icons.Filled.BusinessCenter,
                label = stringResource(id = R.string.company_label),
                placeholder = ""
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isPaidByCustomer,
                    onCheckedChange = onIsPaidByCustomerChange
                )
                Text(text = stringResource(id = R.string.is_paid_by_customer_label))
            }
        }
    }
}