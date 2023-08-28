package com.bruno13palhano.shopdanimanagement.ui.screens.stockorder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.SearchContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorder.viewmodel.StockOrderSearchViewModel

@Composable
fun StockOrderSearchScreen(
    isOrderedByCustomer: Boolean,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockOrderSearchViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getSearchCache()
    }

    val stockOrderItems by viewModel.stockOrderItems.collectAsStateWithLifecycle()
    val searchCacheList by viewModel.searchCache.collectAsStateWithLifecycle()

    SearchContent(
        stockProducts = stockOrderItems,
        searchCacheList = searchCacheList,
        onSearchClick = { search ->
            viewModel.search(search, isOrderedByCustomer)
            viewModel.insertSearch(search)
        },
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}