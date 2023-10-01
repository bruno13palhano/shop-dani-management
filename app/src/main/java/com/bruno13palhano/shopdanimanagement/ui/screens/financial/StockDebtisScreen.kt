package com.bruno13palhano.shopdanimanagement.ui.screens.financial

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bruno13palhano.shopdanimanagement.R
import com.bruno13palhano.shopdanimanagement.ui.components.HorizontalItemList
import com.bruno13palhano.shopdanimanagement.ui.screens.common.Stock
import com.bruno13palhano.shopdanimanagement.ui.screens.financial.viewmodel.StockDebitsViewModel
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Composable
fun StockDebitsScreen(
    navigateUp: () -> Unit,
    onItemClick: (id: Long) -> Unit,
    viewModel: StockDebitsViewModel = hiltViewModel()
) {
    val stockItems by viewModel.debitItems.collectAsStateWithLifecycle()

    StockDebitsContent(
        stockItems = stockItems,
        onItemClick = onItemClick,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockDebitsContent(
    stockItems: List<Stock>,
    onItemClick: (id: Long) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.stock_debits_label)) },
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
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            items(items = stockItems, key = { stockItem -> stockItem.id }) { stockItem ->
                HorizontalItemList(
                    modifier = Modifier.padding(vertical = 4.dp),
                    title = stockItem.name,
                    subtitle = stringResource(id = R.string.price_tag, stockItem.purchasePrice),
                    description = stringResource(id = R.string.quantity_tag, stockItem.quantity),
                    photo = stockItem.photo,
                    onClick = { onItemClick(stockItem.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun StockDebitsDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockDebitsContent(
                stockItems = items,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun StockDebitsPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            StockDebitsContent(
                stockItems = items,
                onItemClick = {},
                navigateUp = {}
            )
        }
    }
}

private val items = listOf(
    Stock(id= 1L, name = "Essencial", photo = byteArrayOf(), purchasePrice = 120.45F, quantity = 12),
    Stock(id= 2L, name = "Kaiak", photo = byteArrayOf(), purchasePrice = 40.33F, quantity = 2),
    Stock(id= 3L, name = "Homem", photo = byteArrayOf(), purchasePrice = 99.99F, quantity = 7),
    Stock(id= 4L, name = "Luna", photo = byteArrayOf(), purchasePrice = 12.39F, quantity = 2),
    Stock(id= 5L, name = "Essencial", photo = byteArrayOf(), purchasePrice = 56.78F, quantity = 1),
    Stock(id= 6L, name = "Una", photo = byteArrayOf(), purchasePrice = 12.12F, quantity = 2),
    Stock(id= 7L, name = "Essencial", photo = byteArrayOf(), purchasePrice = 120.45F, quantity = 12),
    Stock(id= 8L, name = "Kaiak", photo = byteArrayOf(), purchasePrice = 40.33F, quantity = 2),
    Stock(id= 9L, name = "Homem", photo = byteArrayOf(), purchasePrice = 99.99F, quantity = 7),
    Stock(id= 10L, name = "Luna", photo = byteArrayOf(), purchasePrice = 12.39F, quantity = 2),
    Stock(id= 11L, name = "Essencial", photo = byteArrayOf(), purchasePrice = 56.78F, quantity = 1),
    Stock(id= 12L, name = "Una", photo = byteArrayOf(), purchasePrice = 12.12F, quantity = 2)
)