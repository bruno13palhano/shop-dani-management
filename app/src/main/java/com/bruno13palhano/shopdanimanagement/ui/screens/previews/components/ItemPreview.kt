package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.ItemContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ItemDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ItemContent(
                screenTitle = stringResource(id = R.string.new_stock_item_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = arrayOf(),
                name = "",
                photo = byteArrayOf(),
                quantity = "",
                date = "",
                dateOfPayment = "",
                purchasePrice = "",
                salePrice = "",
                validity = "",
                category = "",
                company = "",
                isPaid = false,
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onDateOfPaymentClick = {},
                onMoreOptionsItemClick = {},
                onValidityClick = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ItemContent(
                screenTitle = stringResource(id = R.string.edit_stock_item_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = arrayOf(),
                name = "",
                photo = byteArrayOf(),
                quantity = "",
                date = "",
                dateOfPayment = "",
                purchasePrice = "",
                salePrice = "",
                validity = "",
                category = "",
                company = "",
                isPaid = true,
                onQuantityChange = {},
                onPurchasePriceChange = {},
                onSalePriceChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onDateOfPaymentClick = {},
                onMoreOptionsItemClick = {},
                onValidityClick = {},
                onOutsideClick = {},
                onDoneButtonClick = {},
                navigateUp = {}
            )
        }
    }
}