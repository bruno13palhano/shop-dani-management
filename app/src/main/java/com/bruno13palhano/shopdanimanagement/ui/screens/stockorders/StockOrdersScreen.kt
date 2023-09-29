package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.StockOrderListContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockOrdersViewModel

@Composable
fun StockOrdersScreen(
    isOrderedByCustomer: Boolean,
    isAddButtonEnabled: Boolean,
    screenTitle: String,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockOrdersViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getItems(isOrderedByCustomer)
    }

    val menuOptions =
        if (!isOrderedByCustomer) {
            mutableListOf(
                stringResource(id = R.string.all_products_label),
                stringResource(id = R.string.out_of_stock_label)
            )
        } else {
            mutableListOf(
                stringResource(id = R.string.all_products_label)
            )
        }
    val stockList by viewModel.stockList.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    menuOptions.addAll(categories)

    StockOrderListContent(
        isAddButtonEnabled = isAddButtonEnabled,
        screenTitle = screenTitle,
        itemList = stockList,
        menuOptions = menuOptions.toTypedArray(),
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onMenuItemClick = { index ->
            if (!isOrderedByCustomer) {
                when (index) {
                    0 -> {
                        viewModel.getItems(false)
                    }
                    1 -> { viewModel.getOutOfStock() }
                    else -> {
                        viewModel.getItemsByCategories(menuOptions[index], false)
                    }
                }
            } else {
                when (index) {
                    0 -> {
                        viewModel.getItems(true)
                    }
                    else -> {
                        viewModel.getItemsByCategories(menuOptions[index], true)
                    }
                }
            }
        },
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}