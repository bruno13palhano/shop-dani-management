package com.bruno13palhano.shopdanimanagement.ui.screens.stockorders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.SearchContent
import com.bruno13palhano.shopdanimanagement.ui.screens.stockorders.viewmodel.StockSearchViewModel

@Composable
fun StockSearchScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: StockSearchViewModel = hiltViewModel()
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
            viewModel.search(search)
            viewModel.insertSearch(search)
        },
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}