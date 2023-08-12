package com.bruno13palhano.shopdanimanagement.ui.screens.stock

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalProductItem
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun ProductStockListScreen(
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    ProductStockListContent(
        productList = emptyList(),
        categoryId = categoryId,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductStockListContent(
    productList: List<Stock>,
    categoryId: Long,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.products_label)) },
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
        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 8.dp)
        ) {
            items(productList) { stock ->
                HorizontalProductItem(
                    modifier = Modifier.padding(vertical = 4.dp),
                    name = stock.name,
                    photo = stock.photo,
                    price = stock.purchasePrice,
                    onClick = { stock.id }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductStockListDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductStockListContent(
                productList = productList,
                categoryId = 1L,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun ProductStockListPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ProductStockListContent(
                productList = productList,
                categoryId = 1L,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val productList = listOf(
    Stock(id = 1L, name = "Essencial", photo = "", purchasePrice = 123.45F, quantity = 2),
    Stock(id = 1L, name = "Homem", photo = "", purchasePrice = 63.45F, quantity = 3),
    Stock(id = 1L, name = "Una", photo = "", purchasePrice = 58.90F, quantity = 5),
    Stock(id = 1L, name = "Kaiak", photo = "", purchasePrice = 66.50F, quantity = 3),
    Stock(id = 1L, name = "Luna", photo = "", purchasePrice = 88.99F, quantity = 6),
    Stock(id = 1L, name = "Essencial", photo = "", purchasePrice = 123.45F, quantity = 2),
    Stock(id = 1L, name = "Homem", photo = "", purchasePrice = 63.45F, quantity = 3),
    Stock(id = 1L, name = "Una", photo = "", purchasePrice = 58.90F, quantity = 5),
    Stock(id = 1L, name = "Kaiak", photo = "", purchasePrice = 66.50F, quantity = 3),
    Stock(id = 1L, name = "Luna", photo = "", purchasePrice = 88.99F, quantity = 6)
)