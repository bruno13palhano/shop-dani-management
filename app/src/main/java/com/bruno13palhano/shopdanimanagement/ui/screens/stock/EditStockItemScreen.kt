package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import com.bruno13palhano.shopdanimanagement.ui.components.StockOrderContent

@Composable
fun EditStockItemScreen(
    productId: Long,
    navigateUp: () -> Unit
) {
    StockOrderContent(
        screenTitle = "Edit Stock Item",
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
        navigateUp = navigateUp
    )
}