package com.bruno13palhano.shopdanimanagement.ui.screens.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.ui.components.SearchContent
import com.bruno13palhano.shopdanimanagement.ui.screens.products.viewmodel.SearchProductsViewModel

@Composable
fun SearchProductScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getSearchCache()
    }
    val stockProducts by viewModel.stockProducts.collectAsStateWithLifecycle()
    val searchCacheList by viewModel.searchCache.collectAsStateWithLifecycle()

    SearchContent(
        stockProducts = stockProducts,
        searchCacheList = searchCacheList,
        onSearchClick = { search ->
            viewModel.search(search)
            viewModel.insertSearch(search)
        },
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}