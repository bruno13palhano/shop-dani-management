package com.bruno13palhano.shopdanimanagement.ui.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.StockOrderListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel.StockOrderListViewModel

@Composable
fun StockOrderListScreen(
    isOrderedByCustomer: Boolean,
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockOrderListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getItems(isOrderedByCustomer)
    }

    val menuOptions = mutableListOf("All products")
    val stockList by viewModel.stockList.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    menuOptions.addAll(categories)

    StockOrderListContent(
        screenTitle = screenTitle,
        itemList = stockList,
        menuOptions = menuOptions.toTypedArray(),
        onItemClick = onItemClick,
        onMenuItemClick = {},
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}