package com.bruno13palhano.shopdanimanagement.ui.screens.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalStockItem
import com.bruno13palhano.shopdanimanagement.ui.components.SimpleItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.viewmodel.SearchProductsViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun SearchProductScreen(
    isOrderedByCustomer: Boolean,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SearchProductsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getSearchCache(isOrderedByCustomer)
    }
    val stockProducts by viewModel.stockProducts.collectAsStateWithLifecycle()
    val searchCacheList by viewModel.searchCache.collectAsStateWithLifecycle()

    SearchProductContent(
        stockProducts = stockProducts,
        searchCacheList = searchCacheList,
        onSearchClick = { search ->
            viewModel.search(search, isOrderedByCustomer)
            viewModel.insertSearch(search, isOrderedByCustomer)
        },
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductContent(
    stockProducts: List<Stock>,
    searchCacheList: List<SearchCache>,
    onSearchClick: (search: String) -> Unit,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    var search by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(true) }

    Scaffold(topBar = { TopAppBar( title = {} ) }) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth(),
            query = search,
            onQueryChange = { searchValue -> search = searchValue },
            onSearch = { searchValue ->
                active = false
                onSearchClick(searchValue)
            },
            active = active,
            onActiveChange = { isActive ->
                active = isActive
            },
            leadingIcon = {
                IconButton(onClick = { if (active) active = false else navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.up_button_label)
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    active = false
                    onSearchClick(search)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = R.string.search_label)
                    )
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.search_products_label)) }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                reverseLayout = true
            ) {
                items(items = searchCacheList, key = { searchCache -> searchCache.search }) { searchCache ->
                    SimpleItemList(
                        itemName = searchCache.search,
                        imageVector = Icons.Filled.Close,
                        onClick = {
                            active = false
                            onSearchClick(searchCache.search)
                        }
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(items = stockProducts, key = { stock -> stock.id } ) { stock ->
                HorizontalStockItem(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    name = stock.name,
                    photo = stock.photo,
                    price = stock.purchasePrice,
                    quantity = stock.quantity,
                    onClick = { onItemClick(stock.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true,  showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SearchProductDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchProductContent(
                stockProducts = items,
                searchCacheList = searchCacheList,
                onSearchClick = {},
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true,  showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SearchProductPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchProductContent(
                stockProducts = items,
                searchCacheList = searchCacheList,
                onSearchClick = {},
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

val items = listOf(
    Stock(id= 1L, name = "Product 1", photo = "", purchasePrice = 120.45F, quantity = 12),
    Stock(id= 2L, name = "Product 2", photo = "", purchasePrice = 40.33F, quantity = 2),
    Stock(id= 3L, name = "Product 3", photo = "", purchasePrice = 99.99F, quantity = 7),
    Stock(id= 4L, name = "Product 4", photo = "", purchasePrice = 12.39F, quantity = 2),
    Stock(id= 5L, name = "Product 5", photo = "", purchasePrice = 56.78F, quantity = 1),
    Stock(id= 6L, name = "Product 6", photo = "", purchasePrice = 12.12F, quantity = 2),
)
val searchCacheList = listOf(
    SearchCache(search = "perfume", isOrderedByCustomer = true),
    SearchCache(search = "essencial", isOrderedByCustomer = false),
    SearchCache(search = "gits", isOrderedByCustomer = true),
    SearchCache(search = "soaps", isOrderedByCustomer = false),
    SearchCache(search = "avon", isOrderedByCustomer = true),
    SearchCache(search = "homem", isOrderedByCustomer = false),
)