package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.components.MoreOptionsMenu
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesStockListViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun SalesStockListScreen(
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesStockListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getItems()
    }

    val stockList by viewModel.stockList.collectAsStateWithLifecycle()
    val menuOptions = mutableListOf(stringResource(id = R.string.all_products_label))
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    menuOptions.addAll(categories)

    SalesStockListContent(
        stockList = stockList,
        menuOptions = menuOptions.toTypedArray(),
        onItemClick = onItemClick,
        onSearchClick = onSearchClick,
        onMenuItemClick = { index ->
            if (index == 0) {
                viewModel.getItems()
            } else {
                viewModel.getItemByCategories(menuOptions[index])
            }
        },
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesStockListContent(
    stockList: List<Stock>,
    menuOptions: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onSearchClick: () -> Unit,
    onMenuItemClick: (index: Int) -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_list_label)) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(id = R.string.search_label)
                        )
                    }
                    IconButton(onClick = { expanded = true }) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = stringResource(id = R.string.more_options_label)
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MoreOptionsMenu(
                                    items = menuOptions,
                                    expanded = expanded,
                                    onDismissRequest = { expanded = it },
                                    onClick = onMenuItemClick
                                )
                            }
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        ) {
            items(items = stockList, key = { item -> item.id }) { stock ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stock.name,
                    subtitle = stringResource(id = R.string.price_tag, stock.purchasePrice),
                    description = stringResource(id = R.string.quantity_tag, stock.quantity),
                    photo = stock.photo,
                    onClick = { onItemClick(stock.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SalesStockListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SalesStockListContent(
                stockList = items,
                menuOptions = emptyArray(),
                onItemClick = {},
                onSearchClick = {},
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SalesStockListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SalesStockListContent(
                stockList = items,
                menuOptions = emptyArray(),
                onItemClick = {},
                onSearchClick = {},
                onMenuItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    Stock(id= 1L, name = "Product 1", photo = "", purchasePrice = 120.45F, quantity = 12),
    Stock(id= 2L, name = "Product 2", photo = "", purchasePrice = 40.33F, quantity = 2),
    Stock(id= 3L, name = "Product 3", photo = "", purchasePrice = 99.99F, quantity = 7),
    Stock(id= 4L, name = "Product 4", photo = "", purchasePrice = 12.39F, quantity = 2),
    Stock(id= 5L, name = "Product 5", photo = "", purchasePrice = 56.78F, quantity = 1),
    Stock(id= 6L, name = "Product 6", photo = "", purchasePrice = 12.12F, quantity = 2)
)