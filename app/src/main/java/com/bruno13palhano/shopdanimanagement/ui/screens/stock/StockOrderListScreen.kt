package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.StockListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stock.viewmodel.StockListViewModel

@Composable
fun StockListScreen(
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockListViewModel = hiltViewModel()
) {
    val menuOptions = mutableListOf("All products")
    val stockList by viewModel.stockList.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    menuOptions.addAll(categories)

    StockListContent(
        screenTitle = screenTitle,
        itemList = stockList,
        menuOptions = menuOptions.toTypedArray(),
        onItemClick = onItemClick,
        onMenuItemClick = {},
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}