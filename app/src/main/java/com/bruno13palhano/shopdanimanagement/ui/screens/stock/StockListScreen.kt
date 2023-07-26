package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.Category
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme
import com.bruno13palhano.shopdanimanagement.ui.components.StockItem

@Composable
fun StockListScreen(
    categoryId: String,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    StockListContent(
        categoryId = categoryId,
        stockList = emptyList(),
        onAddButtonClick = onAddButtonClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockListContent(
    categoryId: String,
    stockList: List<Stock>,
    onAddButtonClick: () -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = getTitle(categoryId))) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.up_button_label)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddButtonClick) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_label)
                )
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(it)
        ) {
            items(stockList) { stock ->
                StockItem(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    name = stock.name,
                    price = stock.purchasePrice.toString(),
                    quantity = stock.quantity,
                ) {

                }
            }
         }
    }
}

@StringRes
fun getTitle(categoryId: String): Int {
    return when (categoryId) {
        Category.GIFTS.category -> R.string.gifts_label
        Category.INFANT.category -> R.string.infant_label
        Category.PERFUMES.category -> R.string.perfumes_label
        Category.SOAPS.category -> R.string.soaps_label
        Category.ANTIPERSPIRANT_DEODORANTS.category -> R.string.antiperspirant_deodorants_label
        Category.DEODORANTS_COLOGNE.category -> R.string.deodorants_cologne_label
        Category.MOISTURIZERS.category -> R.string.moisturizers_label
        Category.SUNSCREENS.category -> R.string.sunscreens_label
        Category.MAKEUP.category -> R.string.makeup_label
        Category.FACE.category -> R.string.face_label
        Category.SKIN.category -> R.string.skin_label
        Category.HAIR.category -> R.string.hair_label
        else -> R.string.stock_product_list
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProductListPreview() {
    val items = listOf(
        Stock(id= 1L, name = "Product 1", purchasePrice = 120.45F, quantity = 12),
        Stock(id= 2L, name = "Product 2", purchasePrice = 40.33F, quantity = 2),
        Stock(id= 3L, name = "Product 3", purchasePrice = 99.99F, quantity = 7),
        Stock(id= 4L, name = "Product 4", purchasePrice = 12.39F, quantity = 2),
        Stock(id= 5L, name = "Product 5", purchasePrice = 56.78F, quantity = 1),
        Stock(id= 6L, name = "Product 6", purchasePrice = 12.12F, quantity = 2),
        Stock(id= 7L, name = "Product 7", purchasePrice = 67.99F, quantity = 4),
        Stock(id= 8L, name = "Product 8", purchasePrice = 81.90F, quantity = 6),
        Stock(id= 9L, name = "Product 9", purchasePrice = 77.90F, quantity = 2),
        Stock(id= 10L, name = "Product 10", purchasePrice = 99.00F, quantity = 5),
    )

    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockListContent(
                categoryId = "Perfume",
                stockList = items,
                onAddButtonClick = {},
                navigateUp = {}
            )
        }
    }
}