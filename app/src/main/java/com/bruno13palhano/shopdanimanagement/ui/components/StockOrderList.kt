package com.bruno13palhano.shopdanimanagement.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.StockOrder
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockOrderListContent(
    isOrderedByCustomer: Boolean,
    screenTitle: String,
    itemList: List<StockOrder>,
    menuOptions: Array<String>,
    onItemClick: (id: Long) -> Unit,
    onMenuItemClick: (index: Int) -> Unit,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                },
                actions = {
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
        },
        floatingActionButton = {
            if (!isOrderedByCustomer) {
                FloatingActionButton(onClick = onAddButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_label)
                    )
                }
            }
        }
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(4.dp),
            columns = GridCells.Adaptive(152.dp)
        ) {
            items(itemList) { stockOrder ->
                StockItem(
                    modifier = Modifier.padding(4.dp),
                    name = stockOrder.name,
                    photo = stockOrder.photo,
                    price = stockOrder.purchasePrice,
                    quantity = stockOrder.quantity,
                    onClick = { onItemClick(stockOrder.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun StockOrderListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockOrderListContent(
                isOrderedByCustomer = false,
                screenTitle = "Stock List",
                itemList = items,
                menuOptions = arrayOf(),
                onItemClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun StockOrderListItemPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockOrderListContent(
                isOrderedByCustomer = true,
                screenTitle = "Orders List",
                itemList = items,
                menuOptions = arrayOf(),
                onItemClick = {},
                onMenuItemClick = {},
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    StockOrder(id= 1L, productId = 1, name = "Product 1", photo = "", purchasePrice = 120.45F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 12, isOrderedByCustomer = false),
    StockOrder(id= 2L, productId = 2, name = "Product 2", photo = "", purchasePrice = 40.33F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 2, isOrderedByCustomer = false),
    StockOrder(id= 3L, productId = 3, name = "Product 3", photo = "", purchasePrice = 99.99F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 7, isOrderedByCustomer = false),
    StockOrder(id= 4L, productId = 4, name = "Product 4", photo = "", purchasePrice = 12.39F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 2, isOrderedByCustomer = false),
    StockOrder(id= 5L, productId = 5, name = "Product 5", photo = "", purchasePrice = 56.78F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 1, isOrderedByCustomer = false),
    StockOrder(id= 6L, productId = 6, name = "Product 6", photo = "", purchasePrice = 12.12F, date = 0L,
        validity = 0L, categories = listOf(), company = "", salePrice = 0F, quantity = 2, isOrderedByCustomer = false),
)