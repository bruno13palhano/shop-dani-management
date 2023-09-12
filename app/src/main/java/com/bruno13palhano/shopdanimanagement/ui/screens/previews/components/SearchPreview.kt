package com.bruno13palhano.shopdanimanagement.ui.screens.previews.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruno13palhano.core.model.SearchCache
import com.bruno13palhano.core.model.Stock
import com.bruno13palhano.shopdanimanagement.ui.components.SearchContent
import com.bruno13palhano.shopdanimanagement.ui.theme.ShopDaniManagementTheme

@Preview(showBackground = true,  showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SearchDynamicPreview() {
    ShopDaniManagementTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchContent(
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun SearchPreview() {
    ShopDaniManagementTheme(
        dynamicColor = false
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            SearchContent(
                stockProducts = items,
                searchCacheList = searchCacheList,
                onSearchClick = {},
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
    Stock(id= 6L, name = "Product 6", photo = "", purchasePrice = 12.12F, quantity = 2),
)
private val searchCacheList = listOf(
    SearchCache(search = "perfume"),
    SearchCache(search = "essencial"),
    SearchCache(search = "gits"),
    SearchCache(search = "soaps"),
    SearchCache(search = "avon"),
    SearchCache(search = "homem")
)