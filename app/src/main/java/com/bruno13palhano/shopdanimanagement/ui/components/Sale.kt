package com.bruno13palhano.shopdanimanagement.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaleContent(
    screenTitle: String,
    name: String,
    isPaidByCustomer: Boolean,
    photo: String,
    dateOfSale: String,
    dateOfPayment: String,
    onIsPaidByCustomerChange: (isPaidByCustomer: Boolean) -> Unit,
    onDateOfSaleClick: () -> Unit,
    onDateOfPaymentClick: () -> Unit,
    onOutsideClick: () -> Unit,
    onDoneClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .clickableNoEffect {
                onOutsideClick()
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onDoneClick) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = stringResource(id = R.string.done_label)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(start = 16.dp)
            ) {
                if (photo.isEmpty()) {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                } else {
                    Image(
                        modifier = Modifier
                            .size(200.dp)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        painter = rememberAsyncImagePainter(model = photo),
                        contentDescription = stringResource(id = R.string.product_image_label)
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .clearFocusOnKeyboardDismiss(),
                value = name,
                onValueChange = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Title,
                        contentDescription = stringResource(id = R.string.name_label)
                    )
                },
                singleLine = true,
                enabled = false,
                label = {
                    Text(
                        text = stringResource(id = R.string.name_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_name_label),
                        fontStyle = FontStyle.Italic
                    )
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onDateOfSaleClick()
                        }
                    },
                value = dateOfSale,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = stringResource(id = R.string.date_of_sale_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.date_of_sale_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_date_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.hasFocus) {
                            onDateOfPaymentClick()
                        }
                    },
                value = dateOfPayment,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.EditCalendar,
                        contentDescription = stringResource(id = R.string.date_of_payment_label)
                    )
                },
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(id = R.string.date_of_payment_label),
                        fontStyle = FontStyle.Italic
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_date_label),
                        fontStyle = FontStyle.Italic
                    )
                }
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 2.dp)
                    .align(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(id = R.string.is_paid_by_customer_label))
                Checkbox(
                    checked = isPaidByCustomer,
                    onCheckedChange = onIsPaidByCustomerChange
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SaleDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleContent(
                screenTitle = stringResource(id = R.string.new_sale_label),
                name = "",
                photo = "",
                isPaidByCustomer = true,
                dateOfSale = "",
                dateOfPayment = "",
                onIsPaidByCustomerChange = {},
                onDateOfSaleClick = {},
                onDateOfPaymentClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SalePreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SaleContent(
                screenTitle = stringResource(id = R.string.edit_sale_label),
                name = "",
                photo = "",
                isPaidByCustomer = true,
                dateOfSale = "",
                dateOfPayment = "",
                onIsPaidByCustomerChange = {},
                onDateOfSaleClick = {},
                onDateOfPaymentClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}