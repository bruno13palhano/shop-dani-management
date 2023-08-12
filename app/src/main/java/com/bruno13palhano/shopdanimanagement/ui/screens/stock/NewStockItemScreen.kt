package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.StockOrderContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun NewStockItemScreen(
    productId: Long,
    navigateUp: () -> Unit
) {
    StockOrderContent(
        screenTitle = stringResource(id = R.string.new_item_label),
        name = "",
        quantity = "",
        isOrderedByCustomer = true,
        photo = "",
        date = "",
        onQuantityChange = {},
        onIsOrderedByCustomerChange = {},
        onDateClick = {},
        onOutsideClick = {},
        onDoneClick = {},
        navigateUp = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun NewStockItemDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewStockItemScreen(
                productId = 1L,
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun NewStockItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NewStockItemScreen(
                productId = 1L,
                navigateUp = {}
            )
        }
    }
}