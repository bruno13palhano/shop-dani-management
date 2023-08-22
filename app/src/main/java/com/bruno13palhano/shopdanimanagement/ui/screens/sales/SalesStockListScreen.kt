package com.bruno13palhano.shopdanimanagement.ui.screens.sales

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.StockItem
import com.bruno13palhano.shopdanimanagement.ui.screens.sales.viewmodel.SalesStockListViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun SalesStockListScreen(
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit,
    viewModel: SalesStockListViewModel = hiltViewModel()
) {
    val stockList by viewModel.stockList.collectAsStateWithLifecycle()

    SalesStockListContent(
        stockList = stockList,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesStockListContent(
    stockList: List<Stock>,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
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
                }
            )
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(4.dp),
            columns = GridCells.Adaptive(152.dp)
        ) {
            items(items = stockList, key = { item -> item.id }) { stock ->
                StockItem(
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
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SalesStockListPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SalesStockListContent(
                stockList = items,
                onItemClick = {},
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