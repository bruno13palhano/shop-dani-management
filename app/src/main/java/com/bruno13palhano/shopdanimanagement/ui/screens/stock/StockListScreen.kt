package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import com.bruno13palhano.shopdanimanagement.ui.components.StockListContent

@Composable
fun StockListScreen(
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    StockListContent(
        itemList = emptyList(),
        onItemClick = onItemClick,
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}