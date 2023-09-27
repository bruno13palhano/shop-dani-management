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
import com.bruno13palhano.shopdanimanagement.ui.components.ShoppingContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingContent(
                screenTitle = stringResource(id = R.string.new_item_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = arrayOf(),
                name = "",
                purchasePrice = "",
                quantity = "",
                isPaid = true,
                photo = "",
                date = "",
                onPurchasePriceChange = {},
                onQuantityChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ShoppingPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShoppingContent(
                screenTitle = stringResource(id = R.string.new_item_label),
                snackbarHostState = remember { SnackbarHostState() },
                menuItems = arrayOf(),
                name = "",
                purchasePrice = "",
                quantity = "",
                isPaid = true,
                photo = "",
                date = "",
                onPurchasePriceChange = {},
                onQuantityChange = {},
                onIsPaidChange = {},
                onDateClick = {},
                onMoreOptionsItemClick = {},
                onOutsideClick = {},
                onDoneClick = {},
                navigateUp = {}
            )
        }
    }
}