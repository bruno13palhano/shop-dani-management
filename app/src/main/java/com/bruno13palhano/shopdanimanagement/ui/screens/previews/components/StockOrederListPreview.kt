package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.StockOrderListContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun StockOrderListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockOrderListContent(
                isAddButtonEnabled = false,
                screenTitle = stringResource(id = R.string.orders_list_label),
                itemList = items,
                menuOptions = arrayOf(),
                onItemClick = {},
                onSearchClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun StockOrderListItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockOrderListContent(
                isAddButtonEnabled = true,
                screenTitle = stringResource(id = R.string.stock_list_label),
                itemList = items,
                menuOptions = arrayOf(),
                onItemClick = {},
                onSearchClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    Stock(id= 1L, name = "Essencial", photo = "", purchasePrice = 120.45F, quantity = 12),
    Stock(id= 2L, name = "Kaiak", photo = "", purchasePrice = 40.33F, quantity = 2),
    Stock(id= 3L, name = "Homem", photo = "", purchasePrice = 99.99F, quantity = 7),
    Stock(id= 4L, name = "Luna", photo = "", purchasePrice = 12.39F, quantity = 2),
    Stock(id= 5L, name = "Essencial", photo = "", purchasePrice = 56.78F, quantity = 1),
    Stock(id= 6L, name = "Una", photo = "", purchasePrice = 12.12F, quantity = 2),
)